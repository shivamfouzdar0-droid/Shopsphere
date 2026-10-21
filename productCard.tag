<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" %>

<div class="col">
    <div class="card h-100 product-card shadow-sm border-0 position-relative">
        <c:if test="${product.discount > 0}">
            <span class="badge bg-danger position-absolute top-0 start-0 m-3 px-2 py-1 rounded-pill shadow-sm">
                -${product.discount}% OFF
            </span>
        </c:if>

        <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="text-decoration-none">
            <div class="product-img-wrapper">
                <img src="${product.imageUrl}" class="card-img-top product-img" alt="${product.name}" 
                     onerror="this.onerror=null;this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80';">
            </div>
        </a>

        <div class="card-body d-flex flex-column p-3">
            <div class="d-flex justify-content-between align-items-center mb-1">
                <span class="text-muted text-uppercase fw-semibold" style="font-size: 0.75rem; letter-spacing: 0.5px;">
                    ${product.brand}
                </span>
                <c:if test="${not empty product.categoryName}">
                    <span class="badge bg-light text-secondary border" style="font-size: 0.7rem;">
                        ${product.categoryName}
                    </span>
                </c:if>
            </div>

            <h5 class="card-title fs-6 fw-bold mb-2 text-truncate-2">
                <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="text-dark text-decoration-none title-hover">
                    ${product.name}
                </a>
            </h5>

            <div class="mt-auto pt-2">
                <div class="d-flex align-items-baseline gap-2 mb-3">
                    <span class="fs-5 fw-bold text-primary">₹${product.discountedPrice}</span>
                    <c:if test="${product.discount > 0}">
                        <span class="text-muted text-decoration-line-through small">₹${product.price}</span>
                    </c:if>
                </div>

                <div class="d-grid gap-2">
                    <c:choose>
                        <c:when test="${product.available}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="m-0">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <input type="hidden" name="quantity" value="1">
                                <input type="hidden" name="redirect" value="${pageContext.request.requestURI}?${pageContext.request.queryString}">
                                <button type="submit" class="btn btn-outline-primary btn-sm w-100 fw-semibold rounded-pill py-2 shadow-sm btn-hover-elevate">
                                    <i class="bi bi-cart-plus me-1"></i> Add to Cart
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-secondary btn-sm w-100 rounded-pill py-2" disabled>
                                Out of Stock
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
