<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<footer class="mt-auto bg-dark text-light pt-5 pb-4 border-top border-secondary border-opacity-25">
    <div class="container">
        <div class="row g-4 mb-4">
            <!-- Brand & Mission -->
            <div class="col-lg-4 col-md-6">
                <div class="d-flex align-items-center gap-2 mb-3">
                    <span class="brand-icon d-inline-flex align-items-center justify-content-center rounded-3 bg-gradient-primary">
                        <i class="bi bi-shop text-white fs-5"></i>
                    </span>
                    <span class="fs-4 fw-bold text-white">Shop<span class="text-accent">Sphere</span></span>
                </div>
                <p class="text-secondary small leading-relaxed">
                    ShopSphere is an enterprise-grade E-Commerce platform built for the Rajasthan Technical University (RTU) Advanced Java curriculum. Demonstrates Jakarta EE 10, JSP EL/JSTL, Swing MVC, Java Sockets, and Java RMI.
                </p>
                <div class="d-flex gap-3 text-secondary fs-5 mt-3">
                    <a href="#" class="text-secondary text-hover-light"><i class="bi bi-github"></i></a>
                    <a href="#" class="text-secondary text-hover-light"><i class="bi bi-linkedin"></i></a>
                    <a href="#" class="text-secondary text-hover-light"><i class="bi bi-envelope"></i></a>
                </div>
            </div>

            <!-- Categories -->
            <div class="col-lg-2 col-md-6">
                <h6 class="text-uppercase fw-bold text-white mb-3" style="font-size: 0.85rem; letter-spacing: 1px;">Categories</h6>
                <ul class="list-unstyled text-secondary small d-flex flex-column gap-2">
                    <c:forEach var="cat" items="${applicationScope.globalCategories}">
                        <li><a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}" class="text-secondary text-decoration-none text-hover-light">${cat.name}</a></li>
                    </c:forEach>
                </ul>
            </div>

            <!-- Quick Links -->
            <div class="col-lg-3 col-md-6">
                <h6 class="text-uppercase fw-bold text-white mb-3" style="font-size: 0.85rem; letter-spacing: 1px;">Academic Modules</h6>
                <ul class="list-unstyled text-secondary small d-flex flex-column gap-2">
                    <li><a href="${pageContext.request.contextPath}/products" class="text-secondary text-decoration-none text-hover-light">Web Storefront (Servlet/JSP)</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/login" class="text-secondary text-decoration-none text-hover-light">Admin Management Portal</a></li>
                    <li><a href="${pageContext.request.contextPath}/jsp-demo.jsp" class="text-secondary text-decoration-none text-hover-light">RTU JSP & JSTL Demonstrations</a></li>
                    <li><span class="text-secondary opacity-75">Java Swing Desktop Administration</span></li>
                    <li><span class="text-secondary opacity-75">Java Socket Inventory Server</span></li>
                    <li><span class="text-secondary opacity-75">Java RMI Remote Service</span></li>
                </ul>
            </div>

            <!-- Technology Stack -->
            <div class="col-lg-3 col-md-6">
                <h6 class="text-uppercase fw-bold text-white mb-3" style="font-size: 0.85rem; letter-spacing: 1px;">Architecture Stack</h6>
                <div class="d-flex flex-wrap gap-2">
                    <span class="badge bg-secondary bg-opacity-25 text-light border border-secondary border-opacity-25">Java 17/25</span>
                    <span class="badge bg-secondary bg-opacity-25 text-light border border-secondary border-opacity-25">Jakarta EE 10</span>
                    <span class="badge bg-secondary bg-opacity-25 text-light border border-secondary border-opacity-25">Apache Tomcat 11</span>
                    <span class="badge bg-secondary bg-opacity-25 text-light border border-secondary border-opacity-25">MySQL InnoDB</span>
                    <span class="badge bg-secondary bg-opacity-25 text-light border border-secondary border-opacity-25">JSTL 3.0</span>
                    <span class="badge bg-secondary bg-opacity-25 text-light border border-secondary border-opacity-25">Java Swing</span>
                    <span class="badge bg-secondary bg-opacity-25 text-light border border-secondary border-opacity-25">RMI & Socket</span>
                </div>
            </div>
        </div>

        <hr class="border-secondary border-opacity-25 my-4">

        <div class="d-flex flex-column flex-md-row justify-content-between align-items-center text-secondary small">
            <div>
                &copy; 2026 <strong>ShopSphere</strong>. Developed for RTU Advanced Java Examination & Project Defense.
            </div>
            <div class="mt-2 mt-md-0">
                <span class="text-success"><i class="bi bi-shield-check me-1"></i> Database Protected (PreparedStatements & SHA-256)</span>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5 Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Custom JavaScript Application Logic -->
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
