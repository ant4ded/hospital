package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;

import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    private final UserDao userDao;

    public ClientServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> authorization(String login, String password) throws ServiceException {
        Optional<User> optionalUser = Optional.empty();
        Optional<User> userFromDb;
        try {
            userFromDb = userDao.findByLogin(login);
            if (userFromDb.isPresent() && userFromDb.get().getPassword().equals(password)) {
                optionalUser = userFromDb;
            }
        } catch (DaoException e) {
            throw new ServiceException("Authorization failed.", e);
        }
        return optionalUser;
    }
}
