<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.table.DiagnosesFieldName" %>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName" %>
<%@page import="by.epam.hospital.entity.table.IcdFieldName" %>
<%@page import="by.epam.hospital.entity.CardType" %>
<!DOCTYPE html>
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
                <h1>Role control</h1>
                <a href="${HospitalUrl.MAIN_URL}">Home</a> <span>|</span> <a href="#">Role control</a>
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
                    <h4>Diagnose disease panel</h4>
                    <p>${requestScope.message}</p>
                    <form method="post"
                          action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_DIAGNOSE_DISEASE}">
                                                <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                                                       value="${HospitalUrl.PAGE_DIAGNOSE_DISEASE}">
                                                <input type="hidden" name="${ParameterName.COMMAND}"
                                                       value="${CommandName.DIAGNOSE_DISEASE}">
                        <div class="form-group form-inline justify-content-between">
                            <div class="form-group">
                                <input type="text" name="${UsersDetailsFieldName.FIRST_NAME}" placeholder="First name"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = 'First name'" required class="form-control">
                            </div>
                            <div class="form-group">
                                <input type="text" name="${UsersDetailsFieldName.SURNAME}" placeholder="Surname"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = 'Surname'" required class="form-control">
                            </div>
                            <div class="form-group">
                                <input type="text" name="${UsersDetailsFieldName.LAST_NAME}" placeholder="Last name"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = 'Last name'" required class="form-control">
                            </div>
                            <div class="form-group">
                                <input type="date" name="${UsersDetailsFieldName.BIRTHDAY}" required
                                       class="form-control" placeholder="Birthday"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Birthday'">
                            </div>
                        </div>
                        <div class="form-group form-inline justify-content-between">
                            <div class="form-group">
                                <input type="text" name="${IcdFieldName.CODE}" placeholder="Icd code"
                                       onfocus="this.placeholder = ''"
                                       onblur="this.placeholder = 'Icd code'" required class="form-control">
                            </div>
                            <div class="switch-wrap d-flex justify-content-between">
                                <p class="radio-p">Ambulatory</p>
                                <div class="confirm-radio">
                                    <input type="radio" id="confirm-radio" name="${ParameterName.CARD_TYPE}"
                                           value="${CardType.AMBULATORY}">
                                    <label for="confirm-radio"></label>
                                </div>
                            </div>
                            <div class="switch-wrap d-flex justify-content-between">
                                <p class="radio-p">Stationary</p>
                                <div class="primary-radio">
                                    <input type="radio" id="primary-radio" name="${ParameterName.CARD_TYPE}"
                                           value="${CardType.STATIONARY}">
                                    <label for="primary-radio"></label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <textarea class="single-textarea" name="${DiagnosesFieldName.REASON}"
                                      placeholder="Reason" required onfocus="this.placeholder = ''"
                                      onblur="this.placeholder = 'Reason'"></textarea>
                        </div>
                        <button type="submit" class="template-btn form-btn">Diagnose</button>
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
