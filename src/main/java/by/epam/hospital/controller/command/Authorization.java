package by.epam.hospital.controller.main.command;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.main.MainParameter;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class Authorization implements Command {
    private static final String UNSUCCESSFUL_MESSAGE = "Incorrect login or password";

    private final static Logger logger = Logger.getLogger(Authorization.class);
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userFromRequest = new User();
        userFromRequest.setLogin(request.getParameter(MainParameter.LOGIN));
        userFromRequest.setPassword(request.getParameter(MainParameter.PASSWORD));

        try {
            Optional<User> optionalUser = userDao.find(userFromRequest);
            if (optionalUser.isPresent()) {
                request.getSession().setAttribute(MainParameter.LOGIN_USERNAME, optionalUser.get().getLogin());
                request.getSession().setAttribute(MainParameter.LOGIN_ROLES, optionalUser.get().getRoles());
            } else {
                request.setAttribute(MainParameter.UNSUCCESSFUL_MESSAGE, UNSUCCESSFUL_MESSAGE);
            }
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
        request.getRequestDispatcher(HospitalUrl.JSP_INDEX).forward(request, response);
    }
}
