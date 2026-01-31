<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>V-Note - My Notes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <style>
        .note-card {
            transition: transform 0.2s, box-shadow 0.2s;
            cursor: pointer;
            border-radius: 12px;
        }
        .note-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.3);
        }
        .fab-button {
            width: 65px;
            height: 65px;
            font-size: 32px;
            bottom: 80px; 
            right: 25px;
            z-index: 1050;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        /* Tối ưu textarea cho mobile */
        #modalContent {
            font-size: 16px;
            resize: none;
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100 bg-dark text-light">
    <jsp:include page="/WEB-INF/views/common/header.jsp">
        <jsp:param value="index" name="pageName" />
    </jsp:include>

    <main class="flex-grow-1">
        <div class="container mt-4 mb-5">
            <div class="row g-3" id="note-container"> <c:if test="${empty userNotes}">
                    <div class="col-12 text-center mt-5 py-5">
                        <c:choose>
                            <c:when test="${not empty keyword}">
                                <i class="bi bi-search text-secondary" style="font-size: 3.5rem;"></i>
                                <p class="text-secondary mt-3 fs-5">
                                    No notes found for "<strong>${keyword}</strong>".
                                </p>
                                <a href="${pageContext.request.contextPath}/note-controller?controllerType=index"
                                   class="btn btn-outline-warning mt-2">Back to all notes</a>
                            </c:when>

                            <c:otherwise>
                                <i class="bi bi-journal-plus text-secondary" style="font-size: 4rem;"></i>
                                <p class="text-secondary mt-3 fs-5">Your notebook is empty. Tap (+) to start!</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <c:forEach var="note" items="${userNotes}">
                    <div class="col-12 col-sm-6 col-lg-3 mb-2">
                        <div class="card bg-dark border-secondary note-card h-100 shadow-sm" 
                             onclick="openNote('${note.id}', `${note.title}`, `${note.content}`, '${note.createDate}', '${note.lastEditDate}')">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title text-warning fw-bold mb-2">${note.title}</h5>
                                <p class="card-text text-light opacity-75 small flex-grow-1">
                                    </p>
                                <div class="d-flex justify-content-between align-items-center mt-3">
                                    <span class="text-white-50 x-small">${note.createDate}</span>
                                    <button onclick="event.stopPropagation(); deleteNote('${note.id}')"
                                            class="btn btn-sm btn-outline-danger border-0">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <button class="btn btn-warning rounded-circle position-fixed shadow-lg fab-button"
                onclick="openNote('', '', '', '', '')">
            <i class="bi bi-plus-lg"></i>
        </button>
    </main>

    <div class="modal fade" id="noteModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg modal-fullscreen-sm-down">
            <div class="modal-content bg-dark text-light border-secondary">
                <div class="modal-header border-secondary">
                    <input type="text" id="modalTitle"
                           class="form-control bg-transparent text-warning border-0 fs-3 fw-bold"
                           placeholder="Title...">

                    <button type="button" class="btn btn-link text-warning p-2" onclick="handleSaveAndClose()">
                        <i class="bi bi-check-circle-fill fs-2"></i>
                    </button>
                </div>
                <div class="modal-body p-4">
                    <textarea id="modalContent" class="form-control bg-transparent text-light border-0" 
                              rows="12" placeholder="Start writing..."></textarea>
                </div>
                <div class="modal-footer border-secondary justify-content-start py-2">
                    <div class="small text-white-50 px-2">
                        <i class="bi bi-calendar3 me-1"></i> Created: <span id="modalCreateDate"></span> 
                        <span class="mx-2">|</span>
                        <i class="bi bi-pencil-square me-1"></i> Edited: <span id="modalEditDate"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    let currentNoteId = '';
    const noteModal = new bootstrap.Modal(document.getElementById('noteModal'));

    function openNote(id, title, content, createDate, editDate) {
        currentNoteId = id;
        document.getElementById('modalTitle').value = title;
        document.getElementById('modalContent').value = content;
        document.getElementById('modalCreateDate').innerText = createDate || 'Now';
        document.getElementById('modalEditDate').innerText = editDate || 'Now';
        noteModal.show();
    }

    function handleSaveAndClose() {
        const title = document.getElementById('modalTitle').value.trim();
        const content = document.getElementById('modalContent').value.trim();

        if (title === "" && content !== "") {
            alert("Please enter a title!");
            return;
        }
        
        if (title === "" && content === "") {
            noteModal.hide();
            return;
        }

        saveNote(currentNoteId, title, content);
    }

    function saveNote(id, title, content) {
        const data = new URLSearchParams();
        data.append('controllerType', 'save-note');
        data.append('id', id);
        data.append('title', title);
        data.append('content', content);

        fetch('note-controller', {
            method: 'POST',
            body: data
        })
        .then(res => {
            if(res.ok) {
                window.location.reload();
            } else {
                alert("Error saving note!");
            }
        })
        .catch(err => console.error("Network error:", err));
    }

    function deleteNote(id) {
        if (confirm("Move this note to trash?")) {
            const data = new URLSearchParams();
            data.append('controllerType', 'move-to-trash');
            data.append('id', id);

            fetch('note-controller', {
                method: 'POST',
                body: data
            }).then(res => {
                if (res.ok) window.location.reload();
                else alert("Could not delete note!");
            });
        }
    }
    </script>
    <jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>