package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.validator.UserValidator;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.IcdFieldName;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ServiceAction;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class RequestParametersFilter implements Filter {
    private static final String INIT_PARAMETER_NAME = "parameter_name";
    private static final String PREFIX = "Invalid data. [";
    private static final String SUFFIX = "]";
    private static final String DELIMITER = ", ";

    private final UserValidator userValidator = new UserValidator();
    private String parameterName;

    @Override
    public void init(FilterConfig filterConfig) {
        parameterName = filterConfig.getInitParameter(INIT_PARAMETER_NAME);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String parameterValue = servletRequest.getParameter(parameterName);
        boolean isParameterInvalid = switch (parameterName) {
            case ParameterName.ACTION -> !ServiceAction.hasValue(parameterValue);
            case ParameterName.CARD_TYPE -> !CardType.hasValue(parameterValue);
            case ParameterName.DEPARTMENT -> !Department.hasValue(parameterValue);
            case ParameterName.ROLE -> !Role.hasValue(parameterValue);
            case UsersDetailsFieldName.GENDER -> !UserDetails.Gender.hasValue(parameterValue);
            case IcdFieldName.CODE -> !userValidator.isValidIcdCode(parameterValue);
            case UsersFieldName.LOGIN -> !userValidator.isValidLogin(parameterValue);
            case UsersDetailsFieldName.PASSPORT_ID -> !userValidator.isValidPassportId(parameterName);
            case UsersDetailsFieldName.PHONE -> !userValidator.isValidPhone(parameterName);
            case UsersDetailsFieldName.BIRTHDAY -> !userValidator.isValidBirthDate(parameterName);
            case UsersDetailsFieldName.FIRST_NAME,
                    UsersDetailsFieldName.SURNAME,
                    UsersDetailsFieldName.LAST_NAME -> userValidator.isValidName(parameterName);
            default -> false;
        };
        if (isParameterInvalid) {
            StringJoiner response = new StringJoiner(DELIMITER, PREFIX, SUFFIX);
            response.add(parameterName);
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(servletRequest.getParameter(ParameterName.PAGE_OF_DEPARTURE))
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
