<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.entity.table.UsersDetailsFieldName" %>
<%@page import="by.epam.hospital.entity.table.UsersFieldName" %>
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
                    <h4>Find user credentials panel</h4>
                    <p>${requestScope.message}</p>
                    <c:if test="${requestScope.get(UsersFieldName.LOGIN) != null}">
                    <div class="form-group form-inline justify-content-between">
                        <div class="form-group">
                            <input type="text" value="${requestScope.get(UsersFieldName.LOGIN)}"
                                   placeholder="Login" onfocus="this.placeholder = ''"
                                   onblur="this.placeholder = 'Login'" disabled class="form-control">
                        </div>
                        <div class="form-group">
                            <input type="text" value="${requestScope.get(UsersFieldName.PASSWORD)}"
                                   placeholder="Password" onfocus="this.placeholder = ''"
                                   onblur="this.placeholder = 'Password'" disabled class="form-control">
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
                        <button type="submit" class="template-btn form-btn">Find</button>
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
