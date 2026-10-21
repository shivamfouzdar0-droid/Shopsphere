<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orders Management | ShopSphere Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/fragments/sidebar.jsp">
            <jsp:param name="active" value="orders" />
        </jsp:include>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between align-items-center pt-3 pb-2 mb-4 border-bottom">
                <div>
                    <h1 class="h3 fw-bold mb-0">Customer Orders</h1>
                    <small class="text-muted">Fulfill shipments, update dispatch status, and manage payments</small>
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
                                <th>Order #</th>
                                <th>Customer</th>
                                <th>Delivery City</th>
                                <th>Amount</th>
                                <th>Payment</th>
                                <th>Order Status</th>
                                <th class="text-end pe-4">Update Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ord" items="${orders}">
                                <tr>
                                    <td class="fw-bold">#${ord.orderId}</td>
                                    <td>
                                        <div class="fw-semibold text-dark">${ord.customerName}</div>
                                        <span class="text-muted small">${ord.customerEmail}</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty ord.shippingAddress}">
                                                <span class="small">${ord.shippingAddress.city}, ${ord.shippingAddress.state}</span>
                                            </c:when>
                                            <c:otherwise><span class="text-muted small">Standard Dispatch</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="fw-bold text-primary">₹${ord.finalPayable}</td>
                                    <td>
                                        <span class="badge ${ord.paymentStatus == 'PAID' ? 'bg-success' : 'bg-warning text-dark'} rounded-pill">
                                            ${ord.paymentMethod} : ${ord.paymentStatus}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${ord.orderStatus == 'DELIVERED' ? 'bg-success' : (ord.orderStatus == 'CANCELLED' ? 'bg-danger' : 'bg-primary')} rounded-pill">
                                            ${ord.orderStatus}
                                        </span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="d-inline-flex gap-1 align-items-center">
                                            <input type="hidden" name="action" value="updateOrderStatus">
                                            <input type="hidden" name="orderId" value="${ord.orderId}">
                                            <select name="orderStatus" class="form-select form-select-sm rounded-pill" style="width: 130px;" onchange="this.form.submit()">
                                                <option value="CONFIRMED" ${ord.orderStatus == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                                <option value="SHIPPED" ${ord.orderStatus == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                                <option value="DELIVERED" ${ord.orderStatus == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                                <option value="CANCELLED" ${ord.orderStatus == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                            </select>
                                        </form>
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
