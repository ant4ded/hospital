package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.IcdDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.IcdDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;

import java.sql.Date;
import java.util.Optional;

// TODO: 20.10.2020 diagnose disease
public class DoctorServiceImpl implements DoctorService {
    private final UserDao userDao = new UserDaoImpl();
    private final IcdDao icdDao = new IcdDaoImpl();

    @Override
    public Optional<User> findByRegistrationData(String firstName, String surname, String lastName, Date birthday) throws ServiceException {
        Optional<User> optionalUser;
        try {
            optionalUser = userDao.findByRegistrationData(firstName, surname, lastName, birthday);
        } catch (DaoException e) {
            throw new ServiceException("Can not find user by id", e);
        }
        return optionalUser;
    }

    public boolean diagnoseDisease(String doctorLogin, User patient, String icdCode) {
        return false;
    }
}
