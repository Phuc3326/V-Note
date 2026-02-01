<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - V-Note</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        .login-card {
            padding: 2.5rem 2rem;
            border-radius: 1rem;
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
            background-color: #212529; 
            border: 1px solid #495057;
        }
        
        .form-control {
            background-color: #2b3035 !important;
            color: #ffffff !important;
            border-color: #495057 !important;
        }
        .form-control:focus {
            background-color: #32383e !important;
            border-color: #ffc107 !important;
            box-shadow: 0 0 0 0.25rem rgba(255, 193, 7, 0.25);
            color: #ffffff !important;
        }
        .text-white-50 { color: rgba(255, 255, 255, 0.5) !important; }
    </style>
</head>
<body class="d-flex flex-column min-vh-100 bg-dark text-light">
    <jsp:include page="/WEB-INF/views/common/header.jsp">
        <jsp:param value="login" name="pageName"/>
    </jsp:include>

    <main class="flex-grow-1 d-flex align-items-center">
        <div class="container py-5">
            <%
            Object objSes = session.getAttribute("user");
            User user = (objSes != null) ? (User) objSes : null;
            if (user != null) {
            %>
            <div class="row justify-content-center">
                <div class="col-11 col-md-8 col-lg-5 text-center">
                    <div class="alert alert-info border-0 shadow-sm bg-info bg-opacity-10 text-info">
                        <i class="bi bi-info-circle-fill me-2"></i>
                        You are already logged in! <br> 
                        Please <a href="${pageContext.request.contextPath}/user-controller?controllerType=logout" class="alert-link">logout</a> if you want to switch accounts.
                    </div>
                </div>
            </div>
            <%
            } else {
                Object obj = request.getAttribute("message");
                String msg = (obj != null) ? obj.toString() : "";
                String userName = request.getParameter("userName");
                userName = (userName != null) ? userName : "";
            %>
            <div class="row justify-content-center">
                <div class="col-11 col-sm-8 col-md-6 col-lg-4">
                    <div class="login-card">
                        <div class="text-center mb-4">
                            <h1 class="h3 fw-bold text-warning uppercase">ĐĂNG NHẬP</h1>
                            <p class="text-white-50 small">Chào mừng trở lại! Vui lòng nhập thông tin tài khoản để đăng nhập</p>
                        </div>

                        <form action="${pageContext.request.contextPath}/user-controller?controllerType=login" method="POST">
                            <div class="mb-3">
                                <label for="userName" class="form-label fw-semibold text-light">Tên Đăng Nhập</label>
                                <input type="text" class="form-control form-control-lg" id="userName" name="userName"
                                    required value="<%=userName%>">
                            </div>

                            <div class="mb-3">
                                <label for="password" class="form-label fw-semibold text-light">Mật Khẩu</label>
                                <input type="password" class="form-control form-control-lg" id="password"
                                    name="password" required>
                            </div>

                            <% if(!msg.isEmpty()) { %>
                                <div class="text-danger small mb-3">
                                    <i class="bi bi-exclamation-circle me-1"></i> <%=msg%>
                                </div>
                            <% } %>

                            <div class="d-grid gap-2 mt-4">
                                <button class="btn btn-warning btn-lg fw-bold shadow-sm" type="submit">
                                    ĐĂNG NHẬP
                                </button>
                            </div>

                            <div class="text-center mt-4">
                                <span class="text-white-50 small">Chưa có tài khoản?</span> <br>
                                <a href="${pageContext.request.contextPath}/user-controller?controllerType=sign-up"
                                    class="text-decoration-none fw-bold text-warning">Đăng ký ở đây</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </main>

    

    <jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>