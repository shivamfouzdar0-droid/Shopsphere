<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:if test="${empty sessionScope.userLocale}">
    <fmt:setLocale value="en" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.userLocale}">
    <fmt:setLocale value="${sessionScope.userLocale}" scope="session" />
</c:if>
<fmt:setBundle basename="messages" var="msg" scope="session" />

<!DOCTYPE html>
<html lang="${not empty sessionScope.userLang ? sessionScope.userLang : 'en'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle != null ? pageTitle : 'ShopSphere'} | Curated E-Commerce</title>
    <meta name="description" content="ShopSphere - RTU Advanced Java Enterprise E-Commerce Platform with modern retail storefront and desktop management.">

    <!-- Google Fonts: Inter -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- ShopSphere Custom Design System -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="d-flex flex-column min-vh-100 bg-light">
