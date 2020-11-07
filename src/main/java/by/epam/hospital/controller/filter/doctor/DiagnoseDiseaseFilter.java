package by.epam.hospital.controller.filter.doctor;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.FilterMessageParameter;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.table.IcdFieldName;
import by.epam.hospital.entity.table.UsersDetailsFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class DiagnoseDiseaseFilter implements Filter {

    private final UserValidator userValidator = new UserValidator();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);
        // TODO: 07.11.2020 code duplication
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
        if (!userValidator.isValidDate(servletRequest
                .getParameter(UsersDetailsFieldName.BIRTHDAY))) {
            isHaveInvalidFields = true;
            response.add(UsersDetailsFieldName.BIRTHDAY);
        }
        if (!userValidator.isValidIcdCode(servletRequest.getParameter(IcdFieldName.CODE))) {
            isHaveInvalidFields = true;
            response.add(IcdFieldName.CODE);
        }
        if (isHaveInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(HospitalUrl.PAGE_DIAGNOSE_DISEASE)
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
