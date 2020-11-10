package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;

import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    private final UserDao userDao;
    private final UserDetailsDao userDetailsDao;

    public ClientServiceImpl(UserDao userDao, UserDetailsDao userDetailsDao) {
        this.userDao = userDao;
        this.userDetailsDao = userDetailsDao;
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

    @Override
    public Optional<UserDetails> updateUserDetails(String phone, String address, String login)
            throws ServiceException {
        Optional<UserDetails> result = Optional.empty();
        UserDetails userDetails = new UserDetails();
        userDetails.setPhone(phone);
        userDetails.setAddress(address);
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isPresent()) {
                result = userDetailsDao.update(userDetails, optionalUser.get().getId());
            }
        } catch (DaoException e) {
            throw new ServiceException("UpdateUserDetails failed.", e);
        }
        return result;
    }

    @Override
    public Optional<UserDetails> findUserDetails(String login) throws ServiceException {
        Optional<UserDetails> optionalUserDetails = Optional.empty();
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isPresent()) {
                optionalUserDetails = userDetailsDao.findByUserId(optionalUser.get().getId());
            }
        } catch (DaoException e) {
            throw new ServiceException("FindUserDetails failed.", e);
        }
        return optionalUserDetails;
    }
}
