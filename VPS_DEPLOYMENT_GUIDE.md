# HƯỚNG DẪN TRIỂN KHAI DỰ ÁN V-NOTE LÊN LINUX VPS TỪ A - Z
> **Tài liệu chuẩn hóa quy trình DevOps:** Hướng dẫn từng bước từ khi nhận thông tin VPS mới (Ubuntu 22.04 LTS) cho đến khi website chạy thực tế với Docker, Nginx Reverse Proxy và chứng chỉ bảo mật HTTPS (SSL).

---

## 🏛️ Sơ đồ Kiến trúc Triển khai (Architecture Overview)

```
[ Người dùng / Nhà tuyển dụng ]
               │
               ▼ HTTPS (Cổng 443)
       ┌───────────────┐
       │     Nginx     │  (Xử lý SSL Certificate Let's Encrypt
       │ Reverse Proxy │   & Điều hướng domain vnote.domain.com)
       └───────┬───────┘
               │ Chuyển tiếp nội bộ (HTTP Port 8080)
    ───────────┼─────────────────────────────────────
    [ DOCKER COMPOSE INTERNAL NETWORK ]
               │
               ├──► [ vnote-webapp ] (Tomcat 9 - Java 11)
               │          │
               │          ▼ Kết nối nội bộ port 5432
               └──► [ vnote-postgres ] (PostgreSQL 16 Alpine)
                          │
                          ▼ Gắn Named Volume
                    (vnote_pgdata - Dữ liệu vĩnh viễn)
```

---

## BƯỚC 1: Kết nối SSH vào VPS lần đầu

Mở **PowerShell**, **Terminal** (macOS/Linux) hoặc **Git Bash** trên máy tính cá nhân và chạy:

```bash
ssh root@<IP_VPS_CỦA_BẠN>
```
*Ví dụ:* `ssh root@45.77.32.161`

- Khi hệ thống hỏi: `Are you sure you want to continue connecting (yes/no/[fingerprint])?` ➔ Nhập `yes` rồi nhấn **Enter**.
- Nhập mật khẩu `root` được nhà cung cấp VPS bàn giao.
  > ⚠️ **Lưu ý:** Trên Linux, khi gõ hoặc dán mật khẩu màn hình sẽ **hoàn toàn không hiện ký tự nào** (kể cả dấu hoa thị `***`). Cứ dán (chuột phải) rồi bấm **Enter**.

---

## BƯỚC 2: Cập nhật hệ thống & Cài đặt gói phần mềm cốt lõi

Chạy tổ hợp lệnh sau để nâng cấp Ubuntu và cài đặt các công cụ cần thiết:

```bash
# 1. Cập nhật danh sách gói và nâng cấp phần mềm
apt update && apt upgrade -y

# 2. Cài Docker Engine, Docker Compose v2, Git, Nginx và Certbot (SSL)
apt install -y docker.io docker-compose-v2 git nginx certbot python3-certbot-nginx

# 3. Kích hoạt dịch vụ Docker và Nginx tự khởi động cùng VPS
systemctl enable --now docker
systemctl enable --now nginx
```

> 📌 **Lưu ý khi nâng cấp:** Nếu gặp màn hình màu tím hỏi về cấu hình file `sshd_config` hoặc `daemons`, nhấn **Enter** để giữ cấu hình mặc định an toàn.

---

## BƯỚC 3: Thiết lập Bộ nhớ ảo (Swap RAM)

Web app Java (Tomcat) và quá trình biên dịch Maven cần dung lượng bộ nhớ ổn định. Tạo **3GB - 4GB Swap** giúp hệ thống không bao giờ bị tràn RAM hay sập tiến trình ngầm (OOM Killer).

```bash
# Tạo file swap 3GB (nếu muốn 4GB thay 3G thành 4G)
fallocate -l 3G /swapfile
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile

# Cấu hình tự kích hoạt Swap mỗi khi khởi động lại VPS
echo '/swapfile none swap sw 0 0' >> /etc/fstab

# Kiểm tra lại tổng dung lượng RAM và Swap
free -h
```

---

## BƯỚC 4: Mở Tường lửa Hệ thống (UFW Firewall)

> ⚠️ **Bài học thực tế:** Nếu không mở cổng 80 & 443 trước, nhà mạng sẽ chặn và chứng chỉ SSL Let's Encrypt sẽ báo lỗi `Timeout during connect`.

```bash
# Mở cổng SSH quản trị, cổng HTTP web và cổng HTTPS bảo mật
ufw allow 22/tcp
ufw allow 80/tcp
ufw allow 443/tcp

# Bật tường lửa
ufw --force enable
ufw reload
```

---

## BƯỚC 5: Cấu hình DNS Tên miền (Domain Records)

Truy cập vào trang quản trị tên miền của bạn (Namecheap, Cloudflare, GoDaddy,...):
1. Vào mục **Advanced DNS** / **DNS Management**.
2. Thêm một bản ghi **A Record**:
   - **Type:** `A Record`
   - **Host:** `vnote` *(hoặc tên subdomain bạn muốn)*
   - **Value:** `<IP_VPS_CỦA_BẠN>` *(Ví dụ: `45.77.32.161`)*
   - **TTL:** `Automatic` (hoặc `1 min`)

---

## BƯỚC 6: Clone Mã nguồn và Thiết lập Biến môi trường

### 1. Kéo code từ GitHub về VPS
```bash
cd ~
git clone https://github.com/hufudev/V-Note.git
cd V-Note
```

### 2. Tạo file `.env` chứa thông tin cấu hình sản xuất
Chạy lệnh bên dưới (nhớ thay đổi mật khẩu và API Key thực tế của bạn):

```bash
cat << 'EOF' > .env
POSTGRES_DB=vnote_db
DB_USER=vnote_user
DB_PASS=ThayDoi_MatKhau_Production_Cua_Ban_Tai_Day_2026!
BREVO_API_KEY=xkeysib-your-real-brevo-api-key-here
EMAIL_USER=support@yourdomain.me
EOF
```

> 🔒 **Nguyên tắc bảo mật:** File `.env` chứa bí mật kinh doanh và mật khẩu nên đã được đưa vào `.gitignore`, tuyệt đối không đẩy file này lên GitHub công khai.

---

## BƯỚC 7: Khởi chạy Docker Containers

Thực thi lệnh đóng gói Maven và chạy dịch vụ ngầm:

```bash
docker compose up -d --build
```

Kiểm tra trạng thái các container:
```bash
docker ps
```
*Kết quả chuẩn:*
- `vnote-postgres`: Status `Up ... (healthy)`
- `vnote-webapp`: Status `Up ...` (Port `0.0.0.0:8080->8080/tcp`)

---

## BƯỚC 8: Cấu hình Nginx Reverse Proxy & Cấp SSL HTTPS

### 1. Tạo file cấu hình Nginx đón tên miền
*(Thay `vnote.yourdomain.me` bằng tên miền thực tế của bạn)*

```bash
cat << 'EOF' > /etc/nginx/sites-available/vnote.yourdomain.me
server {
    listen 80;
    server_name vnote.yourdomain.me;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF
```

### 2. Kích hoạt cấu hình và nạp lại Nginx
```bash
ln -sf /etc/nginx/sites-available/vnote.yourdomain.me /etc/nginx/sites-enabled/
nginx -t && systemctl reload nginx
```

### 3. Cấp chứng chỉ SSL miễn phí tự động gia hạn (Let's Encrypt)
```bash
certbot --nginx -d vnote.yourdomain.me --non-interactive --agree-tos -m email-cua-ban@gmail.com
```

🎉 **Hoàn tất!** Giờ đây bạn đã có thể truy cập vào website qua địa chỉ:
`https://vnote.yourdomain.me` với ổ khóa bảo mật SSL màu xanh chuẩn quốc tế.

---

## 🛠️ Cẩm Nang Quản Trị & Vận Hành (Cheatsheet Thường Dùng)

| Nhu cầu thao tác | Câu lệnh thực hiện trên VPS |
| :--- | :--- |
| **Xem log trực tiếp của web Java** | `docker compose logs -f vnote-app` |
| **Xem log của Database Postgres** | `docker compose logs -f vnote-db` |
| **Khởi động lại toàn bộ app** | `docker compose restart` |
| **Dừng toàn bộ hệ thống** | `docker compose down` |
| **Kiểm tra RAM & Swap còn trống** | `free -h` |
| **Kiểm tra dung lượng ổ đĩa SSD** | `df -h` |

### Quy trình cập nhật code khi bạn vừa sửa code ở máy local và đẩy lên GitHub:
```bash
cd ~/V-Note
git pull origin main
docker compose up -d --build
```
*(Docker sẽ tự phát hiện file thay đổi, biên dịch lại file WAR mới và cập nhật website mà không làm mất dữ liệu trong database).*
