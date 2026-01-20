<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>V-Note</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
a {
	text-decoration-line: none;
	color: white;
}
.form-control-dark {
  border-color: var(--bs-gray);
}
.form-control-dark:focus {
  border-color: #fff;
  box-shadow: 0 0 0 .25rem rgba(255, 255, 255, .25);
}

.text-small {
  font-size: 85%;
}

.dropdown-toggle:not(:focus) {
  outline: 0;
}

</style>
</head>
<body>
	<header class="p-3 text-bg-dark">
		<div class="container">
			<div
				class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
				<a href="/"
					class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
					<svg class="bi me-2" width="40" height="32" role="img"
						aria-label="Bootstrap">
                <use xlink:href="#bootstrap"></use>
              </svg>
				</a>
				<ul
					class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
					<li><a href="index.jsp" class="nav-link px-2 text-secondary">Home</a></li>
					<li><a href="trash.jsp" class="nav-link px-2 text-white">Trash</a></li>
				</ul>
				<form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3" role="search">
					<input type="search"
						class="form-control form-control-dark text-bg-dark"
						placeholder="Search..." aria-label="Search">
				</form>
				<div class="text-end">
					<button type="button" class="btn btn-outline-light me-2">
						Find</button>
					<%
					Object obj = session.getAttribute("user");
					User user = (obj != null) ? (User) obj : null;
					if (user == null) {
					%>
					<button type="button" class="btn btn-warning">
						<a href="login.jsp">Login</a>
					</button>
					<%
					} else {
					%>
					<div class="dropdown d-inline-block">
						<button class="btn btn-outline-light dropdown-toggle"
							type="button" id="userMenu" data-bs-toggle="dropdown"
							aria-expanded="false">Account</button>

						<ul class="dropdown-menu dropdown-menu-end"
							aria-labelledby="userMenu">
							<li><h6 class="dropdown-header">
									Hi,
									<%=user.getFullName()%></h6></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item" href="edit-profile.jsp">Edit
									Profile</a></li>
							<li><a class="dropdown-item" href="change-password.jsp">Change
									Password</a></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item text-danger" href="logout">Logout</a></li>
						</ul>
					</div>
					<%
					}
					%>
				</div>
			</div>
		</div>
	</header>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>