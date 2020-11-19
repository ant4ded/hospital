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

<fmt:message bundle="${local}" key="page.doctor_therapies" var="page"/>
<fmt:message bundle="${local}" key="doctor_therapies.title" var="title"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.main.head.patient" var="mainHeadPatient"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.main.head.icd" var="mainHeadIcd"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.main.head.final_diagnosis" var="mainHeadFinalDiagnosis"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.main.head.therapy_end_date" var="mainHeadEndTherapy"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.main.status" var="mainStatus"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.main.status.final_diagnosis" var="mainStatusFinalDiagnosis"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.main.status.therapy" var="mainStatusTherapy"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.inner.head.doctor" var="innerHeadDoctor"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.inner.head.icd" var="innerHeadIcd"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.inner.head.diagnosis" var="innerHeadDiagnosis"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.inner.head.date" var="innerHeadDate"/>
<fmt:message bundle="${local}" key="doctor_therapies.table.inner.head.reason" var="innerHeadReason"/>
<fmt:message bundle="${local}" key="doctor_therapies.btn.make_last_final" var="btnMakeLastFinal"/>
<fmt:message bundle="${local}" key="doctor_therapies.btn.discharge_patient" var="btnDischargePatient"/>
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
                <h3 class="mb-30 title_color">${title}</h3>
                <div class="progress-table-wrap">
                    <div class="progress-table">
                        <div class="table-head">
                            <div class="serial">#</div>
                            <div class="user-details">${mainHeadPatient}</div>
                            <div class="diagnosis-code">${mainHeadIcd}</div>
                            <div class="diagnosis-title">${mainHeadFinalDiagnosis}</div>
                            <div class="table-date">${mainHeadEndTherapy}</div>
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
                                    <c:when test="${therapy.finalDiagnosis.orElse(null) == null &&
                                        therapy.endTherapy.orElse(null) == null}">
                                        <div class="diagnosis-code">${mainStatus}</div>
                                        <div class="diagnosis-title">${mainStatusFinalDiagnosis}</div>
                                        <div class="table-date">${mainStatusTherapy}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="diagnosis-code">${therapy.finalDiagnosis.get().icd.code}</div>
                                        <div class="diagnosis-title">${therapy.finalDiagnosis.get().icd.title}</div>
                                        <div class="table-date">${mainStatusTherapy}</div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="table-row-wrapper">
                                    <c:forEach items="${therapy.diagnoses}" var="diagnosis" varStatus="loop">
                                        <div class="table-row">
                                            <div class="table-group">
                                                <div class="table-parameter">${innerHeadDoctor}</div>
                                                <div class="table-value">
                                                        ${diagnosis.doctor.userDetails.firstName} ${diagnosis.doctor.userDetails.surname} ${diagnosis.doctor.userDetails.lastName}
                                                </div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">${innerHeadIcd}</div>
                                                <div class="table-value">${diagnosis.icd.code}</div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">${innerHeadDiagnosis}</div>
                                                <div class="table-value">${diagnosis.icd.title}</div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">${innerHeadDate}</div>
                                                <div class="table-value">${diagnosis.diagnosisDate}</div>
                                            </div>
                                            <div class="table-group">
                                                <div class="table-parameter">${innerHeadReason}</div>
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
                                                <button type="submit" class="template-btn form-btn">
                                                        ${btnMakeLastFinal}
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
                                                    ${btnDischargePatient}
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
