package by.epam.hospital.controller.command;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.ClientServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class Authorization implements HttpCommand {
    private static final String UNSUCCESSFUL_MESSAGE = "Incorrect login or password";
    private final ClientService clientService = new ClientServiceImpl(new UserDaoImpl());

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(UsersFieldName.LOGIN);
        String password = request.getParameter(UsersFieldName.PASSWORD);

        try {
            Optional<User> optionalUser = clientService.authorization(login, password);
            if (optionalUser.isPresent()) {
                request.getSession().setAttribute(ParameterName.LOGIN_USERNAME, optionalUser.get().getLogin());
                request.getSession().setAttribute(ParameterName.LOGIN_ROLES, optionalUser.get().getRoles());
            } else {
                request.setAttribute(ParameterName.MESSAGE, UNSUCCESSFUL_MESSAGE);
            }
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_MAIN).forward(request, response);
    }
}
