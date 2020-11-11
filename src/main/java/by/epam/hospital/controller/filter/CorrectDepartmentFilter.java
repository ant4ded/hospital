package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Department;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectDepartmentFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (!Department.hasDepartment(servletRequest.getParameter(ParameterName.DEPARTMENT))) {
            StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                    FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);
            response.add(ParameterName.DEPARTMENT);

            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(servletRequest.getParameter(ParameterName.PAGE_OF_DEPARTURE))
                    .forward(servletRequest, servletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
