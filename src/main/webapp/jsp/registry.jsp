<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="by.epam.hospital.controller.HospitalUrl"%>
<%@page import="by.epam.hospital.entity.table.UsersFieldName"%>
<%@page import="by.epam.hospital.entity.UserDetails"%>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Medino</title>
    <%@include file="component/head.jsp"%>
</head>
<body>
<%@include file="component/navbar.jsp"%>

<!-- Banner Area Starts -->
<section class="banner-area other-page">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <h1>Blog Details</h1>
                <a href="${HospitalUrl.MAIN_URL}">Home</a> <span>|</span> <a href="#">Blog Details</a>
            </div>
        </div>
    </div>
</section>
<!-- Banner Area End -->

<!--================Blog Area =================-->
<section class="blog_area section-padding">
    <div class="container">
        <div class="row">
            <div class="col-lg-8 posts-list">
                <div class="comment-form">
                    <h4>Register new client</h4>
                    <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_TEST}">
                        <p>${requestScope.message}</p>
                        <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.REGISTER_CLIENT}">
                        <div class="form-group form-inline">
                            <div class="form-group col-lg-6 col-md-6 name">
                                <input type="text" name="${UsersFieldName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-6 col-md-6 email">
                                <input type="password" name="${UsersFieldName.PASSWORD}" required
                                       class="form-control" placeholder="Password"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Password'">
                            </div>
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.FIRST_NAME}" required
                                   class="form-control" placeholder="First name"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'First name'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.SURNAME}" required
                                   class="form-control" placeholder="Surname"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Surname'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.LAST_NAME}" required
                                   class="form-control" placeholder="Last name"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Last name'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.PASSPORT_ID}" required
                                   class="form-control" placeholder="Passport id"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Passport id'">
                        </div>
                        <div class="form-group form-inline justify-content-between">
                            <div class="form-group">
                                <input type="date" name="${UsersDetailsFieldName.BIRTHDAY}" required
                                       class="form-control" placeholder="Birthday"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Birthday'">
                            </div>
                            <div class="switch-wrap d-flex justify-content-between">
                                <p class="radio-p">Male</p>
                                <div class="confirm-radio">
                                    <input type="radio" id="confirm-radio" name="${UsersDetailsFieldName.GENDER}"
                                           value="${UsersDetailsFieldName.GENDER_MALE}">
                                    <label for="confirm-radio"></label>
                                </div>
                            </div>
                            <div class="switch-wrap d-flex justify-content-between">
                                <p class="radio-p">Female</p>
                                <div class="primary-radio">
                                    <input type="radio" id="primary-radio" name="${UsersDetailsFieldName.GENDER}"
                                           value="${UsersDetailsFieldName.GENDER_FEMALE}">
                                    <label for="primary-radio"></label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.ADDRESS}" required
                                   class="form-control" placeholder="Address"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Address'">
                        </div>
                        <div class="form-group">
                            <input type="text" name="${UsersDetailsFieldName.PHONE}" required
                                   class="form-control" placeholder="Phone"
                                   onfocus="this.placeholder = ''" onblur="this.placeholder = 'Phone'">
                        </div>
                        <button type="submit" class="template-btn">Submit</button>
                    </form>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="blog_right_sidebar">
                    <aside class="single_sidebar_widget post_category_widget">
                        <h4 class="widget_title">Post Catgories</h4>
                        <ul class="list cat-list">
                            <li>
                                <a href="#" class="d-flex justify-content-between">
                                    <p>Technology</p>
                                    <p>37</p>
                                </a>
                            </li>
                            <li>
                                <a href="#" class="d-flex justify-content-between">
                                    <p>Lifestyle</p>
                                    <p>24</p>
                                </a>
                            </li>
                            <li>
                                <a href="#" class="d-flex justify-content-between">
                                    <p>Fashion</p>
                                    <p>59</p>
                                </a>
                            </li>
                            <li>
                                <a href="#" class="d-flex justify-content-between">
                                    <p>Art</p>
                                    <p>29</p>
                                </a>
                            </li>
                            <li>
                                <a href="#" class="d-flex justify-content-between">
                                    <p>Food</p>
                                    <p>15</p>
                                </a>
                            </li>
                            <li>
                                <a href="#" class="d-flex justify-content-between">
                                    <p>Architecture</p>
                                    <p>09</p>
                                </a>
                            </li>
                            <li>
                                <a href="#" class="d-flex justify-content-between">
                                    <p>Adventure</p>
                                    <p>44</p>
                                </a>
                            </li>
                        </ul>
                        <div class="br"></div>
                    </aside>
                </div>
            </div>
        </div>
    </div>
</section>
<!--================Blog Area =================-->
<%@ include file="component/footer.jsp" %>
</body>
</body>
</html>

