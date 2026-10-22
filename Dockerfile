# ==========================================
# STAGE 1: Build mã nguồn bằng Maven & OpenJDK 11
# ==========================================
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app

# Copy pom.xml và tải thư viện trước để tối ưu cache của Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy toàn bộ mã nguồn và biên dịch thành file .war
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Khởi chạy ứng dụng bằng Tomcat 9
# ==========================================
FROM tomcat:9.0-jdk11-corretto
WORKDIR /usr/local/tomcat

# Dọn dẹp các ứng dụng mẫu mặc định của Tomcat
RUN rm -rf webapps/*

# Copy file .war vừa build ở Stage 1 vào webapps và đổi tên thành ROOT.war để chạy tại trang chủ (/)
COPY --from=build /app/target/*.war webapps/ROOT.war

# Mở cổng ứng dụng
EXPOSE 8080

# Chạy server Tomcat (Tự động thích ứng cả Local 8080 lẫn cổng PORT ngẫu nhiên của Heroku)
CMD sh -c "sed -i 's/port=\"8080\"/port=\"'\"${PORT:-8080}\"'\"/g' conf/server.xml && catalina.sh run"
