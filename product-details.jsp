<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="${product.name}" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-4 flex-grow-1">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Products</a></li>
            <c:if test="${not empty product.categoryName}">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products?category=${product.categoryId}" class="text-decoration-none">${product.categoryName}</a></li>
            </c:if>
            <li class="breadcrumb-item active text-truncate" aria-current="page" style="max-width: 250px;">${product.name}</li>
        </ol>
    </nav>

    <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 mb-5 bg-white">
        <div class="row g-5">
            <!-- Product Image -->
            <div class="col-lg-6">
                <div class="border rounded-4 p-3 bg-light text-center d-flex align-items-center justify-content-center" style="min-height: 400px;">
                    <img src="${product.imageUrl}" class="img-fluid rounded-3 shadow-sm" alt="${product.name}" 
                         style="max-height: 380px; object-fit: contain;"
                         onerror="this.onerror=null;this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80';">
                </div>
            </div>

            <!-- Product Details -->
            <div class="col-lg-6 d-flex flex-column">
                <div class="mb-2">
                    <span class="text-uppercase fw-bold text-muted" style="letter-spacing: 1px; font-size: 0.8rem;">
                        ${product.brand}
                    </span>
                    <c:if test="${not empty product.categoryName}">
                        <span class="badge bg-primary bg-opacity-10 text-primary ms-2">${product.categoryName}</span>
                    </c:if>
                </div>

                <h1 class="h2 fw-bold text-dark mb-3">${product.name}</h1>

                <!-- Rating -->
                <div class="d-flex align-items-center gap-2 mb-3">
                    <tag:rating score="${avgRating}" showNumber="${true}" />
                    <span class="text-muted small">(${reviewCount} customer reviews)</span>
                </div>

                <!-- Price & Discount -->
                <div class="d-flex align-items-baseline gap-3 mb-4">
                    <span class="display-6 fw-bold text-primary">₹${product.discountedPrice}</span>
                    <c:if test="${product.discount > 0}">
                        <span class="fs-4 text-muted text-decoration-line-through">₹${product.price}</span>
                        <span class="badge bg-danger fs-6 px-3 py-1 rounded-pill">Save ${product.discount}%</span>
                    </c:if>
                </div>

                <!-- Availability Badge -->
                <div class="mb-4">
                    <c:choose>
                        <c:when test="${product.stock > 10}">
                            <span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 px-3 py-2 rounded-pill">
                                <i class="bi bi-check-circle-fill me-1"></i> In Stock (${product.stock} units ready to ship)
                            </span>
                        </c:when>
                        <c:when test="${product.stock > 0}">
                            <span class="badge bg-warning bg-opacity-10 text-warning border border-warning border-opacity-25 px-3 py-2 rounded-pill">
                                <i class="bi bi-exclamation-circle-fill me-1"></i> Only ${product.stock} units left!
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-danger bg-opacity-10 text-danger border border-danger border-opacity-25 px-3 py-2 rounded-pill">
                                <i class="bi bi-x-circle-fill me-1"></i> Currently Out of Stock
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Description -->
                <div class="mb-4 text-muted leading-relaxed">
                    <p>${product.description}</p>
                </div>

                <!-- Add to Cart & Wishlist Actions -->
                <div class="mt-auto pt-3 border-top">
                    <c:choose>
                        <c:when test="${product.available}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="d-flex flex-wrap align-items-center gap-3">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.productId}">
                                
                                <div class="d-flex align-items-center border rounded-pill px-3 py-1 bg-light">
                                    <label for="quantitySelect" class="me-2 small fw-semibold text-muted">Qty:</label>
                                    <input type="number" id="quantitySelect" name="quantity" value="1" min="1" max="${product.stock}" 
                                           class="form-control form-control-sm border-0 bg-transparent text-center fw-bold" style="width: 50px;">
                                </div>

                                <button type="submit" class="btn btn-primary btn-lg rounded-pill px-4 flex-grow-1 shadow-sm fw-bold">
                                    <i class="bi bi-cart-plus me-2"></i> Add to Bag
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-secondary btn-lg rounded-pill w-100 disabled" disabled>
                                Currently Unavailable
                            </button>
                        </c:otherwise>
                    </c:choose>

                    <!-- Add to Wishlist -->
                    <form action="${pageContext.request.contextPath}/wishlist" method="post" class="mt-2">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/product-details?id=${product.productId}">
                        <button type="submit" class="btn btn-outline-secondary btn-sm rounded-pill w-100 py-2">
                            <i class="bi bi-heart me-1 text-danger"></i> Add to Wishlist
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Customer Reviews Section -->
    <section class="card border-0 shadow-sm rounded-4 p-4 p-md-5 mb-5 bg-white">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Customer Reviews</h3>
                <small class="text-muted">Real verified ratings and opinions</small>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="display-6 fw-bold text-dark">${avgRating}</span>
                <div>
                    <tag:rating score="${avgRating}" showNumber="${false}" />
                    <div class="text-muted small">${reviewCount} Total Ratings</div>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <!-- Review List -->
            <div class="col-lg-7">
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <div class="d-flex flex-column gap-3">
                            <c:forEach var="r" items="${reviews}">
                                <div class="p-3 border rounded-3 bg-light">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <span class="fw-bold text-dark">${r.userName}</span>
                                        <tag:rating score="${r.rating + 0.0}" showNumber="${false}" />
                                    </div>
                                    <p class="text-muted small mb-1">${r.reviewText}</p>
                                    <span class="text-secondary" style="font-size: 0.75rem;">
                                        <fmt:formatDate value="${r.createdAt}" pattern="dd MMM yyyy" />
                                    </span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">No reviews yet for this product. Be the first to share your experience!</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Write a Review Form -->
            <div class="col-lg-5">
                <div class="card p-3 border-0 bg-light rounded-3">
                    <h5 class="fw-bold mb-3">Leave a Rating</h5>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <form action="${pageContext.request.contextPath}/review" method="post">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Star Rating</label>
                                    <select name="rating" class="form-select form-select-sm" required>
                                        <option value="5">⭐⭐⭐⭐⭐ 5 Stars - Outstanding</option>
                                        <option value="4">⭐⭐⭐⭐ 4 Stars - Very Good</option>
                                        <option value="3">⭐⭐⭐ 3 Stars - Average</option>
                                        <option value="2">⭐⭐ 2 Stars - Below Expectation</option>
                                        <option value="1">⭐ 1 Star - Poor</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Your Review</label>
                                    <textarea name="reviewText" class="form-control form-control-sm" rows="3" placeholder="Describe quality, durability, and comfort..." required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm rounded-pill w-100 fw-bold">
                                    Submit Review
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-3">
                                <p class="text-muted small mb-2">You must be logged in to leave a review.</p>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary btn-sm rounded-pill px-4">
                                    Sign In to Review
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/fragments/footer.jsp" />
