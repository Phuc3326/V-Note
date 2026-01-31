<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trash - V-Note</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <style>
        .trash-card {
            border-radius: 12px;
            opacity: 0.85;
            transition: all 0.3s ease;
        }
        .trash-card:hover {
            opacity: 1;
            transform: scale(1.02);
        }
        .action-btn {
            font-size: 1.2rem;
            padding: 5px 10px;
            border-radius: 8px;
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100 bg-dark text-light">
    <jsp:include page="/WEB-INF/views/common/header.jsp">
        <jsp:param value="trash" name="pageName" />
    </jsp:include>

    <main class="flex-grow-1">
        <div class="container mt-4 mb-5">
            <div class="d-flex align-items-center mb-4">
                <i class="bi bi-trash3 fs-2 text-secondary me-3"></i>
                <h1 class="h3 mb-0 fw-bold">Recycle Bin</h1>
            </div>

            <div class="row g-3" id="note-container">
                <c:if test="${empty userNotes}">
                    <div class="col-12 text-center mt-5 py-5">
                        <i class="bi bi-recycle text-secondary opacity-25" style="font-size: 5rem;"></i>
                        <p class="text-secondary mt-3 fs-5">Your trash is empty!</p>
                        <a href="${pageContext.request.contextPath}/note-controller?controllerType=index" 
                           class="btn btn-outline-secondary mt-2">Back to Home</a>
                    </div>
                </c:if>

                <c:forEach var="note" items="${userNotes}">
                    <div class="col-12 col-sm-6 col-lg-3">
                        <div class="card bg-dark border-secondary trash-card h-100 shadow-sm">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title text-secondary fw-bold mb-2 cursor-pointer"
                                    onclick="alert('Please restore this note to view or edit it!')">
                                    ${note.title}
                                </h5>
                                <p class="text-white-50 small mb-4">${note.createDate}</p>

                                <div class="mt-auto d-flex justify-content-end border-top border-secondary pt-2">
                                    <button onclick="restoreNote('${note.id}')" 
                                            class="btn btn-link text-success action-btn me-2" 
                                            title="Restore">
                                        <i class="bi bi-arrow-counterclockwise"></i>
                                    </button> 
                                    <button onclick="hardDeleteNote('${note.id}')" 
                                            class="btn btn-link text-danger action-btn" 
                                            title="Permanently Delete">
                                        <i class="bi bi-x-circle"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    function restoreNote(id) {
        // Hiệu ứng feedback nhanh cho người dùng
        const data = new URLSearchParams();
        data.append('controllerType', 'restore-note');
        data.append('id', id);

        fetch('note-controller', { method: 'POST', body: data })
        .then(res => { 
            if(res.ok) window.location.reload(); 
        });
    }

    function hardDeleteNote(id) {
        if (confirm("WARNING: This note will be PERMANENTLY deleted. This action cannot be undone!")) {
            const data = new URLSearchParams();
            data.append('controllerType', 'hard-delete');
            data.append('id', id);

            fetch('note-controller', { method: 'POST', body: data })
            .then(res => { 
                if(res.ok) window.location.reload(); 
            });
        }
    }
    </script>
    
    <jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>