package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.table.IcdFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectIcdCodeFilter implements Filter {
    private final UserValidator userValidator = new UserValidator();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        if (!userValidator.isValidIcdCode(servletRequest.getParameter(IcdFieldName.CODE))) {
            StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                    FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);
            response.add(IcdFieldName.CODE);

            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(servletRequest.getParameter(ParameterName.PAGE_OF_DEPARTURE))
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
