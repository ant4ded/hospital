package by.epam.hospital.controller.command;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.ClientServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Authorization implements HttpCommand {
    private static final String UNSUCCESSFUL_MESSAGE = "Incorrect login or password";
    private final ClientService clientService = new ClientServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(ParameterName.LOGIN);
        String password = request.getParameter(ParameterName.PASSWORD);
        User userFromDb;

        try {
            userFromDb = clientService.authorization(login, password);

            request.getSession().setAttribute(ParameterName.LOGIN_USERNAME, userFromDb.getLogin());
            request.getSession().setAttribute(ParameterName.LOGIN_ROLES, userFromDb.getRoles());
        } catch (ServiceException e) {
            request.setAttribute(ParameterName.MESSAGE, UNSUCCESSFUL_MESSAGE);
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_MAIN).forward(request, response);
    }
}
