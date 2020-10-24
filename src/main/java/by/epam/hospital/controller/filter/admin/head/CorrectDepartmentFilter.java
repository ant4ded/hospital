package by.epam.hospital.controller.filter.admin.head;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.FilterMessageParameter;
import by.epam.hospital.entity.Department;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectDepartmentFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);

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
            servletRequest.getRequestDispatcher(servletRequest
                    .getParameter(ParameterName.PAGE_OF_DEPARTURE)).forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
