package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;

public class ReceptionistServiceImpl implements ReceptionistService {
    private final UserDao userDao = new UserDaoImpl();
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();

    @Override
    public boolean registerClient(User user) throws ServiceException {
        // TODO: 09.10.2020 validation
        boolean result = true;

        try {
            if (userDao.find(user.getLogin()).isPresent() &&
                    userDetailsDao.find(user.getId()).isPresent()) {
                result = false;
            }
            userDao.create(user);
            user.getUserDetails().setUserId(userDao.find(user.getLogin()).orElse(user).getId());
            userDetailsDao.create(user.getUserDetails());
        } catch (DaoException e) {
            throw new ServiceException("Registration new client failed");
        }
        return result;
    }
}
