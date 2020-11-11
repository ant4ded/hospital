package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.table.UsersDetailsFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class PassportDetailsFilter implements Filter {
    private final UserValidator userValidator = new UserValidator();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        boolean hasInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);

        if (!userValidator.isValidName(servletRequest.getParameter(UsersDetailsFieldName.FIRST_NAME))) {
            hasInvalidFields = true;
            response.add(UsersDetailsFieldName.FIRST_NAME);
        }
        if (!userValidator.isValidName(servletRequest.getParameter(UsersDetailsFieldName.SURNAME))) {
            hasInvalidFields = true;
            response.add(UsersDetailsFieldName.SURNAME);
        }
        if (!userValidator.isValidName(servletRequest.getParameter(UsersDetailsFieldName.LAST_NAME))) {
            hasInvalidFields = true;
            response.add(UsersDetailsFieldName.LAST_NAME);
        }
        if (!userValidator.isValidBirthDate(servletRequest.getParameter(UsersDetailsFieldName.BIRTHDAY))) {
            hasInvalidFields = true;
            response.add(UsersDetailsFieldName.BIRTHDAY);
        }
        if (hasInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(servletRequest.getParameter(ParameterName.PAGE_OF_DEPARTURE))
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
