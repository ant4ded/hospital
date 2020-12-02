<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.UserDetails.Gender" %>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName" %>
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

<fmt:message bundle="${local}" key="page.registry" var="page"/>
<fmt:message bundle="${local}" key="registry.title" var="title"/>
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
                    <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_REGISTER_CLIENT}">
                        <p>${requestScope.message}</p>
                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                               value="${HospitalUrl.PAGE_REGISTRY}">
                        <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.REGISTER_CLIENT}">
                        <div class="form-group form-inline">
                            <div class="form-group col-lg-6 col-md-6 name">
                                <input type="text" name="${UsersFieldName.LOGIN}" required
                                       class="form-control" placeholder="${inputLogin}"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputLogin}'">
                            </div>
                            <div class="form-group col-lg-6 col-md-6 email">
                                <input type="password" name="${UsersFieldName.PASSWORD}" required
                                       class="form-control" placeholder="${inputPassword}"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputPassword}'">
                            </div>
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.FIRST_NAME}" required
                                   class="form-control" placeholder="${inputFirstName}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputFirstName}'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.SURNAME}" required
                                   class="form-control" placeholder="${inputSurname}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputSurname}'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.LAST_NAME}" required
                                   class="form-control" placeholder="${inputLastName}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputLastName}'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.PASSPORT_ID}" required
                                   class="form-control" placeholder="${inputPassportId}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputPassportId}'">
                        </div>
                        <div class="form-group form-inline justify-content-between">
                            <div class="form-group">
                                <input type="date" name="${UsersDetailsFieldName.BIRTHDAY}" required
                                       class="form-control">
                            </div>
                            <div class="switch-wrap d-flex justify-content-between">
                                <p class="radio-p">${UsersDetailsFieldName.GENDER_MALE}</p>
                                <div class="confirm-radio">
                                    <input type="radio" id="confirm-radio" name="${UsersDetailsFieldName.GENDER}"
                                           value="${UsersDetailsFieldName.GENDER_MALE}">
                                    <label for="confirm-radio"></label>
                                </div>
                            </div>
                            <div class="switch-wrap d-flex justify-content-between">
                                <p class="radio-p">${UsersDetailsFieldName.GENDER_FEMALE}</p>
                                <div class="primary-radio">
                                    <input type="radio" id="primary-radio" name="${UsersDetailsFieldName.GENDER}"
                                           value="${UsersDetailsFieldName.GENDER_FEMALE}">
                                    <label for="primary-radio"></label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.ADDRESS}" required
                                   class="form-control" placeholder="${inputAddress}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputAddress}'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.PHONE}" required
                                   class="form-control" placeholder="${inputPhone}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = '${inputPhone}'">
                        </div>
                        <button type="submit" class="template-btn form-btn">${btnSubmit}</button>
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
