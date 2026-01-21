<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
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
a {
	text-decoration-line: none;
}
</style>
</head>
<body>
	<%
		Object objSes = session.getAttribute("user");
		User user = (objSes != null) ? (User) objSes : null;
		if (user != null) {
	%>
		<h1>LOGIN</h1>
		<h4>You have logged in! Please log out first if you want to log in again.</h4>
	<%
		} else {
	%>
	
	<%
	Object obj = request.getAttribute("error");
	String error = (obj != null)? obj.toString() : "";
	String userName = request.getParameter("userName");
	userName = (userName != null)? userName : "";
	String password = request.getParameter("password");
	password = (password != null)? password : "";
	%>
	<div class="container">
		<main class="form-signin w-100 m-auto">
			<form action="login" method="POST">
				<h1 class="h3 mb-3 fw-normal">Please sign in</h1>
				<div class="form-floating">
					<input type="text" class="form-control" id="userName" name="userName"
						placeholder="User name" required value="<%=userName%>"> <label for="userName"> User Name </label>
				</div>
				<div class="form-floating">
					<input type="password" class="form-control" id="password" name="password"
						placeholder="Password" required value="<%=password%>"> <label for="password">Password</label>
				</div>
				<div class="red"> <%= error %></div>
				<button class="btn btn-primary w-100 py-2" type="submit">
					Sign in</button>
					
				<a href="sign-up.jsp">Do not have account yet? Sign-Up.</a>
				<p class="mt-5 mb-3 text-body-secondary">© 2017–2025</p>
			</form>
		</main>
	</div>
	<%
		}
	%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
		crossorigin="anonymous"></script>
</body>
</html>