<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password - V-Note</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <style>
        .password-card {
            padding: 2.5rem 2rem;
            border-radius: 1rem;
            /* Đổ bóng đậm hơn cho nổi bật trên nền tối */
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
            background: #212529; 
            border: 1px solid #495057;
        }
        .red { color: #ff6b6b; }
        
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
    </style>
</head>
<body class="d-flex flex-column min-vh-100 bg-dark text-light">
    <jsp:include page="/WEB-INF/views/common/header.jsp">
        <jsp:param value="change-password" name="pageName"/>
    </jsp:include>

    <main class="flex-grow-1 py-5">
        <div class="container">
            <%
            Object objSes = session.getAttribute("user");
            User user = (objSes != null) ? (User) objSes : null;
            if (user == null) {
            %>
            <div class="text-center">
                <h1 class="display-6 fw-bold text-light">THAY ĐỔI MẬT KHẨU</h1>
                <div class="alert alert-warning mt-4 d-inline-block shadow-sm">
                    <i class="bi bi-shield-lock-fill me-2"></i>
                    Bạn chưa đăng nhập! Vui lòng <a href="${pageContext.request.contextPath}/user-controller?controllerType=login" class="alert-link">đăng nhập</a>.
                </div>
            </div>
            <%
            } else {
                Object obj = session.getAttribute("msg");
                String msg = (obj != null) ? obj.toString() : "";
                session.removeAttribute("msg");
            %>
            <div class="row justify-content-center">
                <div class="col-11 col-md-8 col-lg-5">
                    <div class="password-card">
                        <div class="text-center mb-4">
                            <h1 class="h3 fw-bold text-warning uppercase">THAY ĐỔI MẬT KHẨU</h1>
                            <p class="text-white-50 small">Nhập mật khẩu hiện tại và mật khẩu mới</p>
                        </div>

                        <form action="${pageContext.request.contextPath}/user-controller?controllerType=change-password" method="post">
                            <div class="mb-3">
                                <label for="password" class="form-label fw-semibold text-light">Mật khẩu hiện tại<span class="red">*</span></label>
                                <input type="password" class="form-control form-control-lg" id="password" name="password" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="newPassword" class="form-label fw-semibold text-light">Mật khẩu mới<span class="red">*</span></label>
                                <input type="password" class="form-control form-control-lg" id="newPassword" name="newPassword" required minlength="8" placeholder="Ít nhất 8 ký tự">
                            </div>
                            
                            <div class="mb-4">
                                <label for="passwordReEnter" class="form-label fw-semibold text-light">
                                    Nhập lại mật khẩu mới<span class="red">*</span>
                                    <span id="error-password" class="red small d-block mt-1"></span>
                                </label>
                                <input type="password" class="form-control form-control-lg" id="passwordReEnter" name="passwordReEnter" required>
                            </div>

                            <% if(!msg.isEmpty()) { %>
                                <div class="alert alert-danger py-2 px-3 mb-4 small bg-danger bg-opacity-10 border-danger text-danger">
                                    <i class="bi bi-info-circle me-1"></i> <%=msg%>
                                </div>
                            <% } %>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold shadow-sm">
                                    <i class="bi bi-check-circle me-2"></i>Save Password
                                </button>
                                <a href="${pageContext.request.contextPath}/" class="btn btn-link text-secondary text-decoration-none text-center small">Back to Home</a>
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
    <script>
        const pwdReEnter = document.getElementById('passwordReEnter');
        const pwd = document.getElementById('newPassword');
        const pwd_error = document.getElementById('error-password');

        if(pwdReEnter) {
            pwdReEnter.addEventListener('blur', function() {
                if (pwdReEnter.value !== pwd.value && pwdReEnter.value !== "") {
                    pwd_error.innerText = "Mật khẩu nhập lại không khớp!";
                    pwdReEnter.classList.add('is-invalid');
                } else {
                    pwd_error.innerText = "";
                    pwdReEnter.classList.remove('is-invalid');
                    if (pwdReEnter.value !== "") pwdReEnter.classList.add('is-valid');
                }
            });
        }

        const form = document.querySelector('form');
        if(form) {
            form.addEventListener('submit', function(event) {
                if (pwdReEnter.value !== pwd.value) {
                    event.preventDefault();
                    pwdReEnter.focus();
                }
            });
        }
    </script>
</body>
</html>