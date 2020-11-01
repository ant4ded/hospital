package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.AuthenticationService;
import by.epam.hospital.service.ServiceException;

import java.util.Optional;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDao userDao;

    public AuthenticationServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean isHasRole(String login, Role role) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isPresent()) {
                result = optionalUser.get().getRoles().contains(role);
            }
        } catch (DaoException e) {
            throw new ServiceException("Authentication failed.", e);
        }
        return result;
    }
}
