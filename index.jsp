<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="Home" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-4 flex-grow-1">
    <!-- Hero Banner -->
    <section class="hero-banner p-4 p-md-5 mb-5 shadow-lg">
        <div class="row align-items-center">
            <div class="col-lg-7 position-relative" style="z-index: 1;">
                <span class="badge bg-primary bg-opacity-75 text-white px-3 py-2 rounded-pill fw-semibold mb-3">
                    <i class="bi bi-stars me-1"></i> Spring 2026 Collection
                </span>
                <h1 class="display-5 fw-extrabold mb-3 text-white tracking-tight">
                    Curated Quality for Modern Living.
                </h1>
                <p class="lead text-light opacity-90 mb-4 fs-6 leading-relaxed">
                    Explore high-performance electronics, contemporary apparel, and home essentials. Built with enterprise Java, high availability, and real-time inventory management.
                </p>
                <div class="d-flex flex-wrap gap-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-accent btn-lg rounded-pill px-4 py-2 text-white fw-bold shadow">
                        Shop Catalog <i class="bi bi-arrow-right ms-2"></i>
                    </a>
                    <a href="${pageContext.request.contextPath}/jsp-demo.jsp" class="btn btn-outline-light btn-lg rounded-pill px-4 py-2 fw-semibold">
                        <i class="bi bi-code-slash me-1"></i> RTU Syllabus Demo
                    </a>
                </div>
            </div>
            <div class="col-lg-5 text-center d-none d-lg-block position-relative" style="z-index: 1;">
                <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80" 
                     class="img-fluid rounded-4 shadow-2-strong" style="max-height: 280px; object-fit: cover;" alt="Hero Product">
            </div>
        </div>
    </section>

    <!-- Value Propositions -->
    <section class="row g-3 mb-5 text-center">
        <div class="col-md-4">
            <div class="card p-3 border-0 shadow-sm h-100 rounded-4">
                <div class="d-flex align-items-center gap-3">
                    <div class="bg-primary bg-opacity-10 text-primary p-3 rounded-circle fs-4">
                        <i class="bi bi-truck"></i>
                    </div>
                    <div class="text-start">
                        <h6 class="fw-bold mb-0">Free & Fast Delivery</h6>
                        <small class="text-muted">On all domestic orders over ₹500</small>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3 border-0 shadow-sm h-100 rounded-4">
                <div class="d-flex align-items-center gap-3">
                    <div class="bg-success bg-opacity-10 text-success p-3 rounded-circle fs-4">
                        <i class="bi bi-shield-check"></i>
                    </div>
                    <div class="text-start">
                        <h6 class="fw-bold mb-0">Secure Mock Checkout</h6>
                        <small class="text-muted">PCI-DSS compliant architecture simulation</small>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3 border-0 shadow-sm h-100 rounded-4">
                <div class="d-flex align-items-center gap-3">
                    <div class="bg-warning bg-opacity-10 text-warning p-3 rounded-circle fs-4">
                        <i class="bi bi-arrow-repeat"></i>
                    </div>
                    <div class="text-start">
                        <h6 class="fw-bold mb-0">30-Day Hassle-Free Returns</h6>
                        <small class="text-muted">Dedicated 24/7 student support desk</small>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Popular Categories -->
    <section class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <div>
                <h3 class="fw-bold mb-0">Shop by Category</h3>
                <small class="text-muted">Browse items curated by department</small>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="text-primary text-decoration-none fw-semibold small">
                View All <i class="bi bi-chevron-right"></i>
            </a>
        </div>
        <div class="row row-cols-2 row-cols-md-4 g-3">
            <c:forEach var="cat" items="${categories}">
                <div class="col">
                    <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}" class="text-decoration-none">
                        <div class="category-card p-3 bg-white text-center shadow-sm">
                            <div class="fs-2 text-primary mb-2">
                                <c:choose>
                                    <c:when test="${cat.name.contains('Electronics')}"><i class="bi bi-laptop"></i></c:when>
                                    <c:when test="${cat.name.contains('Fashion')}"><i class="bi bi-handbag"></i></c:when>
                                    <c:when test="${cat.name.contains('Home')}"><i class="bi bi-cup-hot"></i></c:when>
                                    <c:otherwise><i class="bi bi-book"></i></c:otherwise>
                                </c:choose>
                            </div>
                            <h6 class="fw-bold text-dark mb-1">${cat.name}</h6>
                            <small class="text-muted text-truncate d-block">${cat.description}</small>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </section>

    <!-- Featured Products -->
    <section class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-0">Trending & Featured</h3>
                <small class="text-muted">Top discounts and bestsellers available right now</small>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-sm rounded-pill px-3">
                See More Products
            </a>
        </div>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
            <c:forEach var="prod" items="${featuredProducts}">
                <tag:productCard product="${prod}" />
            </c:forEach>
        </div>
    </section>
</main>

<jsp:include page="/fragments/footer.jsp" />
