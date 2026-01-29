<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Verify</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/style.css">

</head>
<body class="d-flex flex-column min-vh-100">
	<jsp:include page="/WEB-INF/views/common/header.jsp">
		<jsp:param value="verify" name="pageName" />
	</jsp:include>

	<%
	Object obj = session.getAttribute("msg");
	String msg = (obj != null) ? obj.toString() : "";
	session.removeAttribute("msg");
	boolean isSuccess = msg.contains("thành công");

	Long otpTime = (Long) session.getAttribute("otpTime");
	long remainingSeconds = 0;
	if (otpTime != null) {
		long currentTime = System.currentTimeMillis();
		long diffInSeconds = (currentTime - otpTime) / 1000;
		remainingSeconds = 60 - diffInSeconds; // Lấy 60s trừ đi thời gian đã trôi qua

		if (remainingSeconds < 0)
			remainingSeconds = 0;
	}
	%>
	<main
		class="flex-grow-1 d-flex align-items-center justify-content-center">
		<div class="card bg-dark border-secondary p-4" style="width: 400px;">
			<h2 class="text-center text-warning">Xác Thực OTP</h2>
			<%
			if (!isSuccess) {
			%>
			<p class="text-center">
				<span style="color: white;">Mã có hiệu lực trong</span> <span
					id="timer" class="red"><%=remainingSeconds%></span> <span
					style="color: white;">giây.</span> 
					<a href="${pageContext.request.contextPath}/user-controller?controllerType=resend-otp"
					class="ms-2 text-warning text-decoration-none fw-bold"
					style="font-size: 0.9rem;"> Gửi lại mã? </a>
			</p>
			<form action="user-controller?controllerType=verify" method="post">
				<div class="mb-3">
					<input type="text" name="otpInput"
						class="form-control text-center fs-3" maxlength="6" required
						autofocus>
				</div>
				<div class="red text-center mb-2">
					<%=msg%>
				</div>
				<button type="submit" class="btn btn-primary w-100">Xác
					nhận</button>
			</form>
			<%
			} else {
			%>
			<div class="text-center">
				<div class="alert alert-success">
					<%=msg%>
				</div>
				<a href="${pageContext.request.contextPath}/user-controller?controllerType=login"
					class="btn btn-warning w-100">Quay về Trang Đăng Nhập</a>
			</div>
			<%
			}
			%>
		</div>
	</main>

	<script>
    let timeLeft = <%=remainingSeconds%>;
    const timerElement = document.getElementById('timer');
    const countdown = setInterval(() => {
        if (timeLeft > 0) timeLeft--;
        timerElement.innerText = timeLeft;
        if (timeLeft <= 0) {
            clearInterval(countdown);
            alert("Mã OTP đã hết hạn!");
        }
    }, 1000);
</script>

	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>