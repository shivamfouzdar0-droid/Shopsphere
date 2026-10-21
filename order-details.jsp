<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order #${order.orderId}" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-4 flex-grow-1">
    <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <h2 class="fw-bold mb-1">Order Details #${order.orderId}</h2>
            <p class="text-muted small mb-0">Placed on <fmt:formatDate value="${order.createdAt}" pattern="dd MMMM yyyy, hh:mm a" /></p>
        </div>
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary btn-sm rounded-pill">
            <i class="bi bi-arrow-left me-1"></i> Back to Orders
        </a>
    </div>

    <!-- Tracking Status Bar -->
    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
        <h6 class="fw-bold mb-3">Order Status</h6>
        <div class="position-relative m-4">
            <div class="progress" style="height: 4px;">
                <c:choose>
                    <c:when test="${order.orderStatus == 'DELIVERED'}">
                        <div class="progress-bar bg-success" role="progressbar" style="width: 100%"></div>
                    </c:when>
                    <c:when test="${order.orderStatus == 'SHIPPED'}">
                        <div class="progress-bar bg-primary" role="progressbar" style="width: 66%"></div>
                    </c:when>
                    <c:when test="${order.orderStatus == 'CANCELLED'}">
                        <div class="progress-bar bg-danger" role="progressbar" style="width: 100%"></div>
                    </c:when>
                    <c:otherwise>
                        <div class="progress-bar bg-warning" role="progressbar" style="width: 33%"></div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="d-flex justify-content-between position-absolute top-0 start-0 w-100 translate-middle-y">
                <span class="badge rounded-pill bg-success">Placed</span>
                <span class="badge rounded-pill ${order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bg-primary' : 'bg-secondary'}">Shipped</span>
                <span class="badge rounded-pill ${order.orderStatus == 'DELIVERED' ? 'bg-success' : 'bg-secondary'}">Delivered</span>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <!-- Ordered Items Table -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                <div class="card-header bg-white py-3 border-bottom">
                    <h5 class="fw-bold mb-0">Products in Order (${order.items.size()})</h5>
                </div>
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light text-muted small text-uppercase">
                            <tr>
                                <th>Item</th>
                                <th class="text-center">Price</th>
                                <th class="text-center">Qty</th>
                                <th class="text-end pe-4">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3 p-2">
                                            <img src="${item.productImageUrl}" class="rounded-3 shadow-xs" 
                                                 style="width: 55px; height: 55px; object-fit: cover;" alt="">
                                            <div>
                                                <span class="text-muted text-uppercase" style="font-size: 0.7rem;">${item.productBrand}</span>
                                                <div class="fw-bold text-dark">${item.productName}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="text-center">₹${item.price}</td>
                                    <td class="text-center fw-semibold">${item.quantity}</td>
                                    <td class="text-end pe-4 fw-bold">₹${item.itemTotal}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Sidebar Summary -->
        <div class="col-lg-4">
            <!-- Delivery Info -->
            <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
                <h6 class="fw-bold mb-3">Shipping Address</h6>
                <c:choose>
                    <c:when test="${not empty order.shippingAddress}">
                        <span class="badge bg-secondary mb-2">${order.shippingAddress.addressType}</span>
                        <div class="fw-medium text-dark">${order.shippingAddress.addressLine}</div>
                        <div class="text-muted small">${order.shippingAddress.city}, ${order.shippingAddress.state} - ${order.shippingAddress.pincode}</div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted small mb-0">Standard Warehouse Dispatch</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Payment Summary -->
            <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                <h6 class="fw-bold mb-3">Financial Breakdown</h6>
                <div class="d-flex justify-content-between mb-2 small">
                    <span class="text-muted">Subtotal</span>
                    <span>₹${order.totalAmount}</span>
                </div>
                <c:if test="${order.discount > 0}">
                    <div class="d-flex justify-content-between mb-2 small text-success">
                        <span>Discount Applied</span>
                        <span>-₹${order.discount}</span>
                    </div>
                </c:if>
                <div class="d-flex justify-content-between mb-2 small">
                    <span class="text-muted">Shipping</span>
                    <span class="text-success fw-bold">FREE</span>
                </div>
                <hr class="my-2">
                <div class="d-flex justify-content-between align-items-baseline mb-3">
                    <span class="fw-bold">Total Charged</span>
                    <span class="fw-bold fs-5 text-primary">₹${order.finalPayable}</span>
                </div>
                <div class="bg-light p-2 rounded small text-center text-muted">
                    Payment Method: <strong>${order.paymentMethod}</strong> (${order.paymentStatus})
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
