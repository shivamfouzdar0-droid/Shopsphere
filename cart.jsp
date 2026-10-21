<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Shopping Bag" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-4 flex-grow-1">
    <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <h2 class="fw-bold mb-1">Your Shopping Cart</h2>
            <p class="text-muted small mb-0">Review items before placing your order</p>
        </div>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary btn-sm rounded-pill">
            <i class="bi bi-arrow-left me-1"></i> Continue Shopping
        </a>
    </div>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <div class="row g-4">
                <!-- Cart Items Table / List -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0">
                                <thead class="table-light text-muted small text-uppercase">
                                    <tr>
                                        <th style="width: 45%;">Product</th>
                                        <th class="text-center">Price</th>
                                        <th class="text-center" style="width: 20%;">Quantity</th>
                                        <th class="text-end pe-4">Subtotal</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center gap-3 p-2">
                                                    <img src="${item.product.imageUrl}" class="rounded-3 shadow-xs" 
                                                         style="width: 65px; height: 65px; object-fit: cover;" 
                                                         alt="${item.product.name}"
                                                         onerror="this.onerror=null;this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80';">
                                                    <div>
                                                        <span class="text-muted text-uppercase" style="font-size: 0.7rem; letter-spacing: 0.5px;">${item.product.brand}</span>
                                                        <h6 class="mb-0 fw-bold">
                                                            <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="text-dark text-decoration-none">
                                                                ${item.product.name}
                                                            </a>
                                                        </h6>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-center fw-semibold">₹${item.product.discountedPrice}</td>
                                            <td class="text-center">
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline-flex align-items-center gap-1">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    
                                                    <button type="button" class="btn btn-outline-secondary btn-sm py-0 px-2 rounded-circle" 
                                                            onclick="updateCartQty(this, -1)">-</button>
                                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}" 
                                                           class="form-control form-control-sm text-center fw-bold border-0 bg-light" style="width: 45px;"
                                                           onchange="this.form.submit()">
                                                    <button type="button" class="btn btn-outline-secondary btn-sm py-0 px-2 rounded-circle" 
                                                            onclick="updateCartQty(this, 1)">+</button>
                                                </form>
                                            </td>
                                            <td class="text-end pe-4 fw-bold text-primary">₹${item.subtotal}</td>
                                            <td class="pe-3">
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="m-0">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button type="submit" class="btn btn-link text-danger p-0" title="Remove item">
                                                        <i class="bi bi-trash3"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Order Summary Card -->
                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                        <h5 class="fw-bold mb-3">Order Summary</h5>
                        
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Subtotal</span>
                            <span class="fw-bold">₹${subtotal}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Estimated Delivery</span>
                            <span class="text-success fw-bold">FREE</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Taxes & Fees</span>
                            <span class="text-muted">Included</span>
                        </div>

                        <hr class="my-3">

                        <div class="d-flex justify-content-between align-items-baseline mb-4">
                            <span class="fs-5 fw-bold text-dark">Estimated Total</span>
                            <span class="fs-4 fw-bold text-primary">₹${subtotal}</span>
                        </div>

                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-lg rounded-pill w-100 fw-bold shadow-sm py-2">
                            Proceed to Checkout <i class="bi bi-arrow-right ms-1"></i>
                        </a>

                        <div class="text-center mt-3 text-muted small">
                            <i class="bi bi-shield-lock me-1"></i> Safe & Encrypted Checkout
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm rounded-4 p-5 text-center bg-white my-4">
                <div class="fs-1 text-muted mb-3"><i class="bi bi-bag-x"></i></div>
                <h3 class="fw-bold mb-2">Your Shopping Cart is Empty</h3>
                <p class="text-muted mb-4">Looks like you haven't added anything to your cart yet.</p>
                <div>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2 fw-semibold">
                        Start Shopping Now
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/fragments/footer.jsp" />
