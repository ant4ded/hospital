package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.FilterMessageParameter;
import by.epam.hospital.entity.Role;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectRoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);

        String parameter = servletRequest.getParameter(ParameterName.ROLE);
        int i = 0;
        while (i < Role.values().length) {
            if (Role.values()[i++].name().equals(parameter)) {
                break;
            }
        }
        if (i > Role.values().length) {
            isHaveInvalidFields = true;
            response.add(ParameterName.ROLE);
        }
        if (isHaveInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL)
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
