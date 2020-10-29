package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;

import java.util.Optional;

public class ReceptionistServiceImpl implements ReceptionistService {
    private final UserDao userDao = new UserDaoImpl();
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();

    @Override
    public boolean registerClient(User user) throws ServiceException {
        try {
            Optional<User> optionalUser = userDao.findByLogin(user.getLogin());
            if (optionalUser.isPresent()) {
                throw new ServiceException("Registration new client failed. User already existing.");
            } else {
                userDao.create(user);
                user.getUserDetails().setUserId(userDao.findByLogin(user.getLogin())
                        .orElseThrow(ServiceException::new).getId());
                userDetailsDao.create(user.getUserDetails());
            }
        } catch (DaoException e) {
            throw new ServiceException("Registration new client failed.", e);
        }
        return true;
    }
}
