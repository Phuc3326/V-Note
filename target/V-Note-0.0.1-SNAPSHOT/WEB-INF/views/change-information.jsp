<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="util.DateConverter"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Information - V-Note</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        .form-card {
            padding: 2rem;
            border-radius: 15px;
            background-color: #212529; 
            border: 1px solid #495057;
            box-shadow: 0 10px 25px rgba(0,0,0,0.5);
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
        <jsp:param value="change-information" name="pageName"/>
    </jsp:include>

    <main class="flex-grow-1 py-5">
        <div class="container">
            <%
            Object objSes = session.getAttribute("user");
            User user = (objSes != null) ? (User) objSes : null;
            if (user == null) {
            %>
            <div class="text-center">
                <h1 class="display-5 fw-bold text-light">THAY ĐỔI THÔNG TIN</h1>
                <div class="alert alert-warning mt-4 d-inline-block shadow-sm">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    Bạn chưa đăng nhập! Vui lòng <a href="${pageContext.request.contextPath}/user-controller?controllerType=login" class="alert-link">đăng nhập</a> trước.
                </div>
            </div>
            <%
            } else {
                Object obj = session.getAttribute("msg");
                String msg = (obj != null) ? obj.toString() : "";
                session.removeAttribute("msg");

                String fullName = user.getFullName();
                String dateOfBirth = (user.getDateOfBirth() != null) ? user.getDateOfBirth().toString() : "";
                String phone = user.getPhone();
                String gender = user.getGender();
            %>
            <div class="row justify-content-center">
                <div class="col-11 col-md-8 col-lg-6">
                    <div class="form-card">
                        <h1 class="h3 mb-4 fw-bold text-center text-warning uppercase">THAY ĐỔI THÔNG TIN</h1>
                        
                        <form action="${pageContext.request.contextPath}/user-controller?controllerType=change-information" method="post">
                            <div class="mb-3">
                                <label for="fullName" class="form-label fw-semibold text-light">Họ và Tên<span class="red">*</span></label>
                                <input type="text" class="form-control form-control-lg" id="fullName" name="fullName" required value="<%=fullName%>">
                            </div>
                            
                            <div class="mb-3">
                                <label for="phone" class="form-label fw-semibold text-light">Số điện thoại <span id="error-phone" class="red small"></span></label>
                                <input type="text" class="form-control form-control-lg" id="phone" name="phone" value="<%=phone%>" placeholder="0xxxxxxxxx">
                            </div>
                            
                            <div class="mb-3">
                                <label for="dateOfBirth" class="form-label fw-semibold text-light">Ngày sinh</label>
                                <input type="date" class="form-control form-control-lg" id="dateOfBirth" name="dateOfBirth" value="<%=dateOfBirth%>">
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-semibold d-block text-light">Giới tính</label>
                                <div class="d-flex gap-4">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="gender" id="genderMale" value="male" <%="male".equals(gender) ? "checked" : ""%>>
                                        <label class="form-check-label text-light" for="genderMale">Nam</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="gender" id="genderFemale" value="female" <%="female".equals(gender) ? "checked" : ""%>>
                                        <label class="form-check-label text-light" for="genderFemale">Nữ</label>
                                    </div>
                                </div>
                            </div>

                            <% if(!msg.isEmpty()) { %>
                                <div class="alert alert-danger py-2 px-3 mb-3 small bg-danger bg-opacity-10 border-danger text-danger">
                                    <i class="bi bi-info-circle me-1"></i> <%=msg%>
                                </div>
                            <% } %>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold shadow-sm">
                                    <i class="bi bi-save me-2"></i>Lưu thay đổi
                                </button>
                                <a href="${pageContext.request.contextPath}/" class="btn btn-link text-secondary text-decoration-none text-center small">Cancel</a>
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
        const dob = document.getElementById('dateOfBirth');
        const today = new Date().toISOString().split('T')[0];
        if(dob) dob.setAttribute('max', today);

        const regexPhone = /^0\d{9}$/;
        const phone = document.getElementById('phone');
        const phone_error = document.getElementById('error-phone');
        if(phone) {
            phone.addEventListener('blur', function() {
                if (phone.value !== "" && !regexPhone.test(phone.value)) {
                    phone_error.innerText = " (Phải bao gồm 10 chữ số)";
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
                if (phone.value !== "" && !regexPhone.test(phone.value)) {
                    event.preventDefault();
                    phone.focus();
                }
            });
        }
    </script>
</body>
</html>