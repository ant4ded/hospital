<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.Therapy" %>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName" %>
<%@page import="by.epam.hospital.service.util.JsonConverter" %>
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
                            <div class="serial col-lg-1 col-md-1">#</div>
                            <div class="user-details col-lg-1 col-md-1">${mainHeadPatient}</div>
                            <div class="diagnosis-code col-lg-1 col-md-1">${mainHeadIcd}</div>
                            <div class="diagnosis-title col-lg-2 col-md-2">${mainHeadFinalDiagnosis}</div>
                            <div class="table-date col-lg-1 col-md-1">${mainHeadEndTherapy}</div>
                        </div>
                        <div class="table-row-border-solid"></div>
                        <c:forEach items="<%=therapies%>" var="therapy" varStatus="loop">
                            <div class="table-row">
                                <div class="align-self-center serial col-lg-1 col-md-1">${loop.index + 1}</div>
                                <div class="align-self-center user-details col-lg-1 col-md-1">
                                        ${therapy.patient.userDetails.firstName} ${therapy.patient.userDetails.surname} ${therapy.patient.userDetails.lastName}
                                </div>
                                <c:choose>
                                    <c:when test="${therapy.finalDiagnosis.orElse(null) == null &&
                                        therapy.endTherapy.orElse(null) == null}">
                                        <div class="align-self-center diagnosis-code col-lg-1 col-md-1">${mainStatus}</div>
                                        <div class="align-self-center diagnosis-title col-lg-2 col-md-2">${mainStatusFinalDiagnosis}</div>
                                        <div class="align-self-center table-date col-lg-1 col-md-1">${mainStatusTherapy}</div>
                                    </c:when>
                                    <c:when test="${therapy.finalDiagnosis.orElse(null) != null &&
                                        therapy.endTherapy.orElse(null) == null}">
                                        <div class="align-self-center diagnosis-code col-lg-1 col-md-1">${therapy.finalDiagnosis.get().icd.code}</div>
                                        <div class="align-self-center diagnosis-title col-lg-2 col-md-2">${therapy.finalDiagnosis.get().icd.title}</div>
                                        <div class="align-self-center table-date col-lg-1 col-md-1">${mainStatusTherapy}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="align-self-center diagnosis-code col-lg-1 col-md-1">${therapy.finalDiagnosis.get().icd.code}</div>
                                        <div class="align-self-center diagnosis-title col-lg-2 col-md-2">${therapy.finalDiagnosis.get().icd.title}</div>
                                        <div class="align-self-center table-date col-lg-1 col-md-1">${therapy.endTherapy}</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="table-row-border-solid"></div>
                            <div class="table-head col-lg-12 col-md-12">
                                <div class="diagnosis-preliminary">preliminary diagnoses</div>
                            </div>
                            <div class="table-row">
                                <div class="table-row-wrapper col-lg-12 col-md-12">
                                    <div class="table-row">
                                        <div class="table-group col-lg-12 col-md-12">
                                            <div class="table-parameter col-lg-1 col-md-1">${innerHeadDoctor}</div>
                                            <div class="table-parameter col-lg-1 col-md-1">${innerHeadIcd}</div>
                                            <div class="table-parameter col-lg-2 col-md-2">${innerHeadDiagnosis}</div>
                                            <div class="table-parameter col-lg-1 col-md-1">${innerHeadDate}</div>
                                            <div class="table-parameter col-lg-4 col-md-4">${innerHeadReason}</div>
                                            <div class="table-parameter col-lg-3 col-md-3"></div>
                                        </div>
                                    </div>
                                    <div class="table-row-border col-lg-9 col-md-9"></div>
                                    <c:forEach items="${therapy.diagnoses}" var="diagnosis" varStatus="loop">
                                        <div class="table-row">
                                            <div class="table-group col-lg-12 col-md-12">
                                                <div class="align-self-center col-lg-1 col-md-1">
                                                        ${diagnosis.doctor.userDetails.firstName} ${diagnosis.doctor.userDetails.surname} ${diagnosis.doctor.userDetails.lastName}
                                                </div>
                                                <div class="align-self-center col-lg-1 col-md-1">${diagnosis.icd.code}</div>
                                                <div class="align-self-center col-lg-2 col-md-2">${diagnosis.icd.title}</div>
                                                <div class="align-self-center col-lg-1 col-md-1">${diagnosis.diagnosisDate}</div>
                                                <div class="align-self-center col-lg-4 col-md-4">${diagnosis.reason}</div>
                                                <div class="align-self-center col-lg-3 col-md-3 d-flex justify-content-center">
                                                    <form class="col-lg-6 col-md-6" method="post" action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_FIND_ASSIGNMENT_MEDICATIONS}">
                                                        <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.FIND_ASSIGNMENT_MEDICATIONS}">
                                                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}" value="${HospitalUrl.PAGE_DOCTOR_THERAPIES}">
                                                        <input type="hidden" name="${ParameterName.DIAGNOSIS}" value="${JsonConverter.makeJsonValidForHtml(diagnosis.toString())}">
                                                        <input type="hidden" name="${ParameterName.PATIENT}" value="${JsonConverter.makeJsonValidForHtml(therapy.patient.toString())}">
                                                        <button type="submit" class="template-btn">
                                                            Show assigned medications
                                                        </button>
                                                    </form>
<%--                                                    todo diagnosis-3 reason-3 on head also 3 and for buttons 4--%>
                                                    <form class="col-lg-6 col-md-6" method="post" action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_FIND_ASSIGNMENT_PROCEDURES}">
                                                        <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.FIND_ASSIGNMENT_PROCEDURES}">
                                                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}" value="${HospitalUrl.PAGE_DOCTOR_THERAPIES}">
                                                        <input type="hidden" name="${ParameterName.DIAGNOSIS}" value="${JsonConverter.makeJsonValidForHtml(diagnosis.toString())}">
                                                        <input type="hidden" name="${ParameterName.PATIENT}" value="${JsonConverter.makeJsonValidForHtml(therapy.patient.toString())}">
                                                        <button type="submit" class="template-btn">
                                                            Show assigned procedures
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="table-row-border col-lg-9 col-md-9"></div>
                                    </c:forEach>
                                    <div class="table-row">
                                        <div class="table-group">
                                            <form method="post" action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_MAKE_LAST_DIAGNOSIS_FINAL}">
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
                                                <button type="submit" class="template-btn">
                                                        ${btnMakeLastFinal}
                                                </button>
                                            </form>
                                            <form method="post" action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_CLOSE_THERAPY}">
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
                                                <button type="submit" class="template-btn">
                                                        ${btnDischargePatient}
                                                </button>
                                            </form>
                                            <form method="post" action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_DOCTOR_FIND_MEDICATIONS_PAGING}">
                                                <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.DOCTOR_FIND_MEDICATIONS_PAGING}">
                                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}" value="${HospitalUrl.PAGE_DOCTOR_THERAPIES}">
                                                <input type="hidden" name="${ParameterName.PATIENT}" value="${JsonConverter.makeJsonValidForHtml(therapy.patient.toString())}">
                                                <input type="hidden" name="${ParameterName.CARD_TYPE}" value="<%=request.getParameter(ParameterName.CARD_TYPE)%>">
                                                <button type="submit" class="template-btn">
                                                    Assign medicament to last diagnosis
                                                </button>
                                            </form>
                                            <form method="post" action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.COMMAND_DOCTOR_FIND_PROCEDURES_PAGING}">
                                                <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.DOCTOR_FIND_PROCEDURES_PAGING}">
                                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}" value="${HospitalUrl.PAGE_DOCTOR_THERAPIES}">
                                                <input type="hidden" name="${ParameterName.PATIENT}" value="${JsonConverter.makeJsonValidForHtml(therapy.patient.toString())}">
                                                <input type="hidden" name="${ParameterName.CARD_TYPE}" value="<%=request.getParameter(ParameterName.CARD_TYPE)%>">
                                                <button type="submit" class="template-btn">
                                                    Assign procedure to last diagnosis
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="table-row-border-solid"></div>
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
