<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.table.MedicationsFieldName" %>
<%@page import="by.epam.hospital.entity.table.MedicationsAssignmentFieldName" %>
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
                    <h4>Medicament control panel</h4>
                    <p>${requestScope.message}</p>
                    <form method="post"
                          action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_DOCTOR_FIND_MEDICATIONS_PAGING}">
                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                               value="${HospitalUrl.PAGE_ASSIGN_MEDICAMENT}">
                        <input type="hidden" name="${ParameterName.COMMAND}"
                               value="${CommandName.DOCTOR_FIND_MEDICATIONS_PAGING}">
                        <div class="form-group form-inline">
                            <div class="form-group col-lg-11 col-md-11 name">
                                <input type="text" name="${ParameterName.NAME_PART}" required
                                       class="form-control" placeholder="Medicament name"
                                       value="<%=request.getParameter(ParameterName.NAME_PART) == null ?
                                        "" : request.getParameter(ParameterName.NAME_PART)%>"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Medicament name'">
                            </div>
                            <div class="form-group col-lg-1 col-md-1 name">
                                <button type="submit" class="template-btn">${btnFind}</button>
                            </div>
                        </div>
                        <c:if test="${requestScope.total_pages != null && requestScope.page_number == null}">
                            <div class="form-group form-inline justify-content-center">
                                <div class="form-group col-lg-1 col-md-1 name page-block">
                                    <input type="hidden" name="${ParameterName.PAGE_NUMBER}" value="1">
                                    <button type="submit" class="page-btn primary">1</button>
                                </div>
                                <div class="form-group col-lg-1 col-md-1 name page-block">
                                    <input type="hidden" name="${ParameterName.PAGE_NUMBER}" value="2">
                                    <button type="submit" class="page-btn">2</button>
                                </div>
                                <div class="form-group col-lg-1 col-md-1 name page-block">
                                    <input type="hidden" name="${ParameterName.PAGE_NUMBER}" value="3">
                                    <button type="submit" class="page-btn">3</button>
                                </div>
                                <div class="form-group col-lg-1 col-md-1 name page-block">
                                    <input type="hidden" name="${ParameterName.PAGE_NUMBER}" value="4">
                                    <button type="submit" class="page-btn">4</button>
                                </div>
                                <div class="form-group col-lg-1 col-md-1 name page-block">
                                    <input type="hidden" name="${ParameterName.PAGE_NUMBER}" value="5">
                                    <button type="submit" class="page-btn">5</button>
                                </div>
                                <div class="form-group col-lg-1 col-md-1 name page-block">
                                    <input type="hidden" name="${ParameterName.PAGE_NUMBER}" value="${requestScope.total_pages}">
                                    <button type="submit" class="page-btn">${requestScope.total_pages}</button>
                                </div>
                            </div>
                        </c:if>
                    </form>
                    <c:forEach items="${requestScope.medicament_list}" var="medicament" varStatus="loop">
                        <div class="form-group form-inline">
                            <form method="post" class="col-lg-12 col-md-12 d-flex justify-content-between"
                                  action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_ASSIGN_MEDICAMENT}">
                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"  value="${HospitalUrl.PAGE_ASSIGN_MEDICAMENT}">
                                <input type="hidden" name="${ParameterName.COMMAND}"            value="${CommandName.ASSIGN_MEDICAMENT}">
                                <input type="hidden" name="${ParameterName.PATIENT}"            value="<%=request.getParameter(ParameterName.PATIENT)%>">
                                <input type="hidden" name="${ParameterName.CARD_TYPE}"          value="<%=request.getParameter(ParameterName.CARD_TYPE)%>">
                                <input type="hidden" name="${MedicationsFieldName.NAME}"        value="${medicament.name}">
                                <div class="form-group col-lg-4 col-md-4">
                                    <input type="text" disabled class="form-control" value="${medicament.name}">
                                </div>
                                <div class="form-group col-lg-4 col-md-4">
                                    <input type="text" name="${MedicationsAssignmentFieldName.DESCRIPTION}"
                                           class="form-control" placeholder="Description"
                                           onfocus="this.placeholder = ''" onblur="this.placeholder = 'Description'">
                                </div>
                                <div class="form-group col-lg-2 col-md-2">
                                    <button type="submit" class="genric-btn primary">Add</button>
                                </div>
                            </form>
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
