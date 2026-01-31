<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>

<%
    String pageName = request.getParameter("pageName");
    Object obj = session.getAttribute("user");
    User user = (obj != null) ? (User) obj : null;
    
    boolean isIndex = "index".equals(pageName);
    boolean isTrash = "trash".equals(pageName);
%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark p-3 border-bottom border-secondary sticky-top">
    <div class="container">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand fw-bold fs-4">V-Note</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navRes" aria-controls="navRes" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navRes">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/note-controller?controllerType=index" 
                       class="nav-link <%= isIndex ? "active text-secondary" : "text-white" %>">Home</a>
                </li>
                <% if (isIndex || isTrash) { %>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/note-controller?controllerType=trash" 
                       class="nav-link <%= isTrash ? "active text-secondary" : "text-white" %>">Trash</a>
                </li>
                <% } %>
            </ul>

            <div class="d-flex flex-column flex-lg-row align-items-lg-center gap-2">
                <% if (isIndex) { %>
                <form action="note-controller" method="GET" class="d-flex align-items-center" role="search">
                    <input type="hidden" name="controllerType" value="search">
                    <input type="search" name="keyword" value="${keyword}"
                        class="form-control form-control-dark text-bg-dark border-secondary me-2"
                        placeholder="Search..." aria-label="Search">
                    <button type="submit" class="btn btn-outline-light text-nowrap">Find</button>
                </form>
                <% } %>
                
                <% if (user == null) { %>
                    <% if (!"login".equals(pageName)) { %>
                    <a href="${pageContext.request.contextPath}/user-controller?controllerType=login" class="btn btn-warning w-100 w-lg-auto">Login</a>
                    <% } %>
                <% } else { %>
                <div class="dropdown">
                    <button class="btn btn-outline-light dropdown-toggle w-100" type="button"
                        id="userMenu" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle me-1"></i> Account
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="userMenu">
                        <li><h6 class="dropdown-header">Hi, <%=user.getFullName()%></h6></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user-controller?controllerType=change-information"><i class="bi bi-person-gear me-2"></i>Edit Profile</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user-controller?controllerType=change-password"><i class="bi bi-shield-lock me-2"></i>Change Password</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/user-controller?controllerType=logout"><i class="bi bi-box-arrow-right me-2"></i>Logout</a></li>
                    </ul>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</nav>