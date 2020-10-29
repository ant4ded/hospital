package by.epam.hospital.service.impl;

import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.AuthenticationService;
import by.epam.hospital.service.ServiceException;

import java.util.Optional;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public boolean isHasRole(String login, Role role) throws ServiceException {
        boolean result = false;
        User user;
        Optional<User> optionalUser;
        try {
            if (!login.equalsIgnoreCase(ParameterName.ANONYMOUS_USER)) {
                optionalUser = userDao.findByLogin(login);
                if (optionalUser.isEmpty()) {
                    throw new ServiceException("Can not find user - " + login);
                }
                user = optionalUser.get();
                result = user.getRoles().contains(role);
            }
        } catch (DaoException e) {
            throw new ServiceException("Exception with find user - " + login, e);
        }
        return result;
    }
}
