<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

<fmt:message bundle="${local}" key="page.department_control" var="page"/>
<fmt:message bundle="${local}" key="department_control.title" var="title"/>
<fmt:message bundle="${local}" key="department_control.message.department_head.part1" var="messagePart1"/>
<fmt:message bundle="${local}" key="department_control.message.department_head.part2" var="messagePart2"/>
<fmt:message bundle="${local}" key="department_control.message.nullLogin" var="messageNullLogin"/>
<fmt:message bundle="${local}" key="department_control.btn.change_head" var="btnChangeHead"/>
<fmt:message bundle="${local}" key="department_control.btn.move_to_department" var="btnMoveToDepartment"/>
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
<!--================Blog Area =================-->
<section class="blog_area section-padding">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 posts-list">
                <div class="comment-form">
                    <h4>Assigned medication</h4>
                    <p>${requestScope.message}</p>
                    <p>${patient.userDetails.firstName} ${patient.userDetails.surname} ${patient.userDetails.lastName}</p>
                    <p>${diagnosis.icd.title}</p>
                    <c:forEach items="${assignment_medications_list}" var="assignment">
                        <div class="form-group form-inline">
                            <div class="col-lg-12 col-md-12 d-flex justify-content-between">
                                <div class="form-group col-lg-4 col-md-4">
                                    <input type="text" disabled class="form-control" value="${assignment.medicament.name}">
                                </div>
                                <div class="form-group col-lg-4 col-md-4">
                                    <input type="text" disabled class="form-control" value="${assignment.time}">
                                </div>
                                <div class="form-group col-lg-4 col-md-4">
                                    <input type="text" disabled class="form-control" value="${assignment.description}">
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</section>
<!--================Blog Area =================-->
<%@include file="component/footer.jsp" %>
</body>
</html>
