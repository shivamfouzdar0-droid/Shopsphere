<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order Confirmed" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-5 flex-grow-1">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card border-0 shadow-lg rounded-4 p-4 p-md-5 bg-white text-center">
                <div class="mx-auto mb-3 d-inline-flex align-items-center justify-content-center bg-success bg-opacity-10 text-success rounded-circle" style="width: 80px; height: 80px;">
                    <i class="bi bi-check-lg display-4"></i>
                </div>
                <span class="badge bg-success bg-opacity-10 text-success px-3 py-1 rounded-pill mx-auto mb-2 fw-semibold">
                    Payment & Allocation Confirmed
                </span>
                <h2 class="fw-bold text-dark mb-1">Thank You For Your Order!</h2>
                <p class="text-muted small mb-4">
                    Your order reference is <strong>#${order.orderId}</strong>. A simulated confirmation receipt has been issued.
                </p>

                <!-- Order Details Card -->
                <div class="text-start bg-light rounded-4 p-4 mb-4">
                    <div class="row g-3">
                        <div class="col-sm-6">
                            <span class="text-muted small d-block">Order Reference:</span>
                            <span class="fw-bold">#${order.orderId}</span>
                        </div>
                        <div class="col-sm-6">
                            <span class="text-muted small d-block">Order Date:</span>
                            <span class="fw-semibold"><fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, hh:mm a" /></span>
                        </div>
                        <div class="col-sm-6">
                            <span class="text-muted small d-block">Payment Method:</span>
                            <span class="badge bg-primary">${order.paymentMethod} (${order.paymentStatus})</span>
                        </div>
                        <div class="col-sm-6">
                            <span class="text-muted small d-block">Order Status:</span>
                            <span class="badge bg-info text-dark">${order.orderStatus}</span>
                        </div>
                        <c:if test="${not empty order.shippingAddress}">
                            <div class="col-12 border-top pt-2 mt-2">
                                <span class="text-muted small d-block">Delivery Address:</span>
                                <span class="fw-medium">${order.shippingAddress.formattedAddress}</span>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Purchased Items -->
                <div class="text-start mb-4">
                    <h6 class="fw-bold text-muted small text-uppercase mb-3">Items in this shipment</h6>
                    <div class="table-responsive">
                        <table class="table table-sm align-middle">
                            <tbody>
                                <c:forEach var="item" items="${order.items}">
                                    <tr>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <img src="${item.productImageUrl}" class="rounded-2" style="width: 40px; height: 40px; object-fit: cover;" alt="">
                                                <div>
                                                    <span class="fw-semibold text-dark">${item.productName}</span>
                                                    <div class="text-muted small">Qty: ${item.quantity} × ₹${item.price}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="text-end fw-bold">₹${item.itemTotal}</td>
                                    </tr>
                                </c:forEach>
                                <tr class="table-light">
                                    <td class="fw-bold">Total Paid:</td>
                                    <td class="text-end fw-bold fs-5 text-primary">₹${order.finalPayable}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="d-flex flex-wrap gap-2 justify-content-center">
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-primary rounded-pill px-4 py-2 fw-semibold">
                        View All Orders
                    </a>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2 fw-semibold">
                        Continue Shopping
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
