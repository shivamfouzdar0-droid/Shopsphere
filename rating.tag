<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="score" required="true" type="java.lang.Double" %>
<%@ attribute name="showNumber" required="false" type="java.lang.Boolean" %>

<div class="d-inline-flex align-items-center text-warning me-1">
    <c:forEach var="i" begin="1" end="5">
        <c:choose>
            <c:when test="${score >= i}">
                <i class="bi bi-star-fill"></i>
            </c:when>
            <c:when test="${score >= (i - 0.5)}">
                <i class="bi bi-star-half"></i>
            </c:when>
            <c:otherwise>
                <i class="bi bi-star text-muted opacity-50"></i>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${showNumber}">
        <span class="ms-1 fw-bold text-dark small">(${score})</span>
    </c:if>
</div>
