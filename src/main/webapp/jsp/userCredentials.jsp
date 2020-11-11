<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName" %>
<%@page import="by.epam.hospital.entity.table.UsersFieldName" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locale" var="local"/>

<fmt:message bundle="${local}" key="page.user_credentials" var="page"/>
<fmt:message bundle="${local}" key="user_credentials.title" var="title"/>
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
                    <c:if test="${requestScope.get(UsersFieldName.LOGIN) != null}">
                    <div class="form-group form-inline justify-content-between">
                        <div class="form-group">
                            <input type="text" value="${requestScope.get(UsersFieldName.LOGIN)}"
                                   placeholder="${inputLogin}" onfocus="this.placeholder = ''"
                                   onblur="this.placeholder = '${inputLogin}'" disabled class="form-control">
                        </div>
                        <div class="form-group">
                            <input type="text" value="${requestScope.get(UsersFieldName.PASSWORD)}"
                                   placeholder="${inputPassword}" onfocus="this.placeholder = ''"
                                   onblur="this.placeholder = '${inputPassword}'" disabled class="form-control">
                        </div>
                    </div>
                    </c:if>
                    <form method="post"
                          action="${HospitalUrl.MAIN_URL}${HospitalUrl.COMMAND_FIND_USER_CREDENTIALS}">
                        <input type="hidden" name="${ParameterName.PAGE_OF_DEPARTURE}"
                               value="${HospitalUrl.PAGE_USER_CREDENTIALS}">
                        <input type="hidden" name="${ParameterName.COMMAND}"
                               value="${CommandName.FIND_USER_CREDENTIALS}">
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
                        <button type="submit" class="template-btn form-btn">${btnFind}</button>
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
