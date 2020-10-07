package by.epam.hospital.controller.main.command;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.main.MainParameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignOut implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute(MainParameter.LOGIN_USERNAME);
        request.getSession().removeAttribute(MainParameter.LOGIN_ROLES);
        response.sendRedirect(HospitalUrl.MAIN_PAGE);
    }
}
