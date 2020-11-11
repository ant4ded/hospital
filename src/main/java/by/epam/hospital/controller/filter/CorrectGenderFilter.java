package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;

import javax.servlet.*;
import java.io.IOException;
import java.util.StringJoiner;

public class CorrectGenderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (!UserDetails.Gender.hasValue(servletRequest.getParameter(UsersDetailsFieldName.GENDER))) {
            StringJoiner response = new StringJoiner(FilterMessageParameter.DELIMITER,
                    FilterMessageParameter.PREFIX, FilterMessageParameter.SUFFIX);
            response.add(UsersDetailsFieldName.GENDER);

            servletRequest.setAttribute(ParameterName.MESSAGE, response.toString());
            servletRequest.getRequestDispatcher(servletRequest.getParameter(ParameterName.PAGE_OF_DEPARTURE))
                    .forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
