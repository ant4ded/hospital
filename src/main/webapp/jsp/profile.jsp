<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
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
                    <h4>Profile details</h4>
                    <p>${requestScope.message}</p>
                    <form method="post"
                          action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_EDIT_USER_DETAILS}">
                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                               value="${HospitalUrl.PAGE_PROFILE}">
                        <input type="hidden" name="${ParameterName.COMMAND}"
                               value="${CommandName.EDIT_USER_DETAILS}">
                        <div class="form-group form-inline">
                            <div class="form-group col-lg-6 col-md-6 name">
                                <input type="text" name="${UsersFieldName.LOGIN}" disabled
                                       class="form-control disable-input" placeholder="Login"
                                       value="${sessionScope.get(ParameterName.LOGIN_USERNAME)}"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-6 col-md-6 email">
                                <input type="password" name="${UsersFieldName.PASSWORD}" disabled
                                       class="form-control disable-input" placeholder="Password"
                                       value="${requestScope.get(UsersFieldName.PASSWORD)}"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Password'">
                            </div>
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.FIRST_NAME}" disabled
                                   class="form-control disable-input" placeholder="First name"
                                   value="${requestScope.get(UsersDetailsFieldName.FIRST_NAME)}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'First name'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.SURNAME}" disabled
                                   class="form-control disable-input" placeholder="Surname"
                                   value="${requestScope.get(UsersDetailsFieldName.SURNAME)}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Surname'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.LAST_NAME}" disabled
                                   class="form-control disable-input" placeholder="Last name"
                                   value="${requestScope.get(UsersDetailsFieldName.LAST_NAME)}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Last name'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.PASSPORT_ID}" disabled
                                   class="form-control disable-input" placeholder="Passport id"
                                   value="${requestScope.get(UsersDetailsFieldName.PASSPORT_ID)}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Passport id'">
                        </div>
                        <div class="form-group form-inline justify-content-between">
                            <div class="form-group">
                                <input type="date" name="${UsersDetailsFieldName.BIRTHDAY}" disabled
                                       class="form-control disable-input" placeholder="Birthday"
                                       value="${requestScope.get(UsersDetailsFieldName.BIRTHDAY)}"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Birthday'">
                            </div>
                            <div class="switch-wrap d-flex justify-content-between">
                                <p class="radio-p">${requestScope.get(UsersDetailsFieldName.GENDER)}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.ADDRESS}" required
                                   class="form-control" placeholder="Address"
                                   value="${requestScope.get(UsersDetailsFieldName.ADDRESS)}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Address'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.PHONE}" required
                                   class="form-control" placeholder="Phone"
                                   value="${requestScope.get(UsersDetailsFieldName.PHONE)}"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Phone'">
                        </div>
                        <button type="submit" class="template-btn form-btn">Update</button>
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
