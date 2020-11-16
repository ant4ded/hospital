package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;

import java.util.Optional;

public class ReceptionistServiceImpl implements ReceptionistService {
    private final UserDao userDao;
    private final UserDetailsDao userDetailsDao;

    public ReceptionistServiceImpl(UserDao userDao, UserDetailsDao userDetailsDao) {
        this.userDao = userDao;
        this.userDetailsDao = userDetailsDao;
    }

    @Override
    public boolean registerClient(User user) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> optionalUser = userDao.findByLogin(user.getLogin());
            if (optionalUser.isEmpty()) {
                result = userDao.createClientWithUserDetails(user) > 0;
            }
        } catch (DaoException e) {
            throw new ServiceException("Registration new client failed.", e);
        }
        return result;
    }

    @Override
    public Optional<User> findUserCredentials(UserDetails userDetails) throws ServiceException {
        Optional<User> optionalUser;
        try {
            optionalUser = userDao.findUserWithUserDetailsByPassportData(userDetails.getFirstName(),
                    userDetails.getSurname(), userDetails.getLastName(), userDetails.getBirthday());
        } catch (DaoException e) {
            throw new ServiceException("FindUserCredentials failed.", e);
        }
        return optionalUser;
    }
}
