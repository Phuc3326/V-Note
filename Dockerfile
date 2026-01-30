# Sử dụng image Tomcat 9 chính thức với JDK 17
FROM tomcat:9.0-jdk17-openjdk

# Xóa các ứng dụng mặc định của Tomcat để tránh xung đột
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file .war từ thư mục target vào thư mục webapps của Tomcat
# Đổi tên thành ROOT.war để ứng dụng chạy ngay tại trang chủ (/)
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng 8080
EXPOSE 8080

# Chạy Tomcat
CMD ["catalina.sh", "run"]