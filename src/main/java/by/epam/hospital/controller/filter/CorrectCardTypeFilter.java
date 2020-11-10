package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.service.ServiceAction;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectCardTypeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        boolean isHaveInvalidFields = false;
        StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);

        String parameter = servletRequest.getParameter(ParameterName.CARD_TYPE);
        int i = 0;
        while (i < CardType.values().length) {
            if (CardType.values()[i++].name().equals(parameter)) {
                break;
            }
        }
        if (i > CardType.values().length) {
            isHaveInvalidFields = true;
            response.add(ParameterName.CARD_TYPE);
        }
        if (isHaveInvalidFields) {
            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL)
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
