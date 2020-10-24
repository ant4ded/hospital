package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Role;
import by.epam.hospital.service.AuthenticationService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AuthenticationServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// TODO: 22.10.2020 filter sequence and mapping in web.xml on command urls
public class AuthenticationFilter implements Filter {
    private static final String FORBIDDEN_MESSAGE = "Access denied";

    private final Logger logger = Logger.getLogger(AuthenticationFilter.class);
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl();

    private Role role;

    @Override
    public void init(FilterConfig filterConfig) {
        role = Role.valueOf(filterConfig.getInitParameter(FilterInitParameterName.ROLE));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpServletRequest.getSession();

        String loginUsername = (String) session.getAttribute(ParameterName.LOGIN_USERNAME);
        loginUsername = loginUsername == null ? FilterInitParameterName.ANONYMOUS_USER : loginUsername;

        try {
            if (loginUsername.isBlank() || !authenticationService.isHasRole(loginUsername, role)) {
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN_MESSAGE);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ServiceException e) {
            servletRequest.setAttribute(ParameterName.MESSAGE, e.getMessage());
            httpServletRequest.getRequestDispatcher(HospitalUrl.PAGE_ERROR).forward(servletRequest, servletResponse);
            logger.error(e);
        }
    }
}
