<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Sign In" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-5 flex-grow-1 d-flex align-items-center justify-content-center">
    <div class="col-12 col-md-8 col-lg-5">
        <div class="card border-0 shadow-lg rounded-4 p-4 p-md-5 bg-white">
            <div class="text-center mb-4">
                <div class="brand-icon d-inline-flex align-items-center justify-content-center rounded-3 bg-gradient-primary mb-3">
                    <i class="bi bi-person-lock text-white fs-4"></i>
                </div>
                <h2 class="fw-bold text-dark">Welcome Back</h2>
                <p class="text-muted small">Sign in to your ShopSphere account</p>
            </div>

            <!-- Error Alerts -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show small py-2 mb-3" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i> ${errorMessage}
                    <button type="button" class="btn-close py-2" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert alert-success alert-dismissible fade show small py-2 mb-3" role="alert">
                    <i class="bi bi-check-circle-fill me-1"></i> ${successMessage}
                    <button type="button" class="btn-close py-2" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation" novalidate>
                <div class="mb-3">
                    <label for="email" class="form-label small fw-semibold text-muted">Email Address</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                        <input type="email" id="email" name="email" class="form-control border-start-0" 
                               value="${enteredEmail != null ? enteredEmail : ''}" placeholder="name@domain.com" required>
                    </div>
                </div>

                <div class="mb-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <label for="password" class="form-label small fw-semibold text-muted mb-0">Password</label>
                    </div>
                    <div class="input-group mt-1">
                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock text-muted"></i></span>
                        <input type="password" id="password" name="password" class="form-control border-start-0" 
                               placeholder="••••••••" required>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100 rounded-pill py-2 fw-bold shadow-sm">
                    Sign In
                </button>
            </form>

            <!-- Quick Demo Credentials Filler for College Evaluation / Viva -->
            <div class="mt-4 pt-3 border-top text-center">
                <span class="text-muted fw-bold small d-block mb-2 text-uppercase" style="font-size: 0.75rem; letter-spacing: 0.5px;">
                    RTU Evaluator Quick Login:
                </span>
                <div class="d-flex gap-2 justify-content-center">
                    <button type="button" class="btn btn-outline-secondary btn-sm rounded-pill" onclick="fillCustomer()">
                        Customer Demo
                    </button>
                    <button type="button" class="btn btn-outline-danger btn-sm rounded-pill" onclick="fillAdmin()">
                        Admin Demo
                    </button>
                </div>
            </div>

            <div class="mt-4 text-center">
                <span class="text-muted small">Don't have an account?</span>
                <a href="${pageContext.request.contextPath}/register" class="text-primary fw-semibold text-decoration-none small ms-1">
                    Sign Up
                </a>
            </div>
        </div>
    </div>
</main>

<script>
function fillCustomer() {
    document.getElementById('email').value = 'customer@shopsphere.com';
    document.getElementById('password').value = 'customer123';
}
function fillAdmin() {
    document.getElementById('email').value = 'admin@shopsphere.com';
    document.getElementById('password').value = 'admin123';
}
</script>

<jsp:include page="/fragments/footer.jsp" />
