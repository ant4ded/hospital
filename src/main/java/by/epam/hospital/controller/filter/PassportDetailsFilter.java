package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.entity.table.UsersFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class PassportDetailsFilter implements Filter {
    private final UserValidator userValidator = new UserValidator();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);

        if (!userValidator.isValidName(servletRequest
                .getParameter(UsersDetailsFieldName.FIRST_NAME))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.FIRST_NAME);
        }
        if (!userValidator.isValidName(servletRequest
                .getParameter(UsersDetailsFieldName.SURNAME))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.SURNAME);
        }
        if (!userValidator.isValidName(servletRequest
                .getParameter(UsersDetailsFieldName.LAST_NAME))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.LAST_NAME);
        }
        if (!userValidator.isValidBirthDate(servletRequest
                .getParameter(UsersDetailsFieldName.BIRTHDAY))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.BIRTHDAY);
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
