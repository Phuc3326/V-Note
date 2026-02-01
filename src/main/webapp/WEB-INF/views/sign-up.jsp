<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="util.CheckValidData"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign-Up - V-Note</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        .register-card {
            padding: 2.5rem 2rem;
            border-radius: 1rem;
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
            background-color: #212529; 
            border: 1px solid #495057;
        }
        .red { color: #ff6b6b; }
        .form-label { font-weight: 600; color: #e9ecef; }
        
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
        <jsp:param value="sign-up" name="pageName"/>
    </jsp:include>

    <main class="flex-grow-1 py-5">
        <div class="container">
            <%
            Object obj = request.getAttribute("message");
            String msg = "";
            if (obj != null) {
                msg = obj.toString();
            } else {
                Object objSes = session.getAttribute("message");
                msg = (objSes != null) ? objSes.toString() : "";
                session.removeAttribute("message");
            }

            String fullName = request.getParameter("fullName") != null ? request.getParameter("fullName") : "";
            String userName = request.getParameter("userName") != null ? request.getParameter("userName") : "";
            String dateOfBirth = request.getParameter("dateOfBirth") != null ? request.getParameter("dateOfBirth") : "";
            if (!CheckValidData.isValidDateOfBirth(dateOfBirth)) dateOfBirth = "";
            String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "";
            String email = request.getParameter("email") != null ? request.getParameter("email") : "";
            String gender = request.getParameter("gender") != null ? request.getParameter("gender") : "";
            if (!CheckValidData.isValidGender(gender)) gender = "";
            %>

            <div class="row justify-content-center">
                <div class="col-11 col-md-9 col-lg-7 col-xl-6">
                    <div class="register-card">
                        <div class="text-center mb-4">
                            <h1 class="h3 fw-bold text-warning uppercase">TẠO TÀI KHOẢN</h1>
                            <p class="text-white-50 small">Sử dụng V-Note để quản lý ghi chú của bạn ở bất cứ đâu</p>
                        </div>

                        <form action="${pageContext.request.contextPath}/user-controller?controllerType=sign-up" method="post">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="fullName" class="form-label">Họ Và Tên<span class="red">*</span></label>
                                    <input placeholder="Nhập đầy đủ họ và tên" type="text" class="form-control" id="fullName" name="fullName" required value="<%=fullName%>">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="userName" class="form-label">Tên Đăng Nhập<span class="red">*</span>
                                    <span id="error-username" class="red small"></span></label>
                                    <input placeholder="Không chứa ký tự đặc biệt" type="text" class="form-control" id="userName" name="userName" required value="<%=userName%>">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label">Mật Khẩu<span class="red">*</span></label>
                                    <input placeholder="Mật khẩu ít nhất 8 ký tự" type="password" class="form-control" id="password" name="password" required minlength="8">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="passwordReEnter" class="form-label">Nhập lại mật khẩu<span class="red">*</span></label>
                                    <input placeholder="Nhập lại mật khẩu đã đặt" type="password" class="form-control" id="passwordReEnter" name="passwordReEnter" required>
                                    <div id="error-password" class="red small mt-1"></div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Địa Chỉ Email<span class="red">*</span></label>
                                <input placeholder="VD: abc@gmail.com" type="email" class="form-control" id="email" name="email" required value="<%=email%>">
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label">Số điện thoại <span id="error-phone" class="red small"></span></label>
                                    <input type="text" class="form-control" id="phone" name="phone" value="<%=phone%>" placeholder="0xxxxxxxxx">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="dateOfBirth" class="form-label">Ngày Sinh</label>
                                    <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" value="<%=dateOfBirth%>">
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label d-block text-light">Giới tính</label>
                                <div class="d-flex gap-4 mt-2">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="gender" id="genderMale" value="male" <%=gender.equals("male") ? "checked" : ""%>>
                                        <label class="form-check-label text-light" for="genderMale">Nam</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="gender" id="genderFemale" value="female" <%=gender.equals("female") ? "checked" : ""%>>
                                        <label class="form-check-label text-light" for="genderFemale">Nữ</label>
                                    </div>
                                </div>
                            </div>

                            <% if(!msg.isEmpty()) { %>
                                <div class="alert alert-danger py-2 px-3 mb-4 small bg-danger bg-opacity-10 border-danger text-danger">
                                    <i class="bi bi-exclamation-circle me-1"></i> <%=msg%>
                                </div>
                            <% } %>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold shadow-sm">Đăng Ký</button>
                                <p class="text-center mt-3 small text-white-50">
                                    Đã có tài khoản? <a href="${pageContext.request.contextPath}/user-controller?controllerType=login" class="text-decoration-none fw-bold text-warning">Đăng nhập ở đây</a>
                                </p>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    

    <jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
	 	// KIỂM TRA USERNAME (CHỈ CHỮ VÀ SỐ, KHÔNG KHOẢNG CÁCH, KHÔNG KÝ TỰ ĐẶT BIỆT)
	    const regexUsername = /^[a-zA-Z0-9]+$/;
	    const userNameInput = document.getElementById('userName');
	    const userNameError = document.getElementById('error-username');
	
	    userNameInput.addEventListener('blur', function() {
	        if (userNameInput.value !== "" && !regexUsername.test(userNameInput.value)) {
	            userNameError.innerText = " (Tên đăng nhập không bao gồm khoản trắng và ký tự đặc biệt)";
	            userNameInput.classList.add('is-invalid');
	        } else {
	            userNameError.innerText = "";
	            userNameInput.classList.remove('is-invalid');
	        }
	    });
    
        const dob = document.getElementById('dateOfBirth');
        const today = new Date().toISOString().split('T')[0];
        if(dob) dob.setAttribute('max', today);

        const pwdReEnter = document.getElementById('passwordReEnter');
        const pwd = document.getElementById('password');
        const pwd_error = document.getElementById('error-password');
        
        if(pwdReEnter) {
            pwdReEnter.addEventListener('blur', function() {
                if (pwdReEnter.value !== pwd.value && pwdReEnter.value !== "") {
                    pwd_error.innerText = "Mật khẩu nhập lại không khớp!";
                    pwdReEnter.classList.add('is-invalid');
                } else {
                    pwd_error.innerText = "";
                    pwdReEnter.classList.remove('is-invalid');
                }
            });
        }

        const regexPhone = /^0\d{9}$/;
        const phone = document.getElementById('phone');
        const phone_error = document.getElementById('error-phone');
        
        if(phone) {
            phone.addEventListener('blur', function() {
                if (phone.value !== "" && !regexPhone.test(phone.value)) {
                    phone_error.innerText = " (10 số bắt đầu với 0)";
                    phone.classList.add('is-invalid');
                } else {
                    phone_error.innerText = "";
                    phone.classList.remove('is-invalid');
                }
            });
        }

        const form = document.querySelector('form');
        if(form) {
            form.addEventListener('submit', function(event) {
                let hasError = false;
                if (userNameInput.value !== "" && !regexUsername.test(userNameInput.value)) {
                    hasError = true;
                    userNameInput.focus();
                }
                if (pwdReEnter.value !== pwd.value) hasError = true;
                if (phone.value !== "" && !regexPhone.test(phone.value)) hasError = true;
                if (hasError) event.preventDefault();
            });
        }
    </script>
</body>
</html>