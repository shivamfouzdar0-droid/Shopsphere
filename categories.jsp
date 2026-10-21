<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Categories | ShopSphere Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/fragments/sidebar.jsp">
            <jsp:param name="active" value="categories" />
        </jsp:include>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between align-items-center pt-3 pb-2 mb-4 border-bottom">
                <div>
                    <h1 class="h3 fw-bold mb-0">Department Categories</h1>
                    <small class="text-muted">Organize catalog navigation hierarchy</small>
                </div>
                <button class="btn btn-primary rounded-pill" data-bs-toggle="modal" data-bs-target="#addCategoryModal">
                    <i class="bi bi-plus-lg me-1"></i> Add Category
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
                                <th>Category ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th class="text-end pe-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${categories}">
                                <tr>
                                    <td class="fw-bold">#${c.categoryId}</td>
                                    <td class="fw-bold text-dark">${c.name}</td>
                                    <td class="text-muted small">${c.description}</td>
                                    <td>
                                        <span class="badge ${c.status ? 'bg-success bg-opacity-10 text-success' : 'bg-secondary'} rounded-pill">
                                            ${c.status ? 'Active' : 'Disabled'}
                                        </span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="d-inline" onsubmit="return confirm('Delete this category?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="categoryId" value="${c.categoryId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm rounded-circle">
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
        </main>
    </div>
</div>

<!-- Add Category Modal -->
<div class="modal fade" id="addCategoryModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 border-0">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold">Create New Category</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Category Title</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Smart Wearables" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Description</label>
                        <textarea name="description" class="form-control" rows="2" placeholder="Brief overview of items in this department..."></textarea>
                    </div>
                    <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" name="status" value="1" id="catStatusCheck" checked>
                        <label class="form-check-label small fw-semibold" for="catStatusCheck">Publish as Active</label>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary rounded-pill" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4 fw-bold">Create</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
