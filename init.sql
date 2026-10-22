-- ========================================================
-- KHỞI TẠO CẤU TRÚC DATABASE V-NOTE (POSTGRESQL)
-- File này tự động thực thi khi container Postgres tạo mới
-- ========================================================

-- 1. Bảng lưu trữ thông tin người dùng
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    dateofbirth DATE,
    gender VARCHAR(20),
    fullname VARCHAR(255),
    phone VARCHAR(30)
);

-- 2. Bảng lưu trữ ghi chú
CREATE TABLE IF NOT EXISTS notes (
    id VARCHAR(50) PRIMARY KEY,
    userid VARCHAR(50) NOT NULL,
    title VARCHAR(255),
    content TEXT,
    createdate DATE,
    lasteditdate DATE,
    isarchived BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_user FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Tạo tài khoản mẫu để test nhanh sau khi khởi động
-- Tài khoản: demo | Mật khẩu: 12345678 (mã hóa SHA-1: 7c222fb2927d828af22f592134e8932480637c0d)
INSERT INTO users (id, username, password, email, dateofbirth, gender, fullname, phone)
VALUES ('U20260101000001', 'demo', '7c222fb2927d828af22f592134e8932480637c0d', 'demo@hufudev.me', '2000-01-01', 'Nam', 'Demo User', '0901234567')
ON CONFLICT (id) DO NOTHING;

-- 4. Thêm 2 ghi chú mẫu cho tài khoản demo
INSERT INTO notes (id, userid, title, content, createdate, lasteditdate, isarchived)
VALUES 
('N20260101000001', 'U20260101000001', 'Chào mừng đến với V-Note!', 'Hệ thống V-Note đã được đóng gói thành công bằng Docker và PostgreSQL!', CURRENT_DATE, CURRENT_DATE, FALSE),
('N20260101000002', 'U20260101000001', 'Mục tiêu thực tập', 'Hoàn thành việc triển khai Docker, Reverse Proxy và đưa link vào CV.', CURRENT_DATE, CURRENT_DATE, FALSE)
ON CONFLICT (id) DO NOTHING;
