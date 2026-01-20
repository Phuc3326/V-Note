<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Change password</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
a {
	text-decoration-line: none;
	color: white;
}
.red {
	color: red;
}
</style>
</head>
<body>
	<%
	Object obj = request.getAttribute("msg");
	String msg = (obj != null) ? obj.toString() : "";
	
	String newPassword = request.getParameter("newPassword");
	newPassword = (newPassword != null) ? newPassword : "";
	String passwordReEnter = request.getParameter("passwordReEnter");
	passwordReEnter = (passwordReEnter != null) ? passwordReEnter : "";
	%>
<div class="container">
		<h1>CHANGE PASSWORD</h1>
		<div id="form-register">
			<form action="change-password" method="post">
				<div class="form-group">
					<label for="password">Current Password<span class="red">*</span></label> <input
						type="password" class="form-control" id="password" name="password">
				</div>
				<div class="form-group">
					<label for="newPassword">New Password<span class="red">*</span></label> <input
						type="password" class="form-control" id="newPassword" name="newPassword"
						required minlength="8" value="<%=newPassword%>">
				</div>
				<div class="form-group">
					<label for="passwordReEnter">Re-Enter Password<span
						class="red">*</span><span id="error-password" class="red small"></span></label>
					<input type="password" class="form-control" id="passwordReEnter" name="passwordReEnter"
						required value="<%=passwordReEnter%>">
				</div>
				<div class="red"><%= msg %></div>
				<button type="submit" class="btn btn-primary">Save</button>
			</form>
		</div>
	</div>
	<script>
		// KIỂM TRA MẬT KHẨU KHI RỜI CHUỘT (BLUR)
		const pwdReEnter = document.getElementById('passwordReEnter');
		const pwd = document.getElementById('newPassword');
		const pwd_error = document.getElementById('error-password');
		pwdReEnter.addEventListener('blur', function() {
			if (pwdReEnter.value !== pwd.value && pwdReEnter.value !== "") {
				pwd_error.innerText = " the re-ender password does not match the password!";
				pwdReEnter.classList.add('is-invalid');
			} else {
				pwd_error.innerText = "";
				pwdReEnter.classList.remove('is-invalid');
			}
		});

		// CHẶN SUBMIT NẾU CÓ LỖI
		const form = document.querySelector('form');
		form.addEventListener('submit', function(event) {
			let hasError = false;
			if (pwdReEnter.value !== pwd.value) {
				hasError = true;
			}
			if (hasError) {
				event.preventDefault();
			}
		})
	</script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>