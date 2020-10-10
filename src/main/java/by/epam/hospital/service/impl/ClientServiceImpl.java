package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;

import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public User authorization(String login, String password) throws ServiceException {
        User userFromRequest = new User(login, password);
        User userFromDb = new User();

        try {
            Optional<User> optionalUser = userDao.find(userFromRequest);
            if (optionalUser.isPresent()) {
                userFromDb.setLogin(optionalUser.get().getLogin());
                userFromDb.setRoles(optionalUser.get().getRoles());
            } else {
                throw new ServiceException("Invalid login or password");
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return userFromDb;
    }
}
