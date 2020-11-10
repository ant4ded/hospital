package by.epam.hospital.controller.filter.receptionist;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.FilterMessageParameter;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.entity.table.UsersFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class RegisterClientFilter implements Filter {

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
        if (!userValidator.isValidPassportId(servletRequest
                .getParameter(UsersDetailsFieldName.PASSPORT_ID))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.PASSPORT_ID);
        }
        if (!userValidator.isValidPhone(servletRequest
                .getParameter(UsersDetailsFieldName.PHONE))) {
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
