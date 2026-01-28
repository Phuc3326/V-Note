<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>

<%
    // Lấy tham số pageName từ jsp:param của trang cha
    String pageName = request.getParameter("pageName");
    // Lấy thông tin user từ session để hiển thị menu account
    Object obj = session.getAttribute("user");
    User user = (obj != null) ? (User) obj : null;
    
    // Kiểm tra trang hiện tại để áp dụng màu sắc (Active link logic)
    boolean isIndex = "index".equals(pageName);
    boolean isTrash = "trash".equals(pageName);
%>

<header class="p-3 text-bg-dark border-bottom border-secondary">
    <div class="container">
        <div class="d-flex align-items-center justify-content-between">
            
            <div class="d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/" 
                   class="text-white text-decoration-none fw-bold fs-4 pe-3 me-3 border-end border-secondary line-height-1">
                    V-Note
                </a>

                <ul class="nav mb-0">
                    <li>
                        <a href="${pageContext.request.contextPath}/note-controller?controllerType=index" 
                           class="nav-link px-2 <%= isIndex ? "text-secondary" : "text-white" %>">
                           Home
                        </a>
                    </li>
                    <% if (isIndex || isTrash) { %>
                    <li>
                        <a href="${pageContext.request.contextPath}/note-controller?controllerType=trash" 
                           class="nav-link px-2 <%= isTrash ? "text-secondary" : "text-white" %>">
                           Trash
                        </a>
                    </li>
                    <% } %>
                </ul>
            </div>

            <div class="d-flex align-items-center">
                <% if (isIndex) { %>
				<form action="note-controller" method="GET"
					class="d-flex align-items-center mb-3 mb-lg-0 me-lg-3"
					role="search">
					<input type="hidden" name="controllerType" value="search">

					<input type="search" name="keyword" value="${keyword}"
						class="form-control form-control-dark text-bg-dark border-secondary"
						placeholder="Search..." aria-label="Search">

					<button type="submit" class="btn btn-outline-light ms-2">Find</button>
				</form>
				<% } %>
                
                <% if (user == null) { %>
                    <% if (!"login".equals(pageName)) { %>
                    <a href="${pageContext.request.contextPath}/user-controller?controllerType=login" class="btn btn-warning">Login</a>
                    <% } %>
                <% } else { %>
                    <div class="dropdown">
                        <button class="btn btn-outline-light dropdown-toggle" type="button" id="userMenu" data-bs-toggle="dropdown">
                            Account
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow">
                            <li><h6 class="dropdown-header">Hi, <%=user.getFullName()%></h6></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/user-controller?controllerType=logout">Logout</a></li>
                        </ul>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</header>