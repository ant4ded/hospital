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
        Map<String, Object> result = new HashMap<>();
        request.getSession().removeAttribute(ParameterName.LOGIN_USERNAME);
        request.getSession().removeAttribute(ParameterName.LOGIN_ROLES);
        result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_MAIN);
        return result;
    }
}
