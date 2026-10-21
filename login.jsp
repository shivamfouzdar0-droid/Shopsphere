<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Portal Login | ShopSphere</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-dark d-flex align-items-center justify-content-center min-vh-100">

<div class="col-11 col-sm-8 col-md-6 col-lg-4">
    <div class="card border-0 shadow-lg rounded-4 p-4 p-md-5 bg-white">
        <div class="text-center mb-4">
            <div class="brand-icon d-inline-flex align-items-center justify-content-center rounded-3 bg-dark mb-2">
                <i class="bi bi-shield-shaded text-accent fs-3"></i>
            </div>
            <h3 class="fw-bold text-dark">Staff & Admin Access</h3>
            <p class="text-muted small">Restricted portal for inventory and store administrators</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger small py-2 mb-3">
                <i class="bi bi-exclamation-triangle-fill me-1"></i> ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/login" method="post">
            <div class="mb-3">
                <label for="adminEmail" class="form-label small fw-semibold text-muted">Admin Email</label>
                <div class="input-group">
                    <span class="input-group-text bg-light"><i class="bi bi-person-badge text-muted"></i></span>
                    <input type="email" id="adminEmail" name="email" class="form-control" value="admin@shopsphere.com" required>
                </div>
            </div>

            <div class="mb-4">
                <label for="adminPassword" class="form-label small fw-semibold text-muted">Master Password</label>
                <div class="input-group">
                    <span class="input-group-text bg-light"><i class="bi bi-key text-muted"></i></span>
                    <input type="password" id="adminPassword" name="password" class="form-control" value="admin123" required>
                </div>
            </div>

            <button type="submit" class="btn btn-dark w-100 rounded-pill py-2 fw-bold shadow-sm mb-3">
                <i class="bi bi-box-arrow-in-right me-1"></i> Access Admin Dashboard
            </button>
        </form>

        <div class="text-center border-top pt-3">
            <a href="${pageContext.request.contextPath}/home" class="text-muted small text-decoration-none">
                <i class="bi bi-arrow-left me-1"></i> Return to Storefront
            </a>
        </div>
    </div>
</div>

</body>
</html>
