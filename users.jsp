<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Management | ShopSphere Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/fragments/sidebar.jsp">
            <jsp:param name="active" value="users" />
        </jsp:include>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between align-items-center pt-3 pb-2 mb-4 border-bottom">
                <div>
                    <h1 class="h3 fw-bold mb-0">Registered Accounts</h1>
                    <small class="text-muted">Customer and administrative roles management</small>
                </div>
            </div>

            <c:if test="${not empty sessionScope.toastSuccess}">
                <div class="alert alert-success alert-dismissible fade show py-2 small" role="alert">
                    <i class="bi bi-check-circle-fill me-1"></i> ${sessionScope.toastSuccess}
                    <button type="button" class="btn-close py-2" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="toastSuccess" scope="session" />
            </c:if>

            <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light text-muted small text-uppercase">
                            <tr>
                                <th>User ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th class="text-end pe-4">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td class="fw-bold">#${u.userId}</td>
                                    <td class="fw-semibold text-dark">${u.name}</td>
                                    <td>${u.email}</td>
                                    <td class="text-muted small">${not empty u.mobile ? u.mobile : '-'}</td>
                                    <td>
                                        <span class="badge ${u.admin ? 'bg-danger' : 'bg-primary'} rounded-pill">
                                            ${u.role}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${u.status ? 'bg-success bg-opacity-10 text-success' : 'bg-secondary'} rounded-pill">
                                            ${u.status ? 'Active' : 'Deactivated'}
                                        </span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <c:if test="${!u.admin}">
                                            <form action="${pageContext.request.contextPath}/admin/users" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="toggleStatus">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <input type="hidden" name="status" value="${u.status ? '0' : '1'}">
                                                <button type="submit" class="btn btn-outline-${u.status ? 'danger' : 'success'} btn-sm rounded-pill px-3">
                                                    ${u.status ? 'Deactivate' : 'Activate'}
                                                </button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
