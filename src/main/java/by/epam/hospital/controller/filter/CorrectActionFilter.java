package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.service.ServiceAction;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectActionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String parameter = servletRequest.getParameter(ParameterName.ACTION);

        if (!ServiceAction.isAdd(parameter) && !ServiceAction.isRemove(parameter)) {
            StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                    FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);
            response.add(ParameterName.ACTION);

            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(servletRequest.getParameter(ParameterName.PAGE_OF_DEPARTURE))
                    .forward(servletRequest, servletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
