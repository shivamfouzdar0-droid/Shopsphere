<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Checkout" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-4 flex-grow-1">
    <div class="mb-4 pb-2 border-bottom">
        <h2 class="fw-bold mb-1">Checkout & Payment</h2>
        <p class="text-muted small mb-0">Select shipping address and simulated payment method</p>
    </div>

    <!-- Error Alerts -->
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show mb-4 rounded-3" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session" />
    </c:if>

    <div class="row g-4">
        <!-- Main Form -->
        <div class="col-lg-8">
            <form id="checkoutForm" action="${pageContext.request.contextPath}/checkout" method="post">
                <!-- 1. Delivery Address -->
                <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
                    <h5 class="fw-bold mb-3 d-flex align-items-center gap-2">
                        <span class="badge bg-primary rounded-circle">1</span>
                        <span>Shipping Address</span>
                    </h5>

                    <c:choose>
                        <c:when test="${not empty addresses}">
                            <div class="d-flex flex-column gap-2 mb-3">
                                <c:forEach var="addr" items="${addresses}" varStatus="loop">
                                    <label class="border rounded-3 p-3 d-flex align-items-start gap-3 cursor-pointer bg-light">
                                        <input type="radio" name="addressId" value="${addr.addressId}" class="form-check-input mt-1" 
                                               <c:if test="${loop.first}">checked</c:if> onchange="toggleNewAddress(false)">
                                        <div>
                                            <span class="badge bg-secondary mb-1">${addr.addressType}</span>
                                            <div class="fw-semibold text-dark">${addr.addressLine}</div>
                                            <div class="text-muted small">${addr.city}, ${addr.state} - ${addr.pincode}</div>
                                        </div>
                                    </label>
                                </c:forEach>
                                <label class="border rounded-3 p-3 d-flex align-items-center gap-3 cursor-pointer bg-light">
                                    <input type="radio" name="addressId" value="new" class="form-check-input" onchange="toggleNewAddress(true)">
                                    <span class="fw-bold text-primary">+ Add New Delivery Address</span>
                                </label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="addressId" value="new">
                        </c:otherwise>
                    </c:choose>

                    <!-- New Address Form (shown if selected or no addresses) -->
                    <div id="newAddressForm" class="${not empty addresses ? 'd-none' : ''}">
                        <h6 class="fw-bold text-muted small text-uppercase mb-3">Enter Delivery Address</h6>
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Street / Flat Address</label>
                            <input type="text" name="addressLine" class="form-control" placeholder="House/Flat No., Apartment, Street name">
                        </div>
                        <div class="row g-2 mb-3">
                            <div class="col-md-5">
                                <label class="form-label small fw-semibold">City</label>
                                <input type="text" name="city" class="form-control" placeholder="Jaipur">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-semibold">State</label>
                                <input type="text" name="state" class="form-control" placeholder="Rajasthan">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">PIN Code</label>
                                <input type="text" name="pincode" class="form-control" placeholder="302001">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 2. Payment Method -->
                <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
                    <h5 class="fw-bold mb-3 d-flex align-items-center gap-2">
                        <span class="badge bg-primary rounded-circle">2</span>
                        <span>Payment Method</span>
                    </h5>

                    <div class="d-flex flex-column gap-2">
                        <label class="border rounded-3 p-3 d-flex align-items-center justify-content-between cursor-pointer bg-light">
                            <div class="d-flex align-items-center gap-3">
                                <input type="radio" name="paymentMethod" value="ONLINE" class="form-check-input" checked>
                                <div>
                                    <div class="fw-bold text-dark">Simulated Online Payment / Card</div>
                                    <small class="text-muted">Instant mock processing for student evaluation</small>
                                </div>
                            </div>
                            <i class="bi bi-credit-card-2-front fs-4 text-primary"></i>
                        </label>

                        <label class="border rounded-3 p-3 d-flex align-items-center justify-content-between cursor-pointer bg-light">
                            <div class="d-flex align-items-center gap-3">
                                <input type="radio" name="paymentMethod" value="COD" class="form-check-input">
                                <div>
                                    <div class="fw-bold text-dark">Cash on Delivery (COD)</div>
                                    <small class="text-muted">Pay at doorstep upon package arrival</small>
                                </div>
                            </div>
                            <i class="bi bi-cash-stack fs-4 text-success"></i>
                        </label>
                    </div>
                </div>

                <!-- Hidden coupon code forwarded to placeOrder -->
                <input type="hidden" name="couponCode" value="${appliedCoupon != null ? appliedCoupon.code : ''}">

                <button type="submit" class="btn btn-primary btn-lg rounded-pill w-100 py-3 fw-bold shadow">
                    <i class="bi bi-lock-fill me-2"></i> Confirm and Place Order (₹${finalPayable})
                </button>
            </form>
        </div>

        <!-- Sidebar Order & Coupon Summary -->
        <div class="col-lg-4">
            <!-- Coupon Code Box -->
            <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
                <h6 class="fw-bold mb-3">Promotional Discount</h6>
                <form action="${pageContext.request.contextPath}/checkout" method="get">
                    <div class="input-group">
                        <input type="text" name="couponCode" class="form-control text-uppercase" 
                               placeholder="e.g. WELCOME10" value="${appliedCoupon != null ? appliedCoupon.code : param.couponCode}">
                        <button type="submit" class="btn btn-outline-primary fw-bold">Apply</button>
                    </div>
                </form>

                <c:if test="${not empty couponSuccess}">
                    <div class="text-success small fw-semibold mt-2">
                        <i class="bi bi-check-circle-fill me-1"></i> ${couponSuccess}
                    </div>
                </c:if>
                <c:if test="${not empty couponError}">
                    <div class="text-danger small fw-semibold mt-2">
                        <i class="bi bi-x-circle-fill me-1"></i> ${couponError}
                    </div>
                </c:if>

                <div class="mt-2 text-muted small">
                    Try demo coupon: <span class="badge bg-light text-dark border">WELCOME10</span> or <span class="badge bg-light text-dark border">FESTIVE20</span>
                </div>
            </div>

            <!-- Order Items Breakdown -->
            <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                <h6 class="fw-bold mb-3">Order Items (${cartItems.size()})</h6>
                <div class="d-flex flex-column gap-3 mb-4">
                    <c:forEach var="item" items="${cartItems}">
                        <div class="d-flex align-items-center justify-content-between small">
                            <div class="d-flex align-items-center gap-2">
                                <img src="${item.product.imageUrl}" class="rounded-2" style="width: 40px; height: 40px; object-fit: cover;" alt="">
                                <div>
                                    <div class="fw-bold text-dark text-truncate" style="max-width: 170px;">${item.product.name}</div>
                                    <span class="text-muted">Qty: ${item.quantity}</span>
                                </div>
                            </div>
                            <span class="fw-bold">₹${item.subtotal}</span>
                        </div>
                    </c:forEach>
                </div>

                <div class="border-top pt-3">
                    <div class="d-flex justify-content-between mb-2 small">
                        <span class="text-muted">Subtotal</span>
                        <span>₹${subtotal}</span>
                    </div>
                    <c:if test="${discount > 0}">
                        <div class="d-flex justify-content-between mb-2 small text-success">
                            <span>Coupon Discount</span>
                            <span>-₹${discount}</span>
                        </div>
                    </c:if>
                    <div class="d-flex justify-content-between mb-2 small">
                        <span class="text-muted">Shipping</span>
                        <span class="text-success fw-bold">FREE</span>
                    </div>

                    <hr class="my-3">

                    <div class="d-flex justify-content-between align-items-baseline">
                        <span class="fw-bold fs-5">Final Payable</span>
                        <span class="fw-bold fs-4 text-primary">₹${finalPayable}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
function toggleNewAddress(show) {
    const el = document.getElementById('newAddressForm');
    if (el) {
        if (show) {
            el.classList.remove('d-none');
        } else {
            el.classList.add('d-none');
        }
    }
}
</script>

<jsp:include page="/fragments/footer.jsp" />
