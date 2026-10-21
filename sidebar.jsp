<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="col-md-3 col-lg-2 d-md-block bg-dark sidebar collapse min-vh-100 p-0 shadow-sm" id="adminSidebar">
    <div class="position-sticky pt-3">
        <div class="px-3 pb-3 border-bottom border-secondary border-opacity-25 mb-3">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="d-flex align-items-center gap-2 text-white text-decoration-none fw-bold fs-5">
                <i class="bi bi-shield-shaded text-accent fs-4"></i>
                <span>Admin<span class="text-accent">Portal</span></span>
            </a>
            <div class="text-secondary small mt-1">ShopSphere Control Panel</div>
        </div>

        <ul class="nav flex-column px-2 gap-1">
            <li class="nav-item">
                <a class="nav-link text-white-50 rounded px-3 py-2 ${param.active == 'dashboard' ? 'active bg-primary text-white' : 'text-hover-white'}" 
                   href="${pageContext.request.contextPath}/admin/dashboard">
                    <i class="bi bi-speedometer2 me-2"></i> Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white-50 rounded px-3 py-2 ${param.active == 'products' ? 'active bg-primary text-white' : 'text-hover-white'}" 
                   href="${pageContext.request.contextPath}/admin/products">
                    <i class="bi bi-box-seam me-2"></i> Products & Stock
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white-50 rounded px-3 py-2 ${param.active == 'categories' ? 'active bg-primary text-white' : 'text-hover-white'}" 
                   href="${pageContext.request.contextPath}/admin/categories">
                    <i class="bi bi-tags me-2"></i> Categories
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white-50 rounded px-3 py-2 ${param.active == 'orders' ? 'active bg-primary text-white' : 'text-hover-white'}" 
                   href="${pageContext.request.contextPath}/admin/orders">
                    <i class="bi bi-receipt me-2"></i> Orders & Tracking
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white-50 rounded px-3 py-2 ${param.active == 'users' ? 'active bg-primary text-white' : 'text-hover-white'}" 
                   href="${pageContext.request.contextPath}/admin/users">
                    <i class="bi bi-people me-2"></i> Customers
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white-50 rounded px-3 py-2 ${param.active == 'coupons' ? 'active bg-primary text-white' : 'text-hover-white'}" 
                   href="${pageContext.request.contextPath}/admin/coupons">
                    <i class="bi bi-percent me-2"></i> Discount Coupons
                </a>
            </li>
        </ul>

        <hr class="border-secondary border-opacity-25 mx-3 my-4">

        <ul class="nav flex-column px-2 gap-1">
            <li class="nav-item">
                <a class="nav-link text-white-50 rounded px-3 py-2 text-hover-white" href="${pageContext.request.contextPath}/home" target="_blank">
                    <i class="bi bi-arrow-up-right-square me-2"></i> View Storefront
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-danger rounded px-3 py-2" href="${pageContext.request.contextPath}/logout">
                    <i class="bi bi-box-arrow-right me-2"></i> Sign Out
                </a>
            </li>
        </ul>
    </div>
</div>
