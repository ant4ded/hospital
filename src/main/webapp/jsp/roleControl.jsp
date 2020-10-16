<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="by.epam.hospital.controller.CommandName" %>
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
                    <h4>Register new client</h4>
                    <p>${requestScope.message}</p>
                    <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                        <input type="hidden" name="${ParameterName.COMMAND}" value="${CommandName.FIND_USER_ROLES}">
                        <div class="form-group form-inline">
                            <div class="form-group col-lg-10 col-md-10 name">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       value="<%=request.getParameter(ParameterName.LOGIN) == null ?
                                        "" : request.getParameter(ParameterName.LOGIN)%>"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-2 col-md-2 name">
                                <button type="submit" class="template-btn">Submit</button>
                            </div>
                        </div>
                    </form>
                    <c:if test="${requestScope.userRoles != null}">
                        <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <div class="form-group form-inline ">
                                <div class="form-group col-lg-4 col-md-4">
                                    <input type="hidden">
                                    <p>Administrator</p>
                                </div>
                                <c:choose>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.ADMIN)}">
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.ADMIN.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_REMOVE}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger">Remove</button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.MEDICAL_ASSISTANT) ||
                                        requestScope.userRoles.containsValue(Role.DEPARTMENT_HEAD) ||
                                        requestScope.userRoles.containsValue(Role.RECEPTIONIST) ||
                                        requestScope.userRoles.containsValue(Role.DOCTOR)}">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.ADMIN.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_ADD}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary">Add</button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                        <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <div class="form-group form-inline ">
                                <div class="form-group col-lg-4 col-md-4">
                                    <p>Receptionist</p>
                                </div>
                                <c:choose>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.RECEPTIONIST)}">
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.RECEPTIONIST.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_REMOVE}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger">Remove</button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.MEDICAL_ASSISTANT) ||
                                        requestScope.userRoles.containsValue(Role.DEPARTMENT_HEAD) ||
                                        requestScope.userRoles.containsValue(Role.DOCTOR) ||
                                        requestScope.userRoles.containsValue(Role.ADMIN)}">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.RECEPTIONIST.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_ADD}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary">Add</button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                        <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <div class="form-group form-inline ">
                                <div class="form-group col-lg-4 col-md-4">
                                    <p>Doctor</p>
                                </div>
                                <c:choose>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.DOCTOR)}">
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.DOCTOR.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_REMOVE}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger">Remove</button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.MEDICAL_ASSISTANT) ||
                                        requestScope.userRoles.containsValue(Role.DEPARTMENT_HEAD) ||
                                        requestScope.userRoles.containsValue(Role.RECEPTIONIST) ||
                                        requestScope.userRoles.containsValue(Role.ADMIN)}">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.DOCTOR.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_ADD}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary">Add</button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                        <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <div class="form-group form-inline ">
                                <div class="form-group col-lg-4 col-md-4">
                                    <p>Head of department</p>
                                </div>
                                <c:choose>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.DEPARTMENT_HEAD)}">
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.DEPARTMENT_HEAD.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_REMOVE}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger">Remove</button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.MEDICAL_ASSISTANT) ||
                                        requestScope.userRoles.containsValue(Role.RECEPTIONIST) ||
                                        !requestScope.userRoles.containsValue(Role.DOCTOR) ||
                                        requestScope.userRoles.containsValue(Role.ADMIN)}">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.DEPARTMENT_HEAD.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_ADD}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary">Add</button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                        <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <div class="form-group form-inline ">
                                <div class="form-group col-lg-4 col-md-4">
                                    <p>Medical assistant</p>
                                </div>
                                <c:choose>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.MEDICAL_ASSISTANT)}">
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.MEDICAL_ASSISTANT.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_REMOVE}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger">Remove</button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.userRoles.containsValue(Role.DEPARTMENT_HEAD) ||
                                        requestScope.userRoles.containsValue(Role.RECEPTIONIST) ||
                                        requestScope.userRoles.containsValue(Role.DOCTOR) ||
                                        requestScope.userRoles.containsValue(Role.ADMIN)}">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary disable" disabled>Add
                                            </button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="${ParameterName.ROLE}"
                                               value="${Role.MEDICAL_ASSISTANT.name()}">
                                        <input type="hidden" name="${ParameterName.ACTION}"
                                               value="${ParameterName.ACTION_ADD}">
                                        <input type="hidden" name="${ParameterName.COMMAND}"
                                               value="${CommandName.ROLE_CONTROL}">
                                        <input type="hidden" name="${ParameterName.LOGIN}"
                                               value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn primary">Add</button>
                                        </div>
                                        <div class="form-group col-lg-2 col-md-2">
                                            <button type="submit" class="genric-btn danger disable" disabled>Remove
                                            </button>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</section>
<!--================Blog Area =================-->
<%@include file="component/footer.jsp" %>
</body>
</html>
