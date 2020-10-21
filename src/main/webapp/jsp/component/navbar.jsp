<%--suppress HtmlFormInputWithoutLabel --%>
<%@page import="by.epam.hospital.controller.CommandName" %>
<%@page import="by.epam.hospital.controller.HospitalUrl" %>
<%@page import="by.epam.hospital.controller.ParameterName" %>
<%@page import="by.epam.hospital.entity.Role" %>
<%@page import="by.epam.hospital.entity.table.UsersFieldName" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title></title>
</head>
<body>

<!-- Preloader Starts -->
<div class="preloader">
    <div class="spinner"></div>
</div>
<!-- Preloader End -->

<!-- Sign In -->
<div class="login-form-flex">
    <div id="login" class="login-form">
        <h4>Login</h4>
        <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
            <div class="form-group">
                <p>${requestScope.message}</p>
                <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.AUTHORIZATION}">
                <input type="text" name="${UsersFieldName.LOGIN}" placeholder="Login"
                       onfocus="this.placeholder = ''"
                       onblur="this.placeholder = 'Login'" required class="single-input">
                <input type="password" name="${UsersFieldName.PASSWORD}" placeholder="Password"
                       onfocus="this.placeholder = ''"
                       onblur="this.placeholder = 'Password'" required class="single-input">
            </div>
            <button type="submit" class="template-btn form-btn">Submit</button>
        </form>
    </div>
</div>

<!-- Header Area Starts -->
<header class="header-area">
    <div class="header-top">
        <div class="container">
            <div class="row">
                <div class="col-lg-9 d-md-flex">
                    <h6 class="mr-3"><span class="mr-2"><i class="fa fa-mobile"></i></span> call us now! +1 305 708 2563
                    </h6>
                    <h6 class="mr-3"><span class="mr-2"><i class="fa fa-envelope-o"></i></span> medical@example.com</h6>
                    <h6><span class="mr-2"><i class="fa fa-map-marker"></i></span> Find our Location</h6>
                </div>
                <div class="col-lg-3">
                    <div class="social-links">
                        <ul>
                            <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                            <li><a href="#"><i class="fa fa-linkedin"></i></a></li>
                            <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                            <li><a href="#"><i class="fa fa-instagram"></i></a></li>
                            <li><a href="#"><i class="fa fa-vimeo"></i></a></li>
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
                        <li class="menu-active"><a href="#">Home</a></li>
                        <li><a href="#">departments</a></li>
                        <li><a href="#">doctors</a></li>
                        <li class="menu-has-children"><a href="">Pages</a>
                            <ul>
                                <li><a href="#">about us</a></li>
                                <li><a href="#">elements</a></li>
                            </ul>
                        </li>
                        <li><a href="#">Contact</a></li>
                        <c:if test="${sessionScope.loginUsername != null}">
                            <li class="menu-has-children"><a href="">${sessionScope.loginUsername}</a>
                                <ul>
                                        <%--Role.RECEPTIONIST--%>
                                    <c:if test="${sessionScope.loginRoles.contains(Role.RECEPTIONIST)}">
                                        <li><a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_REGISTRY}">
                                            Register new client
                                        </a></li>
                                    </c:if>
                                        <%--Role.ADMIN_HEAD--%>
                                    <c:if test="${sessionScope.loginRoles.contains(Role.ADMIN_HEAD)}">
                                        <li><a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_DEPARTMENT_CONTROL}">
                                            Department control
                                        </a></li>
                                        <li><a href="${HospitalUrl.MAIN_URL}${HospitalUrl.PAGE_ROLE_CONTROL}">
                                            Role control
                                        </a></li>
                                    </c:if>
                                    <li>
                                        <a href="${HospitalUrl.MAIN_URL}?${ParameterName.COMMAND}=${CommandName.SIGN_OUT}">
                                            sign out
                                        </a>
                                    </li>
                                    <li><a href="#">settings</a></li>
                                </ul>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.loginUsername == null}">
                            <li id="login-btn"><a href="#">Sign in</a></li>
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
