<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products Management | ShopSphere Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/fragments/sidebar.jsp">
            <jsp:param name="active" value="products" />
        </jsp:include>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between align-items-center pt-3 pb-2 mb-4 border-bottom">
                <div>
                    <h1 class="h3 fw-bold mb-0">Products & Inventory</h1>
                    <small class="text-muted">Manage store catalog, pricing, and physical warehouse stock</small>
                </div>
                <button class="btn btn-primary rounded-pill" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    <i class="bi bi-plus-lg me-1"></i> Add New Product
                </button>
            </div>

            <!-- Toast Messages -->
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
                                <th>Product</th>
                                <th>Category</th>
                                <th>Base Price</th>
                                <th>Discount</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <th class="text-end pe-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3 p-1">
                                            <img src="${p.imageUrl}" class="rounded-3" style="width: 45px; height: 45px; object-fit: cover;" alt=""
                                                 onerror="this.onerror=null;this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80';">
                                            <div>
                                                <div class="fw-bold text-dark text-truncate" style="max-width: 250px;">${p.name}</div>
                                                <span class="text-muted small">${p.brand}</span>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span class="badge bg-light text-dark border">${p.categoryName}</span></td>
                                    <td class="fw-semibold">₹${p.price}</td>
                                    <td>${p.discount}%</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.stock <= 10}">
                                                <span class="badge bg-danger rounded-pill">${p.stock} units</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-success rounded-pill">${p.stock} units</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.status}">
                                                <span class="badge bg-success bg-opacity-10 text-success rounded-pill">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary rounded-pill">Hidden</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end pe-4">
                                        <div class="d-inline-flex gap-1">
                                            <button type="button" class="btn btn-outline-primary btn-sm rounded-circle btn-edit-product" title="Edit"
                                                    data-id="${p.productId}"
                                                    data-category="${p.categoryId}"
                                                    data-name="<c:out value='${p.name}' />"
                                                    data-brand="<c:out value='${p.brand}' />"
                                                    data-price="${p.price}"
                                                    data-discount="${p.discount}"
                                                    data-stock="${p.stock}"
                                                    data-img="<c:out value='${p.imageUrl}' />"
                                                    data-status="${p.status}"
                                                    data-desc="<c:out value='${p.description}' />">
                                                <i class="bi bi-pencil"></i>
                                            </button>

                                            <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-inline" onsubmit="return confirm('Delete this product permanently?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="productId" value="${p.productId}">
                                                <button type="submit" class="btn btn-outline-danger btn-sm rounded-circle" title="Delete">
                                                    <i class="bi bi-trash3"></i>
                                                </button>
                                            </form>
                                        </div>
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

<!-- Modal: Add Product -->
<div class="modal fade" id="addProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content rounded-4 border-0">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold">Add New Product to Catalog</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label small fw-bold">Product Name</label>
                            <input type="text" name="name" class="form-control" placeholder="e.g. Sony WH-1000XM5" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Brand</label>
                            <input type="text" name="brand" class="form-control" placeholder="e.g. Sony" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold">Category</label>
                            <select name="categoryId" class="form-select" required>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.categoryId}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold">Base Price (₹)</label>
                            <input type="number" step="0.01" name="price" class="form-control" placeholder="2999.00" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold">Discount (%)</label>
                            <input type="number" step="0.01" name="discount" class="form-control" value="0.00" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Stock Quantity</label>
                            <input type="number" name="stock" class="form-control" value="25" required>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label small fw-bold">Image URL</label>
                            <input type="url" name="imageUrl" class="form-control" placeholder="https://images.unsplash.com/..." required>
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-bold">Detailed Description</label>
                            <textarea name="description" class="form-control" rows="3" placeholder="Enter key specs and features..." required></textarea>
                        </div>
                        <div class="col-12">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" name="status" value="1" id="addStatusCheck" checked>
                                <label class="form-check-label small fw-semibold" for="addStatusCheck">Visible in Storefront (Active)</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary rounded-pill" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4 fw-bold">Create Product</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal: Edit Product -->
<div class="modal fade" id="editProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content rounded-4 border-0">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold">Update Product</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="productId" id="editProductId">
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label small fw-bold">Product Name</label>
                            <input type="text" name="name" id="editName" class="form-control" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Brand</label>
                            <input type="text" name="brand" id="editBrand" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold">Category</label>
                            <select name="categoryId" id="editCategoryId" class="form-select" required>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.categoryId}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold">Base Price (₹)</label>
                            <input type="number" step="0.01" name="price" id="editPrice" class="form-control" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold">Discount (%)</label>
                            <input type="number" step="0.01" name="discount" id="editDiscount" class="form-control" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Stock Quantity</label>
                            <input type="number" name="stock" id="editStock" class="form-control" required>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label small fw-bold">Image URL</label>
                            <input type="url" name="imageUrl" id="editImageUrl" class="form-control" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-bold">Detailed Description</label>
                            <textarea name="description" id="editDescription" class="form-control" rows="3" required></textarea>
                        </div>
                        <div class="col-12">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" name="status" value="1" id="editStatusCheck">
                                <label class="form-check-label small fw-semibold" for="editStatusCheck">Visible in Storefront (Active)</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary rounded-pill" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4 fw-bold">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.btn-edit-product').forEach(function(btn) {
        btn.addEventListener('click', function() {
            document.getElementById('editProductId').value = this.dataset.id || '';
            document.getElementById('editCategoryId').value = this.dataset.category || '';
            document.getElementById('editName').value = this.dataset.name || '';
            document.getElementById('editBrand').value = this.dataset.brand || '';
            document.getElementById('editPrice').value = this.dataset.price || '';
            document.getElementById('editDiscount').value = this.dataset.discount || '';
            document.getElementById('editStock').value = this.dataset.stock || '';
            document.getElementById('editImageUrl').value = this.dataset.img || '';
            document.getElementById('editDescription').value = this.dataset.desc || '';
            document.getElementById('editStatusCheck').checked = (this.dataset.status === 'true' || this.dataset.status === '1');
            
            new bootstrap.Modal(document.getElementById('editProductModal')).show();
        });
    });
});
</script>
</body>
</html>
