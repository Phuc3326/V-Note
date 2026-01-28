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
		<jsp:param value="index" name="pageName" />
	</jsp:include>

	<main class="flex-grow-1">
		<div class="container mt-4">
			<div class="row" id="note-container">

				<!-- 1. Trường hợp: Danh sách ghi chú RỖNG -->
				<c:if test="${empty userNotes}">
					<div class="col-12 text-center mt-5">
						<c:choose>
							<%-- Trường hợp: Đang tìm kiếm nhưng không thấy kết quả --%>
							<c:when test="${not empty keyword}">
								<i class="bi bi-search text-secondary" style="font-size: 4rem;"></i>
								<p class="text-secondary mt-3">
									Không tìm thấy ghi chú nào khớp với từ khóa "<strong>${keyword}</strong>".
								</p>
								<a
									href="${pageContext.request.contextPath}/note-controller?controllerType=index"
									class="btn btn-link text-warning">Quay lại danh sách chính</a>
							</c:when>

							<%-- Trường hợp: Trang chủ thực sự chưa có ghi chú nào --%>
							<c:otherwise>
								<i class="bi bi-sticky text-secondary" style="font-size: 4rem;"></i>
								<p class="text-secondary mt-3">Bạn chưa có ghi chú nào. Hãy
									nhấn nút (+) để tạo nhé!</p>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>

				<!-- 2. Trường hợp: CÓ ghi chú (Vòng lặp hiển thị Card) -->
				<c:forEach var="note" items="${userNotes}">
					<div class="col-md-3 mb-3">
						<div class="card bg-dark border-secondary note-card h-100">
							<div class="card-body">
								<h5 class="card-title text-warning cursor-pointer"
									onclick="openNote('${note.id}', `${note.title}`, `${note.content}`, '${note.createDate}', '${note.lastEditDate}')">
									${note.title}</h5>
								<p class="text-white-50 small">${note.createDate}</p>

								<a href="javascript:void(0)" onclick="deleteNote('${note.id}')"
									class="text-danger position-absolute bottom-0 end-0 m-2"> <i
									class="bi bi-trash"></i>
								</a>
							</div>
						</div>
					</div>
				</c:forEach>

			</div>
		</div>

		<button
			class="btn btn-warning rounded-circle position-fixed shadow-lg"
			style="width: 60px; height: 60px; font-size: 30px; bottom: 80px; right: 30px; z-index: 1050;"
			onclick="openNote('', '', '', '', '')">+</button>
	</main>

	<div class="modal fade" id="noteModal" tabindex="-1" aria-hidden="true"> <!-- class modal sẽ ẩn cửa sổ này đến khi dùng hàm show -->
	    <div class="modal-dialog modal-dialog-centered">
	        <div class="modal-content bg-dark text-light border-secondary">
				<div class="modal-header border-secondary">
					<input type="text" id="modalTitle"
						class="form-control bg-transparent text-warning border-0 fs-4"
						placeholder="Tiêu đề...">

					<button type="button"
						class="btn btn-link text-warning p-0 text-decoration-none"
						onclick="handleSaveAndClose()">
						<i class="bi bi-check-lg" style="font-size: 2rem;"></i>
					</button>
				</div>
				<div class="modal-body">
	                <textarea id="modalContent" class="form-control bg-transparent text-light border-0" rows="10" placeholder="Nội dung ghi chú..."></textarea>
	                <div class="mt-3 small text-white-50">
	                    Ngày tạo: <span id="modalCreateDate"></span> | Sửa lần cuối: <span id="modalEditDate"></span>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	
	<script>
	let currentNoteId = '';
	const noteModal = new bootstrap.Modal(document.getElementById('noteModal'));

	function openNote(id, title, content, createDate, editDate) {
	    currentNoteId = id;
	    document.getElementById('modalTitle').value = title;
	    document.getElementById('modalContent').value = content;
	    document.getElementById('modalCreateDate').innerText = createDate || 'Mới';
	    document.getElementById('modalEditDate').innerText = editDate || 'Mới';
	    noteModal.show(); // Hiện cửa sổ con
	}

	function handleSaveAndClose() {
	    const title = document.getElementById('modalTitle').value.trim();
	    const content = document.getElementById('modalContent').value.trim();

	    // Nếu có nội dung mà không có tiêu đề thì không cho đóng
	    if (title === "" && content !== "") {
	        alert("Vui lòng nhập tiêu đề!");
	        return;
	    }
	    
	    // Nếu trống hết thì ẩn không lưu
	    if (title === "" && content === "") {
	        noteModal.hide();
	        return;
	    }

	    // Gửi dữ liệu về Servlet bằng Fetch API (AJAX)
	    saveNote(currentNoteId, title, content);
	}

	function saveNote(id, title, content) {
	    // 1. Tạo một đối tượng chứa dữ liệu theo định dạng "key=value" giống như Form gửi đi
	    const data = new URLSearchParams();
	    data.append('controllerType', 'save-note');
	    data.append('id', id);
	    data.append('title', title);
	    data.append('content', content);

	    // 2. Sử dụng Fetch API để gửi yêu cầu HTTP POST
	    fetch('note-controller', {
	        method: 'POST',   // Phương thức gửi
	        body: data        // Dữ liệu đã đóng gói ở bước 1
	    })
	    // 3. Xử lý phản hồi từ Server
	    .then(res => { // res là response nhận được sau quá trình từ lúc servlet (note-controller) nhận được request đến lúc request được hoàn thành (chạy hết doPost/doGet).
	        if(res.ok) { // res.ok trả về true nếu trong quá trình trên không có lỗi.
	            window.location.reload(); // Nếu lưu thành công, tải lại trang để hiện ghi chú mới
	        } else {
	            alert("Có lỗi xảy ra khi lưu!");
	        }
	    })
	    .catch(err => console.error("Lỗi kết nối:", err)); // Catch lỗi xảy ra trong quá trình trình duyệt gửi request đến servlet (server), nếu mất mạng trong lúc gửi sẽ throw Exception.
	}
	function deleteNote(id) {
	    if (confirm("Bạn có chắc chắn muốn bỏ ghi chú này vào thùng rác?")) {
	        const data = new URLSearchParams();
	        data.append('controllerType', 'move-to-trash');
	        data.append('id', id);

	        fetch('note-controller', {
	            method: 'POST',
	            body: data
	        }).then(res => {
	            if (res.ok) window.location.reload();
	            else alert("Không thể xóa ghi chú!");
	        });
	    }
	}
	</script>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>