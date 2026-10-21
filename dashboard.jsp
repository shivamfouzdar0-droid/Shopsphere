<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | ShopSphere Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <jsp:include page="/fragments/sidebar.jsp">
            <jsp:param name="active" value="dashboard" />
        </jsp:include>

        <!-- Main Content Area -->
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-4 border-bottom">
                <div>
                    <h1 class="h3 fw-bold mb-0">Store Performance Dashboard</h1>
                    <small class="text-muted">Real-time enterprise metrics & inventory overview</small>
                </div>
                <div class="btn-toolbar mb-2 mb-md-0 gap-2">
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-sm btn-primary rounded-pill">
                        <i class="bi bi-plus-lg me-1"></i> New Product
                    </a>
                </div>
            </div>

            <!-- Toast Messages -->
            <c:if test="${not empty sessionScope.toastSuccess}">
                <div class="alert alert-success alert-dismissible fade show py-2 small" role="alert">
                    <i class="bi bi-check-circle-fill me-1"></i> ${sessionScope.toastSuccess}
                    <button type="button" class="btn-close py-2" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="toastSuccess" scope="session" />
            </c:if>

            <!-- Metrics Overview Cards -->
            <div class="row g-3 mb-4">
                <div class="col-xl-3 col-sm-6">
                    <div class="card metric-card shadow-sm p-3 bg-white">
                        <div class="d-flex align-items-center gap-3">
                            <div class="metric-icon-box bg-primary bg-opacity-10 text-primary">
                                <i class="bi bi-currency-rupee"></i>
                            </div>
                            <div>
                                <span class="text-muted small d-block">Total Revenue</span>
                                <h4 class="fw-bold mb-0">₹${summary.totalRevenue}</h4>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-sm-6">
                    <div class="card metric-card shadow-sm p-3 bg-white">
                        <div class="d-flex align-items-center gap-3">
                            <div class="metric-icon-box bg-success bg-opacity-10 text-success">
                                <i class="bi bi-bag-check"></i>
                            </div>
                            <div>
                                <span class="text-muted small d-block">Completed Orders</span>
                                <h4 class="fw-bold mb-0">${summary.totalOrders}</h4>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-sm-6">
                    <div class="card metric-card shadow-sm p-3 bg-white">
                        <div class="d-flex align-items-center gap-3">
                            <div class="metric-icon-box bg-info bg-opacity-10 text-info">
                                <i class="bi bi-box-seam"></i>
                            </div>
                            <div>
                                <span class="text-muted small d-block">Active Catalog</span>
                                <h4 class="fw-bold mb-0">${summary.totalProducts}</h4>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-sm-6">
                    <div class="card metric-card shadow-sm p-3 bg-white">
                        <div class="d-flex align-items-center gap-3">
                            <div class="metric-icon-box bg-warning bg-opacity-10 text-warning">
                                <i class="bi bi-people"></i>
                            </div>
                            <div>
                                <span class="text-muted small d-block">Registered Customers</span>
                                <h4 class="fw-bold mb-0">${summary.totalCustomers}</h4>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-4">
                <!-- Recent Orders -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                        <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center border-bottom">
                            <h6 class="fw-bold mb-0">Recent Customer Orders</h6>
                            <a href="${pageContext.request.contextPath}/admin/orders" class="small text-primary text-decoration-none">
                                View All Orders <i class="bi bi-arrow-right"></i>
                            </a>
                        </div>
                        <div class="table-responsive">
                            <table class="table align-middle mb-0 small">
                                <thead class="table-light text-muted">
                                    <tr>
                                        <th>Order #</th>
                                        <th>Customer</th>
                                        <th>Items</th>
                                        <th>Total</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="ord" items="${recentOrders}">
                                        <tr>
                                            <td class="fw-bold text-dark">#${ord.orderId}</td>
                                            <td>
                                                <div class="fw-semibold">${ord.customerName}</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">${ord.customerEmail}</div>
                                            </td>
                                            <td>${ord.items.size()} item(s)</td>
                                            <td class="fw-bold text-primary">₹${ord.finalPayable}</td>
                                            <td>
                                                <span class="badge ${ord.orderStatus == 'DELIVERED' ? 'bg-success' : 'bg-primary'} rounded-pill">
                                                    ${ord.orderStatus}
                                                </span>
                                            </td>
                                            <td class="text-secondary"><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM, HH:mm" /></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Low Stock Inventory Alerts -->
                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm rounded-4 p-3 bg-white">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h6 class="fw-bold mb-0 text-danger">
                                <i class="bi bi-exclamation-triangle-fill me-1"></i> Low Stock Alerts
                            </h6>
                            <span class="badge bg-danger rounded-pill">${lowStockProducts.size()}</span>
                        </div>
                        <c:choose>
                            <c:when test="${not empty lowStockProducts}">
                                <div class="d-flex flex-column gap-2">
                                    <c:forEach var="lsp" items="${lowStockProducts}">
                                        <div class="p-2 border rounded-3 bg-light d-flex justify-content-between align-items-center small">
                                            <div class="text-truncate" style="max-width: 170px;">
                                                <div class="fw-semibold text-dark text-truncate">${lsp.name}</div>
                                                <span class="text-muted">${lsp.categoryName}</span>
                                            </div>
                                            <span class="badge bg-danger text-white rounded-pill px-2 py-1">
                                                ${lsp.stock} left
                                            </span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small mb-0">All products have healthy inventory levels.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
