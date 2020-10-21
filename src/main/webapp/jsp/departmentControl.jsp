<%--suppress HtmlFormInputWithoutLabel --%>
<%@page contentType="text/html;charset=UTF-8" %>
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
<!-- Banner Area Starts -->
<section class="blog_area section-padding">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 posts-list">
                <div class="comment-form">
                    <h4>Department control panel</h4>
                    <p>${requestScope.message}</p>
                    <c:if test="${requestScope.userRoles.contains(Role.DEPARTMENT_HEAD)}">
                        <p>
                            This user is head of department ${requestScope.department}.
                            You can not move him to other department.
                        </p>
                    </c:if>
                    <c:if test="${requestScope.login == null}">
                        <p>Find doctor if you want move him to any department.</p>
                    </c:if>
                    <form method="post" action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                        <input type="hidden" name="${ParameterName.COMMAND}"
                               value="${CommandName.FIND_DEPARTMENT_CONTROL_ATTRIBUTES}">
                        <div class="form-group form-inline">
                            <div class="form-group col-lg-11 col-md-11 name">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       value="<%=request.getParameter(ParameterName.LOGIN) == null ?
                                        "" : request.getParameter(ParameterName.LOGIN)%>"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-1 col-md-1 name">
                                <button type="submit" class="template-btn">Submit</button>
                            </div>
                        </div>
                    </form>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.INFECTIOUS}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.INFECTIOUS)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Infectious
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.INFECTIOUS}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.CARDIOLOGY}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.CARDIOLOGY)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Cardiology
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.CARDIOLOGY}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.NEUROLOGY}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.NEUROLOGY)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Neurology
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.NEUROLOGY}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.OTORHINOLARYNGOLOGY}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.OTORHINOLARYNGOLOGY)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Otorhinolaryngology
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.OTORHINOLARYNGOLOGY}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.PEDIATRIC}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.PEDIATRIC)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Pediatric
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.PEDIATRIC}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.THERAPEUTIC}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.THERAPEUTIC)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Therapeutic
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.THERAPEUTIC}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.UROLOGY}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.UROLOGY)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Urology
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.UROLOGY}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.TRAUMATOLOGY}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.TRAUMATOLOGY)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Traumatology
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.TRAUMATOLOGY}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group form-inline">
                        <form method="post" class="col-lg-9 col-md-9 d-flex"
                              action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                            <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                   value="${Department.SURGERY}">
                            <input type="hidden" name="${ParameterName.COMMAND}"
                                   value="${CommandName.CHANGE_DEPARTMENT_HEAD}">
                            <div class="form-group col-lg-4 col-md-4">
                                <c:if test="${requestScope.department.equals(Department.SURGERY)}">
                                    <i class="fa fa-user-md"></i>
                                </c:if>
                                Surgery
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <input type="text" name="${ParameterName.LOGIN}" required
                                       class="form-control" placeholder="Login"
                                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
                            </div>
                            <div class="form-group col-lg-4 col-md-4">
                                <button type="submit" class="genric-btn primary">Change department head</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${!requestScope.userRoles.contains(Role.DEPARTMENT_HEAD) &&
                                requestScope.userRoles.contains(Role.DOCTOR)}">
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <input type="hidden" name="${ParameterName.DEPARTMENT}"
                                           value="${Department.SURGERY}">
                                    <input type="hidden" name="${ParameterName.COMMAND}"
                                           value="${CommandName.MOVE_DOCTOR_TO_DEPARTMENT}">
                                    <input type="hidden" name="${ParameterName.LOGIN}"
                                           value="<%=request.getParameter(ParameterName.LOGIN)%>">
                                    <button type="submit" class="genric-btn primary">
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" class="col-lg-3 col-md-3"
                                      action="${HospitalUrl.MAIN_URL}${HospitalUrl.SERVLET_MAIN}">
                                    <button type="submit" class="genric-btn primary disable" disabled>
                                        Move found doctor to department
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!--================Blog Area =================-->
<%@include file="component/footer.jsp" %>
</body>
</html>
