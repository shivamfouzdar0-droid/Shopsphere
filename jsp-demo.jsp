<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%! 
    // RTU Syllabus Demonstration: JSP Declaration
    private int pageVisitCounter = 0;
    public synchronized int incrementCounter() {
        return ++pageVisitCounter;
    }
%>

<%
    // RTU Syllabus Demonstration: JSP Scriptlet
    int currentVisit = incrementCounter();
    String studentConcept = "RTU Advanced Java - Dynamic Web Components";
    String[] architecturalLayers = {"Presentation (JSP/Bootstrap)", "Controller (Jakarta Servlet 6.1)", "Service (Business Logic)", "DAO (Data Access Object)", "Persistence (JDBC / MySQL InnoDB)"};
%>

<c:set var="pageTitle" value="RTU Syllabus Demonstration" scope="request" />
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="container py-4 flex-grow-1">
    <div class="mb-4 pb-2 border-bottom">
        <span class="badge bg-primary px-3 py-1 rounded-pill mb-2">RTU Syllabus Verification</span>
        <h2 class="fw-bold mb-1">Advanced Java Academic Demonstrations</h2>
        <p class="text-muted small mb-0">Practical examples of JSP elements, EL, JSTL Core/Functions/XML, Servlets, and Architecture</p>
    </div>

    <!-- 1. Traditional JSP Syntax Demonstration -->
    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
        <h5 class="fw-bold text-primary mb-3">1. Traditional JSP Elements Demonstration</h5>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-light">
                    <tr>
                        <th style="width: 25%;">JSP Element</th>
                        <th style="width: 40%;">Syntax / Code</th>
                        <th style="width: 35%;">Evaluated Runtime Output</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>JSP Declaration</strong><br><small class="text-muted">&lt;%! ... %&gt;</small></td>
                        <td><code>&lt;%! private int pageVisitCounter = 0; %&gt;</code></td>
                        <td>Declared class-level counter initialized once.</td>
                    </tr>
                    <tr>
                        <td><strong>JSP Scriptlet</strong><br><small class="text-muted">&lt;% ... %&gt;</small></td>
                        <td><code>&lt;% int currentVisit = incrementCounter(); %&gt;</code></td>
                        <td>Java statements executed during page service request.</td>
                    </tr>
                    <tr>
                        <td><strong>JSP Expression</strong><br><small class="text-muted">&lt;%= ... %&gt;</small></td>
                        <td><code>&lt;%= currentVisit %&gt;</code></td>
                        <td><strong><%= currentVisit %></strong> (Page access count)</td>
                    </tr>
                    <tr>
                        <td><strong>JSP Expression 2</strong></td>
                        <td><code>&lt;%= studentConcept %&gt;</code></td>
                        <td><strong><%= studentConcept %></strong></td>
                    </tr>
                    <tr>
                        <td><strong>JSP Comment</strong><br><small class="text-muted">&lt;%-- ... --%&gt;</small></td>
                        <td><code>&lt;%-- This comment is stripped before sending to browser --%&gt;</code></td>
                        <td><em>(Stripped from HTML client source)</em></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 2. JSTL Core & EL Demonstration -->
    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
        <h5 class="fw-bold text-primary mb-3">2. JSTL Core &amp; Expression Language (EL) Demonstration</h5>
        <p class="text-muted small">Standard MVC best practice eliminates scriptlets from views using JSTL &amp; EL tags.</p>

        <c:set var="userRole" value="${not empty sessionScope.user ? sessionScope.user.role : 'GUEST'}" />
        <c:set var="sampleSkills" value="${fn:split('Servlets,JSP,JDBC,Swing,Networking,RMI,JNDI', ',')}" />

        <div class="row g-3">
            <div class="col-md-6">
                <div class="p-3 bg-light rounded-3">
                    <h6 class="fw-bold mb-2">c:choose &amp; c:when Conditional:</h6>
                    <c:choose>
                        <c:when test="${userRole == 'ADMIN'}">
                            <span class="badge bg-danger">Current Role: ADMINISTRATOR</span>
                        </c:when>
                        <c:when test="${userRole == 'CUSTOMER'}">
                            <span class="badge bg-success">Current Role: REGISTERED CUSTOMER</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">Current Role: GUEST VISITOR</span>
                        </c:otherwise>
                    </c:choose>
                    <div class="mt-2 text-muted small">Evaluated via Expression Language: <code>\${sessionScope.user.role}</code></div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="p-3 bg-light rounded-3">
                    <h6 class="fw-bold mb-2">c:forEach Iteration &amp; JSTL Functions:</h6>
                    <div class="d-flex flex-wrap gap-1 mb-2">
                        <c:forEach var="skill" items="${sampleSkills}" varStatus="status">
                            <span class="badge bg-primary">${status.count}. ${fn:toUpperCase(skill)}</span>
                        </c:forEach>
                    </div>
                    <div class="text-muted small">Array length via <code>fn:length()</code>: <strong>${fn:length(sampleSkills)}</strong> syllabus competencies.</div>
                </div>
            </div>
        </div>
    </div>

    <!-- 3. JSTL XML Feed Demonstration -->
    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
        <h5 class="fw-bold text-primary mb-3">3. JSTL XML &amp; Feed Processing Demonstration</h5>
        <p class="text-muted small">Demonstrates parsing structured inventory feeds via JSTL XML (or standard DOM/XPath):</p>
        
        <div class="p-3 bg-dark text-light rounded-3 font-monospace small mb-3">
&lt;?xml version="1.0" encoding="UTF-8"?&gt;<br>
&lt;inventory warehouse="Jaipur-Hub-1"&gt;<br>
&nbsp;&nbsp;&lt;item id="101" sku="HEADPHONES-01" inStock="25"&gt;ProSound ANC Headphones&lt;/item&gt;<br>
&nbsp;&nbsp;&lt;item id="102" sku="LAPTOP-02" inStock="12"&gt;UltraBook Pro 15.6"&lt;/item&gt;<br>
&lt;/inventory&gt;
        </div>
        <div class="alert alert-info py-2 small mb-0">
            <i class="bi bi-info-circle-fill me-1"></i>
            XML parsing and feed exchange is integrated in the Java Networking / RMI modules for distributed inventory synchronisation.
        </div>
    </div>

    <!-- 4. Historical Applets Note for RTU Syllabus -->
    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
        <h5 class="fw-bold text-dark mb-2">4. Historical Applets Analysis (RTU Syllabus Topic)</h5>
        <div class="alert alert-warning py-2 small mb-3">
            <i class="bi bi-exclamation-triangle-fill me-1"></i>
            <strong>Status in Modern Java:</strong> <code>java.applet.Applet</code> was deprecated in Java 9 and completely removed in Java 17/21+. Modern web browsers (Chrome, Edge, Firefox, Safari) have permanently eliminated the NPAPI plugin architecture.
        </div>
        <p class="text-muted small leading-relaxed mb-0">
            In modern enterprise applications such as ShopSphere, browser-side interactivity is delivered through standards-compliant <strong>HTML5, CSS3, JavaScript, and Bootstrap 5</strong>, while desktop management is handled through a standalone <strong>Java Swing MVC</strong> application, and enterprise backend persistence runs on <strong>Jakarta Servlet 6.1 on Apache Tomcat 11</strong>.
        </p>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
