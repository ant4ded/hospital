<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Medino</title>
    <%@include file="component/head.jsp" %>
</head>
<body>
<%@include file="component/navbar.jsp" %>
<!-- Banner Area Starts -->
<section class="banner-area other-page">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <h1>Error page</h1>
                <a href="${HospitalUrl.MAIN_URL}">Home</a> <span>|</span> <a href="#">Error</a>
            </div>
        </div>
    </div>
</section>
<!-- Banner Area End -->
<div class="error-header">
    <%=request.getAttribute("javax.servlet.error.status_code")%>
    <p>
        <%=request.getAttribute("javax.servlet.error.message")%>
    </p>
    <c:if test="${requestScope.message != null}">
        <p>${requestScope.message}</p>
    </c:if>
</div>
<%@include file="component/footer.jsp" %>
</body>
</html>