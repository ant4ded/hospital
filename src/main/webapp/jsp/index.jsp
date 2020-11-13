<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.Department" %>
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

<fmt:message bundle="${local}" key="index.title.h4" var="titleH4"/>
<fmt:message bundle="${local}" key="index.title.h1" var="titleH1"/>
<fmt:message bundle="${local}" key="index.title.p" var="titleP"/>
<fmt:message bundle="${local}" key="index.feature1.title" var="featureTitle1"/>
<fmt:message bundle="${local}" key="index.feature2.title" var="featureTitle2"/>
<fmt:message bundle="${local}" key="index.feature3.title" var="featureTitle3"/>
<fmt:message bundle="${local}" key="index.feature4.title" var="featureTitle4"/>
<fmt:message bundle="${local}" key="index.feature1.description" var="featureDescription1"/>
<fmt:message bundle="${local}" key="index.feature2.description" var="featureDescription2"/>
<fmt:message bundle="${local}" key="index.feature3.description" var="featureDescription3"/>
<fmt:message bundle="${local}" key="index.feature4.description" var="featureDescription4"/>
<fmt:message bundle="${local}" key="index.welcome.title" var="welcomeTitle"/>
<fmt:message bundle="${local}" key="index.welcome.description1" var="welcomeDescription1"/>
<fmt:message bundle="${local}" key="index.welcome.description2" var="welcomeDescription2"/>
<fmt:message bundle="${local}" key="index.departments.title" var="departmentsTitle"/>
<fmt:message bundle="${local}" key="index.departments.description" var="departmentsDescription"/>
<fmt:message bundle="${local}" key="index.hotline.title" var="hotlineTitle"/>
<fmt:message bundle="${local}" key="index.hotline.description" var="hotlineDescription"/>


<%-- Banner Area Starts --%>
<section class="banner-area">
    <div class="container">
        <div class="row">
            <div class="col-lg-6">
                <h4>${titleH4}</h4>
                <h1>${titleH1}</h1>
                <p>${titleP}</p>
            </div>
        </div>
    </div>
</section>
<%-- Banner Area End --%>

<%-- Feature Area Starts --%>
<section class="feature-area section-padding">
    <div class="container">
        <div class="row">
            <div class="col-lg-3 col-md-6">
                <div class="single-feature text-center item-padding">
                    <img src="images/feature1.png" alt="">
                    <h3>${featureTitle1}</h3>
                    <p class="pt-3">${featureDescription1}</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="single-feature text-center item-padding mt-4 mt-md-0">
                    <img src="images/feature2.png" alt="">
                    <h3>${featureTitle2}</h3>
                    <p class="pt-3">${featureDescription2}</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="single-feature text-center item-padding mt-4 mt-lg-0">
                    <img src="images/feature3.png" alt="">
                    <h3>${featureTitle3}</h3>
                    <p class="pt-3">${featureDescription3}</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="single-feature text-center item-padding mt-4 mt-lg-0">
                    <img src="images/feature4.png" alt="">
                    <h3>${featureTitle4}</h3>
                    <p class="pt-3">${featureDescription4}</p>
                </div>
            </div>
        </div>
    </div>
</section>
<%-- Feature Area End --%>

<%-- Welcome Area Starts --%>
<section class="welcome-area section-padding3">
    <div class="container">
        <div class="row">
            <div class="col-lg-5 align-self-center">
                <div class="welcome-img">
                    <img src="images/welcome.png" alt="">
                </div>
            </div>
            <div class="col-lg-7">
                <div class="welcome-text mt-5 mt-lg-0">
                    <h2>${welcomeTitle}</h2>
                    <p class="pt-3">${welcomeDescription1}</p>
                    <p class="pt-3">${welcomeDescription2}</p>
                </div>
            </div>
        </div>
    </div>
</section>
<%-- Welcome Area End --%>

<%-- Department Area Starts --%>
<section class="department-area section-padding4">
    <div class="container">
        <div class="row">
            <div class="col-lg-6 offset-lg-3">
                <div class="section-top text-center">
                    <h2>${departmentsTitle}</h2>
                    <p>${departmentsDescription}</p>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="department-slider owl-carousel">
                    <c:forEach items="${Department.values()}" var="department">
                        <div class="single-slide">
                            <div class="slide-img">
                                <img src="images/departments/${department}.jpg" alt="" class="img-fluid">
                                <div class="hover-state">
                                    <a href="#"><i class="fa fa-stethoscope"></i></a>
                                </div>
                            </div>
                            <div class="single-department item-padding text-center">
                                <h3>${department}</h3>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</section>
<%-- Department Area Starts --%>

<%-- Hotline Area Starts --%>
<section class="hotline-area text-center section-padding">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <h2>${hotlineTitle}</h2>
                <span>(+01) – 256 567 550</span>
                <p class="pt-3">${hotlineDescription}</p>
            </div>
        </div>
    </div>
</section>
<%-- Hotline Area End --%>
<%@include file="component/footer.jsp" %>
</body>
</html>
