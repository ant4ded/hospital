package by.epam.hospital.controller.filter;


import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.table.UsersFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectLoginFilter implements Filter {
    private final UserValidator userValidator = new UserValidator();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);

        if (!userValidator.isValidLogin(servletRequest.getParameter(UsersFieldName.LOGIN))) {
            isHaveInvalidFields = true;
            response.add(UsersFieldName.LOGIN);
        }
        if (isHaveInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(servletRequest.getParameter(ParameterName.PAGE_OF_DEPARTURE))
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
