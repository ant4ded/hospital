package by.epam.hospital.controller.filter;

import by.epam.hospital.controller.ParameterName;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionLocaleFilter implements Filter {
    private static final String CHARACTER_ENCODING = "utf-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(CHARACTER_ENCODING);
        response.setCharacterEncoding(CHARACTER_ENCODING);

        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getParameter(ParameterName.LOCALE_SESSION) != null) {
            req.getSession().setAttribute(ParameterName.LANG, req.getParameter(ParameterName.LOCALE_SESSION));
        } else {
            req.getSession().setAttribute(ParameterName.LANG, ParameterName.LOCALE_EN);
        }
        chain.doFilter(request, response);
    }
}
