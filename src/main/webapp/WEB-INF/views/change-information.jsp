<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="util.DateConverter"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Change Information</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
	integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.min.js"
	integrity="sha384-G/EV+4j2dNv+tEPo3++6LCgdCROaejBqfUeNjuKAiuXbjrxilcCdDz6ZAVfHWe1Y"
	crossorigin="anonymous"></script>
<style>
.red {
	color: red;
}

h1 {
	text-align: center;
}

#form-register {
	padding: 20px;
}
.form
</style>
</head>
<body>
	<%
		Object objSes = session.getAttribute("user");
		User user = (objSes != null) ? (User) objSes : null;
		if (user == null) {
	%>
		<h1>CHANGE INFORMATION</h1>
		<h4>You haven't logged in yet! Please log in first to change information.</h4>
	<%
		} else {
	%>
	
	<%
	Object obj = session.getAttribute("msg");
	String msg = (obj != null) ? obj.toString() : "";
	session.removeAttribute("msg");
	
	String fullName = user.getFullName();
	String dateOfBirth = (user.getDateOfBirth() != null) ? user.getDateOfBirth().toString() : "";
	String phone = user.getPhone();
	String email = user.getEmail();
	String gender = user.getGender();
	%>
	<div class="container">
		<h1>CHANGE INFORMATION</h1>
		<div id="form-register">
			<form action="${pageContext.request.contextPath}/user-controller?controllerType=change-information" method="post">
				<div class="form-group">
					<label for="fullName">Full name<span class="red">*</span></label> <input
						type="text" class="form-control" id="fullName" name="fullName"
						required value="<%=fullName%>">
				</div>
				<div class="form-group">
					<label for="email">Email address<span class="red">*</span></label>
					<input type="email" class="form-control" id="email" name="email"
						required value="<%=email%>">
				</div>
				<div class="form-group">
					<label for="phone">Phone<span id="error-phone"
						class="red small"></span></label> <input type="text" class="form-control"
						id="phone" name="phone" value="<%=phone%>">
				</div>
				<div class="form-group">
					<label for="dateOfBirth">Date of birth</label> <input type="date"
						class="form-control" id="dateOfBirth" name="dateOfBirth" value="<%=dateOfBirth%>">
				</div>

				<div class="form-group">
					<label>Gender</label>
					<div class="form-check">
						<input class="form-check-input" type="radio" name="gender"
							id="genderMale" value="male" <%= "male".equals(gender) ? "checked" : "" %>> <label
							class="form-check-label" for="genderMale">Male</label>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="radio" name="gender"
							id="genderFemale" value="female" <%= "female".equals(gender) ? "checked" : "" %>> <label
							class="form-check-label" for="genderFemale">Female</label>
					</div>
				</div>
				<div class="red"><%= msg %></div>
				<button type="submit" class="btn btn-primary">Save</button>
			</form>
		</div>
	</div>
	<%
		}
	%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
		crossorigin="anonymous"></script>
	<script>
		// 1. CHẶN NGÀY TƯƠNG LAI CHO DATE OF BIRTH
		const dob = document.getElementById('dateOfBirth');
		const today = new Date().toISOString().split('T')[0];
		dob.setAttribute('max', today);

		// 2. KIỂM TRA PHONE (NẾU NHẬP THÌ PHẢI ĐÚNG 10 SỐ)
		const regexPhone = /^0\d{9}$/;
		const phone = document.getElementById('phone');
		const phone_error = document.getElementById('error-phone');
		phone.addEventListener('blur', function () {
			if (phone.value !== "" && !regexPhone.test(phone.value)) {
				phone_error.innerText = " a number phone must contain 10 digits and start with number 0!";
				phone.classList.add('is-invalid');
			} else {
				phone_error.innerText = "";
				phone.classList.remove('is-invalid');
			}
		});

		// 3. CHẶN SUBMIT NẾU CÓ LỖI
		const form = document.querySelector('form');
		form.addEventListener('submit', function(event) {
			let hasError = false;
			if (phone.value !== "" && !regexPhone.test(phone.value)) {
				hasError = true;
			}
			
			if (hasError) {
				event.preventDefault();
			}
		})
	</script>
</body>
</html>