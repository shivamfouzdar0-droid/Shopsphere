<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:if test="${empty msg}">
    <fmt:setBundle basename="messages" var="msg" scope="session" />
</c:if>

<nav class="navbar navbar-expand-lg navbar-dark sticky-top custom-navbar shadow-sm">
    <div class="container">
        <!-- Brand Logo -->
        <a class="navbar-brand d-flex align-items-center gap-2 fw-bold fs-4" href="${pageContext.request.contextPath}/home">
            <span class="brand-icon d-inline-flex align-items-center justify-content-center rounded-3 bg-gradient-primary">
                <i class="bi bi-shop text-white fs-5"></i>
            </span>
            <span class="brand-text">Shop<span class="text-accent">Sphere</span></span>
        </a>

        <!-- Mobile Toggler -->
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <!-- Navigation Links -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 fw-medium">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <fmt:message key="nav.home" bundle="${msg}"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">
                        <fmt:message key="nav.products" bundle="${msg}"/>
                    </a>
                </li>
                <!-- Categories Dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        <fmt:message key="nav.categories" bundle="${msg}"/>
                    </a>
                    <ul class="dropdown-menu shadow border-0 py-2">
                        <c:forEach var="cat" items="${applicationScope.globalCategories}">
                            <li>
                                <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/products?category=${cat.categoryId}">
                                    ${cat.name}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </li>
            </ul>

            <!-- Search Form -->
            <form class="d-flex mx-lg-3 my-2 my-lg-0 search-bar-wrapper flex-grow-1" action="${pageContext.request.contextPath}/products" method="get">
                <div class="input-group">
                    <input type="text" name="search" class="form-control bg-light border-0 search-input" 
                           placeholder="Search headphones, laptops, clothing..." value="${param.search}">
                    <button class="btn btn-primary px-3" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <!-- Actions & User State -->
            <ul class="navbar-nav align-items-center gap-1">
                <!-- Language Selector -->
                <li class="nav-item dropdown me-2">
                    <a class="nav-link dropdown-toggle text-light py-1 px-2 rounded bg-dark-subtle border border-secondary border-opacity-25 small" href="#" data-bs-toggle="dropdown">
                        <i class="bi bi-translate me-1"></i>
                        ${empty sessionScope.userLang || sessionScope.userLang == 'en' ? 'EN' : 'हिन्दी'}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow border-0">
                        <li><a class="dropdown-item ${sessionScope.userLang == 'en' ? 'active' : ''}" href="${pageContext.request.contextPath}/language?lang=en">English (EN)</a></li>
                        <li><a class="dropdown-item ${sessionScope.userLang == 'hi' ? 'active' : ''}" href="${pageContext.request.contextPath}/language?lang=hi">हिन्दी (Hindi)</a></li>
                    </ul>
                </li>

                <!-- Wishlist Icon -->
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item me-1">
                        <a class="nav-link position-relative p-2" href="${pageContext.request.contextPath}/wishlist" title="Wishlist">
                            <i class="bi bi-heart fs-5"></i>
                        </a>
                    </li>
                </c:if>

                <!-- Shopping Cart Icon with Badge -->
                <li class="nav-item me-2">
                    <a class="nav-link position-relative p-2" href="${pageContext.request.contextPath}/cart" title="Shopping Cart">
                        <i class="bi bi-cart3 fs-5"></i>
                        <span class="position-absolute top-1 start-100 translate-middle badge rounded-pill bg-danger border border-light" style="font-size: 0.65rem;">
                            ${sessionScope.cartCount != null ? sessionScope.cartCount : 0}
                        </span>
                    </a>
                </li>

                <!-- Authentication / Profile Dropdown -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center gap-2" href="#" role="button" data-bs-toggle="dropdown">
                                <span class="avatar-circle bg-primary text-white fw-bold">
                                    ${sessionScope.user.name.substring(0, 1).toUpperCase()}
                                </span>
                                <span class="d-none d-lg-inline text-light fw-semibold small">
                                    ${sessionScope.user.name.split(' ')[0]}
                                </span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow border-0 py-2">
                                <li class="px-3 py-2 border-bottom">
                                    <div class="fw-bold text-dark">${sessionScope.user.name}</div>
                                    <div class="text-muted small">${sessionScope.user.email}</div>
                                    <c:if test="${sessionScope.user.admin}">
                                        <span class="badge bg-danger mt-1">ADMINISTRATOR</span>
                                    </c:if>
                                </li>
                                <li>
                                    <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person me-2 text-primary"></i> <fmt:message key="nav.profile" bundle="${msg}"/>
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/orders">
                                        <i class="bi bi-box-seam me-2 text-primary"></i> <fmt:message key="nav.orders" bundle="${msg}"/>
                                    </a>
                                </li>
                                <c:if test="${sessionScope.user.admin}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item py-2 text-primary fw-semibold" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="bi bi-speedometer2 me-2"></i> <fmt:message key="nav.admin" bundle="${msg}"/>
                                        </a>
                                    </li>
                                </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item py-2 text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i> <fmt:message key="nav.logout" bundle="${msg}"/>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="btn btn-outline-light btn-sm rounded-pill px-3 me-2" href="${pageContext.request.contextPath}/login">
                                <fmt:message key="nav.login" bundle="${msg}"/>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-accent btn-sm rounded-pill px-3 text-white fw-semibold" href="${pageContext.request.contextPath}/register">
                                <fmt:message key="nav.register" bundle="${msg}"/>
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Global Toast Notification Alerts -->
<c:if test="${not empty sessionScope.toastSuccess}">
    <div class="alert alert-success alert-dismissible fade show m-0 rounded-0 border-0 text-center fw-medium py-2" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.toastSuccess}
        <button type="button" class="btn-close py-2" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="toastSuccess" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.toastError}">
    <div class="alert alert-danger alert-dismissible fade show m-0 rounded-0 border-0 text-center fw-medium py-2" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.toastError}
        <button type="button" class="btn-close py-2" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="toastError" scope="session" />
</c:if>
