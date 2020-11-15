<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.Therapy" %>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName" %>
<%@page import="java.util.List" %>
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
<% List<Therapy> therapies = (List<Therapy>) request.getAttribute(ParameterName.THERAPIES_LIST);%>
<!-- Banner Area End -->
<!--================Blog Area =================-->
<c:choose>
    <c:when test="<%=therapies != null && !therapies.isEmpty()%>">
        <div class="container-fluid">
            <div class="section-top-border">
                <h3 class="mb-30 title_color">Therapies of your patients</h3>
                <div class="progress-table-wrap">
                    <div class="progress-table">
                        <div class="table-head">
                            <div class="serial">#</div>
                            <div class="user-details">Patient</div>
                            <div class="diagnosis-code">ICD ICD</div>
                            <div class="diagnosis-title">Final Diagnosis Title</div>
                            <div class="table-date">End Therapy</div>
                        </div>
                        <c:forEach items="<%=therapies%>" var="therapy" varStatus="loop">
                            <div class="table-row">
                                <div class="serial">${loop.index + 1}</div>
                                <div class="user-details">
                                        ${therapy.patient.userDetails.firstName}<br>
                                        ${therapy.patient.userDetails.surname}<br>
                                        ${therapy.patient.userDetails.lastName}
                                </div>
                                <c:choose>
                                    <c:when test="${therapy.finalDiagnosis.orElse(null) == null && therapy.endTherapy.orElse(null) == null}">
                                        <div class="diagnosis-code">Status:</div>
                                        <div class="diagnosis-title">In the process of identification</div>
                                        <div class="table-date">Open</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="diagnosis-code">${therapy.finalDiagnosis.get().icd.code}</div>
                                        <div class="diagnosis-title">${therapy.finalDiagnosis.get().icd.title}</div>
                                        <div class="table-date">Open</div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="table-row-wrapper">
                                    <c:forEach items="${therapy.diagnoses}" var="diagnosis" varStatus="loop">
                                        <div class="table-row">
                                            <div class="table-group">
                                                <div class="table-parameter">Attending doctor</div>
                                                <div class="table-value">
                                                        ${diagnosis.doctor.userDetails.firstName} ${diagnosis.doctor.userDetails.surname} ${diagnosis.doctor.userDetails.lastName}
                                                </div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">Icd code</div>
                                                <div class="table-value">${diagnosis.icd.code}</div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">Diagnosis</div>
                                                <div class="table-value">${diagnosis.icd.title}</div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">Attending date</div>
                                                <div class="table-value">${diagnosis.diagnosisDate}</div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">Reason</div>
                                                <div class="table-value">${diagnosis.reason}</div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    <div class="table-row">
                                        <div class="table-group">
                                            <form action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_MAKE_LAST_DIAGNOSIS_FINAL}">
                                                <input type="hidden" name="${ParameterName.COMMAND}"
                                                       value="${CommandName.MAKE_LAST_DIAGNOSIS_FINAL}">
                                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                                                       value="${HospitalUrl.PAGE_DOCTOR_THERAPIES}">
                                                <input type="hidden" name="${UsersDetailsFieldName.FIRST_NAME}"
                                                       value="${therapy.patient.userDetails.firstName}">
                                                <input type="hidden" name="${UsersDetailsFieldName.SURNAME}"
                                                       value="${therapy.patient.userDetails.surname}">
                                                <input type="hidden" name="${UsersDetailsFieldName.LAST_NAME}"
                                                       value="${therapy.patient.userDetails.lastName}">
                                                <input type="hidden" name="${UsersDetailsFieldName.BIRTHDAY}"
                                                       value="${therapy.patient.userDetails.birthday}">
                                                <input type="hidden" name="${ParameterName.CARD_TYPE}"
                                                       value="<%=request.getParameter(ParameterName.CARD_TYPE)%>">
                                                <button type="submit" class="template-btn form-btn">Make last final
                                                </button>
                                            </form>
                                            <form action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_CLOSE_THERAPY}">
                                                <input type="hidden" name="${ParameterName.COMMAND}"
                                                       value="${CommandName.CLOSE_THERAPY}">
                                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                                                       value="${HospitalUrl.PAGE_DOCTOR_THERAPIES}">
                                                <input type="hidden" name="${UsersDetailsFieldName.FIRST_NAME}"
                                                       value="${therapy.patient.userDetails.firstName}">
                                                <input type="hidden" name="${UsersDetailsFieldName.SURNAME}"
                                                       value="${therapy.patient.userDetails.surname}">
                                                <input type="hidden" name="${UsersDetailsFieldName.LAST_NAME}"
                                                       value="${therapy.patient.userDetails.lastName}">
                                                <input type="hidden" name="${UsersDetailsFieldName.BIRTHDAY}"
                                                       value="${therapy.patient.userDetails.birthday}">
                                                <input type="hidden" name="${ParameterName.CARD_TYPE}"
                                                       value="<%=request.getParameter(ParameterName.CARD_TYPE)%>">
                                                <button type="submit" class="template-btn form-btn">
                                                    Discharge a patient
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="error-header">
            <p>${requestScope.message}</p>
        </div>
    </c:otherwise>
</c:choose>
<!--================Blog Area =================-->
<%@include file="component/footer.jsp" %>
</body>
</html>
