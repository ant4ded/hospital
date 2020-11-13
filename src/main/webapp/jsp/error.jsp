<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Medino</title>
    <%@include file="component/head.jsp" %>
</head>
<body>
<%@include file="component/navbar.jsp" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale" var="local"/>

<fmt:message bundle="${local}" key="page.error" var="page"/>
<!-- Banner Area Starts -->
<section class="banner-area other-page">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <h1>${page}</h1>
                <a href="${HospitalUrl.MAIN_URL}">${home}</a> <span>|</span> <a href="#">${page}</a>
            </div>
        </div>
    </div>
</section>
<!-- Banner Area End -->
<div class="error-header">
    <%=request.getAttribute(ParameterName.SERVLET_STATUS_CODE)%>
    <p>
        <%=request.getAttribute(ParameterName.SERVLET_EXCEPTION_MESSAGE)%>
    </p>
    <c:if test="${requestScope.message != null}">
        <p>${requestScope.message}</p>
    </c:if>
</div>
<%@include file="component/footer.jsp" %>
</body>
</html>