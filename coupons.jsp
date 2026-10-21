<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coupons Management | ShopSphere Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/fragments/sidebar.jsp">
            <jsp:param name="active" value="coupons" />
        </jsp:include>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between align-items-center pt-3 pb-2 mb-4 border-bottom">
                <div>
                    <h1 class="h3 fw-bold mb-0">Discount & Promotional Coupons</h1>
                    <small class="text-muted">Create campaigns, set percentage/fixed reductions and expiry</small>
                </div>
                <button class="btn btn-primary rounded-pill" data-bs-toggle="modal" data-bs-target="#addCouponModal">
                    <i class="bi bi-plus-lg me-1"></i> Create Coupon
                </button>
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
                                <th>Code</th>
                                <th>Type</th>
                                <th>Discount</th>
                                <th>Min. Order</th>
                                <th>Max. Cap</th>
                                <th>Valid Until</th>
                                <th>Status</th>
                                <th class="text-end pe-4">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cpn" items="${coupons}">
                                <tr>
                                    <td>
                                        <span class="badge bg-light text-dark border fw-bold fs-6 px-3 py-1 font-monospace">
                                            ${cpn.code}
                                        </span>
                                    </td>
                                    <td><span class="badge bg-secondary">${cpn.discountType}</span></td>
                                    <td class="fw-bold text-success">
                                        <c:choose>
                                            <c:when test="${cpn.discountType == 'PERCENTAGE'}">${cpn.discountValue}%</c:when>
                                            <c:otherwise>₹${cpn.discountValue}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>₹${cpn.minimumOrder}</td>
                                    <td>₹${cpn.maximumDiscount}</td>
                                    <td><fmt:formatDate value="${cpn.expiryDate}" pattern="dd MMM yyyy" /></td>
                                    <td>
                                        <span class="badge ${cpn.status ? 'bg-success bg-opacity-10 text-success' : 'bg-secondary'} rounded-pill">
                                            ${cpn.status ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <form action="${pageContext.request.contextPath}/admin/coupons" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="toggleStatus">
                                            <input type="hidden" name="couponId" value="${cpn.couponId}">
                                            <input type="hidden" name="status" value="${cpn.status ? '0' : '1'}">
                                            <button type="submit" class="btn btn-outline-${cpn.status ? 'warning' : 'success'} btn-sm rounded-pill px-3">
                                                ${cpn.status ? 'Disable' : 'Enable'}
                                            </button>
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

<!-- Modal: Add Coupon -->
<div class="modal fade" id="addCouponModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 border-0">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold">New Coupon Code</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/coupons" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Coupon Code</label>
                        <input type="text" name="code" class="form-control text-uppercase" placeholder="e.g. MEGA50" required>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-bold">Discount Type</label>
                            <select name="discountType" class="form-select">
                                <option value="PERCENTAGE">Percentage (%)</option>
                                <option value="FIXED">Fixed Amount (₹)</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-bold">Discount Value</label>
                            <input type="number" step="0.01" name="discountValue" class="form-control" placeholder="15.00" required>
                        </div>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-bold">Minimum Order (₹)</label>
                            <input type="number" step="0.01" name="minimumOrder" class="form-control" value="500.00" required>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-bold">Max Cap (₹)</label>
                            <input type="number" step="0.01" name="maximumDiscount" class="form-control" value="500.00" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Expiry Date</label>
                        <input type="date" name="expiryDate" class="form-control" value="2027-12-31" required>
                    </div>
                    <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" name="status" value="1" id="cpnStatus" checked>
                        <label class="form-check-label small fw-semibold" for="cpnStatus">Activate Immediately</label>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary rounded-pill" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4 fw-bold">Save Coupon</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
