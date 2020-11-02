package by.epam.hospital.controller.command;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignOut implements HttpCommand {
    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute(ParameterName.LOGIN_USERNAME);
        request.getSession().removeAttribute(ParameterName.LOGIN_ROLES);
        response.sendRedirect(HospitalUrl.MAIN_URL);
        return new HashMap<>();
    }
}
