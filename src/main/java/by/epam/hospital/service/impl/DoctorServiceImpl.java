package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.IcdDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.IcdDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;

import java.sql.Date;
import java.util.Optional;

// TODO: 20.10.2020 diagnose disease
public class DoctorServiceImpl implements DoctorService {
    private final UserDao userDao = new UserDaoImpl();
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();
    private final IcdDao icdDao = new IcdDaoImpl();

    @Override
    public Optional<User> findByRegistrationData(String firstName, String surname, String lastName, Date birthday)
            throws ServiceException {
        Optional<User> optionalUser = Optional.empty();
        try {
            Optional<UserDetails> userDetails =
                    userDetailsDao.findByRegistrationData(firstName, surname, lastName, birthday);
            if (userDetails.isPresent()) {
                optionalUser = userDao.findById(userDetails.get().getUserId());
            }
        } catch (DaoException e) {
            throw new ServiceException("Can not find user by id", e);
        }
        return optionalUser;
    }

    public boolean diagnoseDisease(String doctorLogin, String patientLogin, String icdCode, String reason) {
        return false;
    }
}
