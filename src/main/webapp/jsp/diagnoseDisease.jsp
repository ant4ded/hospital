<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.CardType" %>
<%@page import="by.epam.hospital.entity.table.DiagnosesFieldName" %>
<%@page import="by.epam.hospital.entity.table.IcdFieldName" %>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale" var="local"/>

<fmt:message bundle="${local}" key="page.diagnose_disease" var="page"/>
<fmt:message bundle="${local}" key="diagnose_disease.title" var="title"/>
<fmt:message bundle="${local}" key="diagnose_disease.btn.diagnose" var="btnDiagnose"/>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
                    <form method="post"
                          action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_DIAGNOSE_DISEASE}">
                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                               value="${HospitalUrl.PAGE_DIAGNOSE_DISEASE}">
                        <input type="hidden" name="${ParameterName.COMMAND}"
                               value="${CommandName.DIAGNOSE_DISEASE}">
                        <div class="form-group form-inline justify-content-between">
                            <div class="form-group">
                                <input type="text" name="${UsersDetailsFieldName.FIRST_NAME}" placeholder="${inputFirstName}"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = '${inputFirstName}'" required class="form-control">
                            </div>
                            <div class="form-group">
                                <input type="text" name="${UsersDetailsFieldName.SURNAME}" placeholder="${inputSurname}"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = '${inputSurname}'" required class="form-control">
                            </div>
                            <div class="form-group">
                                <input type="text" name="${UsersDetailsFieldName.LAST_NAME}" placeholder="${inputLastName}"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = '${inputLastName}'" required class="form-control">
                            </div>
                            <div class="form-group">
                                <input type="date" name="${UsersDetailsFieldName.BIRTHDAY}" required
                                       class="form-control">
                            </div>
                        </div>
                        <div class="form-group form-inline justify-content-between">
                            <div class="form-group">
                                <input type="text" name="${IcdFieldName.CODE}" placeholder="${inputIcdCode}"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = '${inputIcdCode}'" required class="form-control">
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
                        <div class="form-group">
                            <textarea class="single-textarea" name="${DiagnosesFieldName.REASON}"
                                      placeholder="${inputReason}" required onfocus="this.placeholder = ''"
                                      onblur="this.placeholder = '${inputReason}'"></textarea>
                        </div>
                        <button type="submit" class="template-btn form-btn">${btnDiagnose}</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>
<!--================Blog Area =================-->
<%@include file="component/footer.jsp" %>
</body>
</html>
