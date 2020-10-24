package by.epam.hospital.controller.filter.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.FilterMessageParameter;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.table.UsersFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class MoveDoctorToDepartmentFilter implements Filter {
    private final UserValidator userValidator = new UserValidator();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);

        if (!userValidator.isValidLogin(servletRequest
                .getParameter(UsersFieldName.LOGIN))) {
            isHaveInvalidFields = true;
            response.add(UsersFieldName.LOGIN);
        }

        String parameter = servletRequest.getParameter(ParameterName.DEPARTMENT);
        int i = 0;
        while (i < Department.values().length) {
            if (Department.values()[i++].name().equals(parameter)) {
                break;
            }
        }
        if (i >= Department.values().length) {
            isHaveInvalidFields = true;
            response.add(ParameterName.DEPARTMENT);
        }

        if (isHaveInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
