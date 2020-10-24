package by.epam.hospital.controller.filter.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.FilterMessageParameter;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ServiceAction;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class RoleControlFilter implements Filter {
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

        String parameter = servletRequest.getParameter(ParameterName.ACTION);
        int i = 0;
        while (i < ServiceAction.values().length) {
            if (ServiceAction.values()[i++].name().equals(parameter)) {
                break;
            }
        }
        if (i >= ServiceAction.values().length) {
            isHaveInvalidFields = true;
            response.add(ParameterName.ACTION);
        }

        parameter = servletRequest.getParameter(ParameterName.ROLE);
        i = 0;
        while (i < Role.values().length) {
            if (Role.values()[i++].name().equals(parameter)) {
                break;
            }
        }
        if (i >= Role.values().length) {
            isHaveInvalidFields = true;
            response.add(ParameterName.ROLE);
        }
        if (isHaveInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
