<%--suppress HtmlFormInputWithoutLabel --%>
<%@page import="by.epam.hospital.controller.CommandName" %>
<%@page import="by.epam.hospital.controller.HospitalUrl" %>
<%@page import="by.epam.hospital.controller.ParameterName" %>
<%@page import="by.epam.hospital.entity.Role" %>
<%@page import="by.epam.hospital.entity.CardType" %>
<%@page import="by.epam.hospital.entity.table.UsersFieldName" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <title></title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale" var="local"/>

<fmt:message bundle="${local}" key="nav.home" var="home"/>
<fmt:message bundle="${local}" key="nav.departments" var="departments"/>
<fmt:message bundle="${local}" key="nav.doctors" var="doctors"/>
<fmt:message bundle="${local}" key="nav.contact" var="contact"/>
<fmt:message bundle="${local}" key="nav.sign_in" var="signIn"/>
<fmt:message bundle="${local}" key="nav.client.profile" var="profile"/>
<fmt:message bundle="${local}" key="nav.client.sign_out" var="signOut"/>
<fmt:message bundle="${local}" key="nav.receptionist.register_new_client" var="registerNewClient"/>
<fmt:message bundle="${local}" key="nav.receptionist.find_user_credentials" var="findUserCredentials"/>
<fmt:message bundle="${local}" key="nav.doctor.diagnose_disease" var="diagnoseDisease"/>
<fmt:message bundle="${local}" key="nav.admin.head.department_control" var="departmentControl"/>
<fmt:message bundle="${local}" key="nav.admin.head.role_control" var="roleControl"/>
<fmt:message bundle="${local}" key="authorization.title" var="authorizationTitle"/>

<fmt:message bundle="${local}" key="input.login" var="inputLogin"/>
<fmt:message bundle="${local}" key="input.password" var="inputPassword"/>
<fmt:message bundle="${local}" key="input.first_name" var="inputFirstName"/>
<fmt:message bundle="${local}" key="input.surname" var="inputSurname"/>
<fmt:message bundle="${local}" key="input.last_name" var="inputLastName"/>
<fmt:message bundle="${local}" key="input.icd_code" var="inputIcdCode"/>
<fmt:message bundle="${local}" key="input.reason" var="inputReason"/>
<fmt:message bundle="${local}" key="input.passport_id" var="inputPassportId"/>
<fmt:message bundle="${local}" key="input.address" var="inputAddress"/>
<fmt:message bundle="${local}" key="input.phone" var="inputPhone"/>
<fmt:message bundle="${local}" key="radio.card_type.ambulatory" var="inputAmbulatory"/>
<fmt:message bundle="${local}" key="radio.card_type.stationary" var="inputStationary"/>

<fmt:message bundle="${local}" key="therapies.table.main.head.patient" var="mainHeadPatient"/>
<fmt:message bundle="${local}" key="therapies.table.main.head.icd" var="mainHeadIcd"/>
<fmt:message bundle="${local}" key="therapies.table.main.head.final_diagnosis" var="mainHeadFinalDiagnosis"/>
<fmt:message bundle="${local}" key="therapies.table.main.head.therapy_end_date" var="mainHeadEndTherapy"/>
<fmt:message bundle="${local}" key="therapies.table.main.status" var="mainStatus"/>
<fmt:message bundle="${local}" key="therapies.table.main.status.final_diagnosis" var="mainStatusFinalDiagnosis"/>
<fmt:message bundle="${local}" key="therapies.table.main.status.therapy" var="mainStatusTherapy"/>
<fmt:message bundle="${local}" key="therapies.table.inner.head.doctor" var="innerHeadDoctor"/>
<fmt:message bundle="${local}" key="therapies.table.inner.head.icd" var="innerHeadIcd"/>
<fmt:message bundle="${local}" key="therapies.table.inner.head.diagnosis" var="innerHeadDiagnosis"/>
<fmt:message bundle="${local}" key="therapies.table.inner.head.date" var="innerHeadDate"/>
<fmt:message bundle="${local}" key="therapies.table.inner.head.reason" var="innerHeadReason"/>

<fmt:message bundle="${local}" key="btn.submit" var="btnSubmit"/>
<fmt:message bundle="${local}" key="btn.find" var="btnFind"/>
<fmt:message bundle="${local}" key="btn.update" var="btnUpdate"/>
<fmt:message bundle="${local}" key="btn.add" var="btnAdd"/>
<fmt:message bundle="${local}" key="btn.remove" var="btnRemove"/>

<!-- Preloader Starts -->
<div class="preloader">
    <div class="spinner"></div>
</div>
<!-- Preloader End -->

<!-- Sign In -->
<div class="login-form-flex">
    <div id="login" class="login-form">
        <h4>${authorizationTitle}</h4>
        <form method="post" action="${HospitalUrl.APP_NAME_URL}${HospitalUrl.SERVLET_MAIN}">
            <div class="form-group">
                <p>${requestScope.message}</p>
                <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.AUTHORIZATION}">
                <input type="text" name="${UsersFieldName.LOGIN}" placeholder="${inputLogin}"
                       onfocus="this.placeholder = ''"
                       onblur="this.placeholder = '${inputLogin}'" required class="single-input">
                <input type="password" name="${UsersFieldName.PASSWORD}" placeholder="${inputPassword}"
                       onfocus="this.placeholder = ''"
                       onblur="this.placeholder = '${inputPassword}'" required class="single-input">
            </div>
            <button type="submit" class="template-btn form-btn">${btnSubmit}</button>
        </form>
    </div>
</div>

<!-- Header Area Starts -->
<header class="header-area">
    <div class="header-top">
        <div class="container">
            <div class="row">
                <div class="col-lg-9 d-md-flex"></div>
                <div class="col-lg-3">
                    <div class="social-links">
                        <ul>
                            <li>
                                <a href="?${ParameterName.LOCALE_SESSION}=${ParameterName.LOCALE_EN}">
                                    <i class="fa fa-cog"></i>EN
                                </a>
                            </li>
                            <li>
                                <a href="?${ParameterName.LOCALE_SESSION}=${ParameterName.LOCALE_DE}">
                                    <i class="fa fa-cog"></i>DE
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="header" id="home">
        <div class="container">
            <div class="row align-items-center justify-content-between d-flex">
                <div id="logo">
                    <a href="#"><img src="images/logo/logo.png" alt="" title=""/></a>
                </div>
                <nav id="nav-menu-container">
                    <ul class="nav-menu">
                        <li class="menu-active"><a href="${HospitalUrl.MAIN_URL}">${home}</a></li>
                        <c:if test="${sessionScope.loginUsername != null}">
                            <li class="menu-has-children"><a href="">${sessionScope.loginUsername}</a>
                                <ul>
                                        <%--Role.RECEPTIONIST--%>
                                    <c:if test="${sessionScope.loginRoles.contains(Role.RECEPTIONIST)}">
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_REGISTRY}">
                                                    ${registerNewClient}
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_USER_CREDENTIALS}">
                                                    ${findUserCredentials}
                                            </a>
                                        </li>
                                    </c:if>
                                        <%--Role.DOCTOR--%>
                                    <c:if test="${sessionScope.loginRoles.contains(Role.DOCTOR)}">
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_DIAGNOSE_DISEASE}">
                                                    ${diagnoseDisease}
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_FIND_OPEN_DOCTOR_THERAPIES}?${ParameterName.COMMAND}=${CommandName.FIND_OPEN_DOCTOR_THERAPIES}&${ParameterName.CARD_TYPE}=${CardType.AMBULATORY}">
                                                    ambulatory therapies
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_FIND_OPEN_DOCTOR_THERAPIES}?${ParameterName.COMMAND}=${CommandName.FIND_OPEN_DOCTOR_THERAPIES}&${ParameterName.CARD_TYPE}=${CardType.STATIONARY}">
                                                    stationary therapies
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_PATIENT_THERAPIES}">
                                                    all patient therapies
                                            </a>
                                        </li>
                                    </c:if>
                                        <%--Role.ADMIN_HEAD--%>
                                    <c:if test="${sessionScope.loginRoles.contains(Role.ADMIN_HEAD)}">
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_DEPARTMENT_CONTROL}">
                                                    ${departmentControl}
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_ROLE_CONTROL}">
                                                    ${roleControl}
                                            </a>
                                        </li>
                                    </c:if>
                                    <li>
                                        <a href="${HospitalUrl.MAIN_URL}?${ParameterName.COMMAND}=${CommandName.FIND_USER_DETAILS}">
                                                ${profile}
                                        </a>
                                    </li>
                                    <li>
                                        <a href="${HospitalUrl.MAIN_URL}?${ParameterName.COMMAND}=${CommandName.SIGN_OUT}">
                                                ${signOut}
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.loginUsername == null}">
                            <li id="login-btn"><a href="#">${signIn}</a></li>
                        </c:if>
                    </ul>
                </nav>
                <!-- #nav-menu-container -->
            </div>
        </div>
    </div>
</header>
<!-- Header Area End -->
</body>
</html>
