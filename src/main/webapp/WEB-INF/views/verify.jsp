<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify OTP - V-Note</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        .verify-card {
            border-radius: 1.5rem;
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
            background-color: #212529;
            border: 1px solid #495057;
        }
        .otp-input {
            letter-spacing: 0.5rem;
            font-weight: bold;
            background-color: #2b3035 !important;
            color: #ffc107 !important;
            border: 2px solid #495057;
        }
        .otp-input:focus {
            border-color: #ffc107;
            box-shadow: 0 0 0 0.25rem rgba(255, 193, 7, 0.25);
            background-color: #32383e !important;
        }
        #timer { font-family: 'Courier New', Courier, monospace; }
        .text-white-50 { color: rgba(255, 255, 255, 0.5) !important; }
    </style>
</head>
<body class="d-flex flex-column min-vh-100 bg-dark text-light">
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
        remainingSeconds = 60 - diffInSeconds;
        if (remainingSeconds < 0) remainingSeconds = 0;
    }
    %>

    <main class="flex-grow-1 d-flex align-items-center">
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-11 col-sm-8 col-md-6 col-lg-4">
                    <div class="card verify-card p-4 p-md-5">
                        <div class="text-center mb-4">
                            <h2 class="fw-bold text-warning uppercase">XÁC THỰC OTP</h2>
                        </div>

                        <% if (!isSuccess) { %>
                        <p class="text-center small mb-4">
                            <span class="text-white-50">Mã đã được gửi qua Email! Có hiệu lực trong</span> 
                            <span id="timer" class="badge bg-danger fs-6 mx-1"><%=remainingSeconds%></span> 
                            <span class="text-white-50">giây.</span>
                        </p>

                        <form action="user-controller?controllerType=verify" method="post">
                            <div class="mb-4">
                                <input type="text" name="otpInput" 
                                    class="form-control form-control-lg text-center fs-2 otp-input" 
                                    maxlength="6" inputmode="numeric" pattern="[0-9]*" 
                                    required autofocus placeholder="••••••">
                            </div>

                            <% if(!msg.isEmpty()) { %>
                                <div class="text-danger text-center small mb-3">
                                    <i class="bi bi-exclamation-circle me-1"></i> <%=msg%>
                                </div>
                            <% } %>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold shadow-sm py-3">
                                    XÁC NHẬN
                                </button>
                                <a href="${pageContext.request.contextPath}/user-controller?controllerType=resend-otp"
                                   class="btn btn-link text-warning text-decoration-none small mt-2">
                                    Gửi lại mã?
                                </a>
                            </div>
                        </form>
                        <% } else { %>
                        <div class="text-center">
                            <div class="alert alert-success border-0 shadow-sm py-3 mb-4 bg-success bg-opacity-10 text-success">
                                <i class="bi bi-check-circle-fill me-2"></i> <%=msg%>
                            </div>
                            <div class="d-grid">
                                <a href="${pageContext.request.contextPath}/user-controller?controllerType=login"
                                   class="btn btn-warning btn-lg fw-bold">QUAY VỀ ĐĂNG NHẬP</a>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script>
    let timeLeft = <%=remainingSeconds%>;
    const timerElement = document.getElementById('timer');
    if (timerElement) {
        const countdown = setInterval(() => {
            if (timeLeft > 0) {
                timeLeft--;
                timerElement.innerText = timeLeft;
            } else {
                clearInterval(countdown);
                timerElement.classList.replace('bg-danger', 'bg-secondary');
            }
        }, 1000);
    }
    </script>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>