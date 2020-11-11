<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.Department" %>
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
                    <h4>${title}</h4>
                    <p>${requestScope.message}</p>
                    <c:if test="${requestScope.userRoles.contains(Role.DEPARTMENT_HEAD)}">
                        <p>
                            ${messagePart1} ${requestScope.department}. ${messagePart2}
                        </p>
                    </c:if>
                    <c:if test="${requestScope.login == null}">
                        <p>${messageNullLogin}</p>
                    </c:if>
                    <form method="post"
                          action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_FIND_DEPARTMENT_CONTROL_ATTRIBUTES}">
                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                               value="${HospitalUrl.PAGE_DEPARTMENT_CONTROL}">
                        <input type="hidden" name="${ParameterName.COMMAND}"
                               value="${CommandName.FIND_DEPARTMENT_CONTROL_ATTRIBUTES}">
                        <div class="form-group form-inline">
                            <div class="form-group col-lg-11 col-md-11 name">
                                <input type="text" name="${UsersFieldName.LOGIN}" required
                                       class="form-control" placeholder="${inputLogin}"
                                       value="<%=request.getParameter(UsersFieldName.LOGIN) == null ?
                                        "" : request.getParameter(UsersFieldName.LOGIN)%>"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputLogin}'">
                            </div>
                            <div class="form-group col-lg-1 col-md-1 name">
                                <button type="submit" class="template-btn">${btnFind}</button>
                            </div>
                        </div>
                    </form>
                    <c:forEach items="${Department.values()}" var="department">
                        <div class="form-group form-inline">
                            <form method="post" class="col-lg-9 col-md-9 d-flex"
                                  action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                                       value="${HospitalUrl.PAGE_DEPARTMENT_CONTROL}">
                                <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                       value="${department}">
                                <input type="hidden" name="${ParameterName.COMMAND}"
                                       value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                                <div class="form-group col-lg-4 col-md-4">
                                    <c:if test="${requestScope.department.equals(department)}">
                                        <i class="fa fa-user-md"></i>
                                    </c:if>
                                    ${department}
                                </div>
                                <div class="form-group col-lg-4 col-md-4">
                                    <input type="text" name="${UsersFieldName.LOGIN}" required
                                           class="form-control" placeholder="${inputLogin}"
                                           onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputLogin}'">
                                </div>
                                <div class="form-group col-lg-4 col-md-4">
                                    <button type="submit" class="genric-btn primary">${btnChangeHead}</button>
                                </div>
                            </form>
                            <c:choose>
                                <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                    <form method="post" class="col-lg-3 col-md-3"
                                          action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_MOVE_DOCTOR_TO_DEPARTMENT}">
                                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                                               value="${HospitalUrl.PAGE_DEPARTMENT_CONTROL}">
                                        <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                               value="${department}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                        <input type="hidden" name="${UsersFieldName.LOGIN}"
                                               value="<%=request.getParameter(UsersFieldName.LOGIN)%>">
                                        <button type="submit" class="genric-btn primary">
                                            ${btnMoveToDepartment}
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" class="col-lg-3 col-md-3" action="#">
                                        <button type="submit" class="genric-btn primary disable" disabled>
                                            ${btnMoveToDepartment}
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
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
