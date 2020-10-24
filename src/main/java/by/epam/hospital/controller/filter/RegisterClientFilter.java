package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.table.UsersDetailsFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class RegisterClientFilter implements Filter {
    private static final String PREFIX = "Invalid data. [";
    private static final String SUFFIX = "]";
    private static final String DELIMITER = ", ";

    private final UserValidator userValidator = new UserValidator();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(DELIMITER, PREFIX, SUFFIX);

        // TODO: 24.10.2020 auto generate login and password
        if (!userValidator.isValidPassportId(servletRequest
                .getParameter(UsersDetailsFieldName.PASSPORT_ID))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.PASSPORT_ID);
        }
        if (!userValidator.isValidFirstLastNameOrSurname(servletRequest
                .getParameter(UsersDetailsFieldName.FIRST_NAME))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.FIRST_NAME);
        }
        if (!userValidator.isValidFirstLastNameOrSurname(servletRequest
                .getParameter(UsersDetailsFieldName.SURNAME))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.SURNAME);
        }
        if (!userValidator.isValidFirstLastNameOrSurname(servletRequest
                .getParameter(UsersDetailsFieldName.LAST_NAME))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.LAST_NAME);
        }
        if (!userValidator.isValidDate(servletRequest
                .getParameter(UsersDetailsFieldName.BIRTHDAY))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.BIRTHDAY);
        }
        if (!userValidator.isValidPhone(servletRequest.
                getParameter(UsersDetailsFieldName.PHONE))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.PHONE);
        }
        if (isHaveInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(HospitalUrl.PAGE_REGISTRY).forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
