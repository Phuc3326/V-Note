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

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/style.css">

</head>
<body class="d-flex flex-column min-vh-100">
	<jsp:include page="/WEB-INF/views/common/header.jsp">
		<jsp:param value="index" name="pageName"/>
	</jsp:include>
	
	<main class="flex-grow-1">
        <div class="container mt-4"></div>
    </main>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>