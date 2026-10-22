# V-Note - Personal Note Management System

V-Note is a lightweight and secure web application designed for personal note-taking, daily task tracking, and information management, featuring two-factor email verification and soft-delete recovery mechanisms.

### Live Demo
- Website: https://vnote.hufudev.me

### Key Features
- **User Authentication & Security:** Secure registration, login, profile editing, and password management. Supports 6-digit OTP verification via transactional email.
- **Note Management (CRUD):** Quick note creation and editing via modal interface, with automatic timestamps for creation and latest modifications.
- **Search:** Instant keyword filtering by note title.
- **Trash & Data Recovery:** Soft-delete mechanism with a dedicated Trash bin to restore accidentally removed notes or delete them permanently.
- **Responsive Dark UI:** Clean dark theme built with Bootstrap 5, fully optimized for both desktop and mobile browsers.

### Technology Stack
- **Backend:** Java 11, Java Servlet, JSP, MVC Architecture, Apache Tomcat 9, Maven.
- **Database:** PostgreSQL 16, JDBC.
- **Email Service:** Brevo REST API v3 (HTTPS).
- **Frontend:** HTML5, CSS3, JavaScript, Bootstrap 5.
- **DevOps:** Docker, Multi-Stage Dockerfile, Docker Compose, Heroku.
