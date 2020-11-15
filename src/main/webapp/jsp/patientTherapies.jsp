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
                                    <c:when test="${therapy.finalDiagnosis.orElse(null) != null && therapy.endTherapy.orElse(null) != null}">
                                        <div class="diagnosis-code">${therapy.finalDiagnosis.get().icd.code}</div>
                                        <div class="diagnosis-title">${therapy.finalDiagnosis.get().icd.title}</div>
                                        <div class="table-date">${therapy.endTherapy.get()}</div>
                                    </c:when>
                                    <c:when test="${therapy.finalDiagnosis.orElse(null) != null && therapy.endTherapy.orElse(null) == null}">
                                        <div class="diagnosis-code">${therapy.finalDiagnosis.get().icd.code}</div>
                                        <div class="diagnosis-title">${therapy.finalDiagnosis.get().icd.title}</div>
                                        <div class="table-date">Open</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="diagnosis-code">Status:</div>
                                        <div class="diagnosis-title">In the process of identification</div>
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
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <section class="blog_area section-padding">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 posts-list">
                        <div class="comment-form">
                            <h4>${title}</h4>
                            <p>${requestScope.message}</p>
                            <form method="post"
                                  action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_FIND_PATIENT_THERAPIES}">
                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                                       value="${HospitalUrl.PAGE_PATIENT_THERAPIES}">
                                <input type="hidden" name="${ParameterName.COMMAND}"
                                       value="${CommandName.FIND_PATIENT_THERAPIES}">
                                <div class="form-group form-inline justify-content-between">
                                    <div class="form-group">
                                        <input type="text" name="${UsersDetailsFieldName.FIRST_NAME}"
                                               placeholder="${inputFirstName}"
                                               onfocus="this.placeholder = ''"
                                               onblur="this.placeholder = '${inputFirstName}'" required
                                               class="form-control">
                                    </div>
                                    <div class="form-group">
                                        <input type="text" name="${UsersDetailsFieldName.SURNAME}"
                                               placeholder="${inputSurname}"
                                               onfocus="this.placeholder = ''"
                                               onblur="this.placeholder = '${inputSurname}'" required
                                               class="form-control">
                                    </div>
                                    <div class="form-group">
                                        <input type="text" name="${UsersDetailsFieldName.LAST_NAME}"
                                               placeholder="${inputLastName}"
                                               onfocus="this.placeholder = ''"
                                               onblur="this.placeholder = '${inputLastName}'" required
                                               class="form-control">
                                    </div>
                                </div>
                                <div class="form-group form-inline justify-content-between">
                                    <div class="form-group">
                                        <div class="form-group">
                                            <input type="date" name="${UsersDetailsFieldName.BIRTHDAY}" required
                                                   class="form-control">
                                        </div>
                                    </div>
                                    <div class="switch-wrap d-flex justify-content-between">
                                        <p class="radio-p">${inputAmbulatory}</p>
                                        <div class="confirm-radio">
                                            <input type="radio" id="confirm-radio" name="${ParameterName.CARD_TYPE}"
                                                   value="${CardType.AMBULATORY}">
                                            <label for="confirm-radio"></label>
                                        </div>
                                    </div>
                                    <div class="switch-wrap d-flex justify-content-between">
                                        <p class="radio-p">${inputStationary}</p>
                                        <div class="primary-radio">
                                            <input type="radio" id="primary-radio" name="${ParameterName.CARD_TYPE}"
                                                   value="${CardType.STATIONARY}">
                                            <label for="primary-radio"></label>
                                        </div>
                                    </div>
                                </div>
                                <button type="submit" class="template-btn form-btn">${btnFind}</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </c:otherwise>
</c:choose>
<!--================Blog Area =================-->
<%@include file="component/footer.jsp" %>
</body>
</html>
