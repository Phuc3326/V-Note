<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="d-flex flex-column min-vh-100">
	<jsp:include page="/WEB-INF/views/common/header.jsp">
		<jsp:param value="trash" name="pageName" />
	</jsp:include>

	<main class="flex-grow-1">
		<div class="container mt-4">
			<div class="row" id="note-container">

				<!-- Kiểm tra nếu danh sách RỖNG -->
				<c:if test="${empty userNotes}">
					<div class="col-12 text-center mt-5">
						<i class="bi bi-trash3 text-secondary" style="font-size: 4rem;"></i>
						<p class="text-secondary mt-3">Thùng rác trống rỗng!</p>
					</div>
				</c:if>

				<!-- Nếu KHÔNG RỖNG thì mới lặp qua để hiện Card -->
				<c:if test="${not empty userNotes}">
					<c:forEach var="note" items="${userNotes}">
						<div class="col-md-3 mb-3">
							<div class="card bg-dark border-secondary note-card">
								<div class="card-body">
									<h5 class="card-title text-secondary cursor-pointer"
										onclick="alert('Vui lòng khôi phục ghi chú này để có thể xem hoặc chỉnh sửa!')">
										${note.title}</h5>
									<p class="text-white-50 small">${note.createDate}</p>

									<div class="position-absolute bottom-0 end-0 m-2">
										<a href="javascript:void(0)"
											onclick="restoreNote('${note.id}')" class="text-success me-2">
											<i class="bi bi-arrow-counterclockwise"></i>
										</a> <a href="javascript:void(0)"
											onclick="hardDeleteNote('${note.id}')" class="text-danger">
											<i class="bi bi-x-circle"></i>
										</a>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>

			</div>
		</div>
	</main>

	<script>
	function restoreNote(id) {
	    const data = new URLSearchParams();
	    data.append('controllerType', 'restore-note'); // Hành động mới
	    data.append('id', id);

	    fetch('note-controller', { method: 'POST', body: data })
	    .then(res => { if(res.ok) window.location.reload(); });
	}

	function hardDeleteNote(id) {
	    if (confirm("CẢNH BÁO: Ghi chú này sẽ bị xóa VĨNH VIỄN và không thể khôi phục. Bạn chắc chắn chứ?")) {
	        const data = new URLSearchParams();
	        data.append('controllerType', 'hard-delete'); // Hành động xóa sạch khỏi DB
	        data.append('id', id);

	        fetch('note-controller', { method: 'POST', body: data })
	        .then(res => { if(res.ok) window.location.reload(); });
	    }
	}
	</script>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>