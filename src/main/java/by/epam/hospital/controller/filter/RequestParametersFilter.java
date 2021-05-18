package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.CommandName;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.filter.validator.DataValidator;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.*;
import by.epam.hospital.service.ServiceAction;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class RequestParametersFilter implements Filter {
    private static final String INIT_PARAMETER_NAME = "parameter_name";
    private static final String PREFIX = "Invalid data. [";
    private static final String SUFFIX = "]";
    private static final String DELIMITER = ", ";

    private final DataValidator dataValidator = new DataValidator();
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
            case ParameterName.COMMAND -> !CommandName.hasValue(parameterValue);
            case ParameterName.ACTION -> !ServiceAction.hasValue(parameterValue);
            case ParameterName.CARD_TYPE -> !CardType.hasValue(parameterValue);
            case ParameterName.DEPARTMENT -> !Department.hasValue(parameterValue);
            case ParameterName.ROLE -> !Role.hasValue(parameterValue);
            case ParameterName.PROCEDURE_OR_MEDICAMENT_NAME -> !dataValidator
                    .isValidProcedureOrMedicamentName(parameterValue);
            case ProceduresFieldName.COST -> !dataValidator.isValidCost(parameterValue);
            case UsersDetailsFieldName.GENDER -> !UserDetails.Gender.hasValue(parameterValue);
            case IcdFieldName.CODE -> !dataValidator.isValidIcdCode(parameterValue);
            case UsersFieldName.LOGIN -> !dataValidator.isValidLogin(parameterValue);
            case UsersDetailsFieldName.PASSPORT_ID -> !dataValidator.isValidPassportId(parameterValue);
            case UsersDetailsFieldName.PHONE -> !dataValidator.isValidPhone(parameterValue);
            case UsersDetailsFieldName.BIRTHDAY -> !dataValidator.isValidBirthDate(parameterValue);
            case UsersDetailsFieldName.FIRST_NAME,
                    UsersDetailsFieldName.SURNAME,
                    UsersDetailsFieldName.LAST_NAME -> !dataValidator.isValidName(parameterValue);
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
