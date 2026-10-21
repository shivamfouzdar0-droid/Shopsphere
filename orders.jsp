<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Orders" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-4 flex-grow-1">
    <div class="mb-4 pb-2 border-bottom">
        <h2 class="fw-bold mb-1">Order History</h2>
        <p class="text-muted small mb-0">Track shipments, download receipts, and review past purchases</p>
    </div>

    <c:choose>
        <c:when test="${not empty orders}">
            <div class="d-flex flex-column gap-3">
                <c:forEach var="ord" items="${orders}">
                    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                        <div class="row align-items-center g-3">
                            <div class="col-md-3">
                                <span class="text-muted small d-block">Order Reference</span>
                                <span class="fw-bold fs-6 text-dark">#${ord.orderId}</span>
                                <span class="text-secondary small d-block">
                                    <fmt:formatDate value="${ord.createdAt}" pattern="dd MMM yyyy" />
                                </span>
                            </div>

                            <div class="col-md-3">
                                <span class="text-muted small d-block">Items Ordered</span>
                                <span class="fw-semibold text-dark">${ord.items.size()} item(s)</span>
                                <span class="text-muted small d-block">Method: ${ord.paymentMethod}</span>
                            </div>

                            <div class="col-md-3">
                                <span class="text-muted small d-block">Total Amount</span>
                                <span class="fw-bold fs-5 text-primary">₹${ord.finalPayable}</span>
                                <div class="mt-1">
                                    <c:choose>
                                        <c:when test="${ord.orderStatus == 'DELIVERED'}">
                                            <span class="badge bg-success rounded-pill px-2 py-1">Delivered</span>
                                        </c:when>
                                        <c:when test="${ord.orderStatus == 'SHIPPED'}">
                                            <span class="badge bg-primary rounded-pill px-2 py-1">Shipped</span>
                                        </c:when>
                                        <c:when test="${ord.orderStatus == 'CANCELLED'}">
                                            <span class="badge bg-danger rounded-pill px-2 py-1">Cancelled</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning text-dark rounded-pill px-2 py-1">Confirmed</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="col-md-3 text-md-end">
                                <a href="${pageContext.request.contextPath}/order-details?id=${ord.orderId}" class="btn btn-outline-primary btn-sm rounded-pill px-3 py-2 fw-semibold">
                                    View Details <i class="bi bi-chevron-right ms-1"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm rounded-4 p-5 text-center bg-white my-4">
                <div class="fs-1 text-muted mb-3"><i class="bi bi-box2"></i></div>
                <h4 class="fw-bold mb-2">No Past Orders Found</h4>
                <p class="text-muted mb-4">You have not placed any orders on ShopSphere yet.</p>
                <div>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2 fw-semibold">
                        Explore Catalog
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/fragments/footer.jsp" />
