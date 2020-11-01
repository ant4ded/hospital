package by.epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.entity.*;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;

import java.sql.Date;
import java.util.Optional;

public class DoctorServiceImpl implements DoctorService {
    private final IcdDao icdDao;
    private final UserDao userDao;
    private final TherapyDao therapyDao;
    private final DiagnosisDao diagnosisDao;
    private final UserDetailsDao userDetailsDao;

    public DoctorServiceImpl(IcdDao icdDao, UserDao userDao, TherapyDao therapyDao,
                             DiagnosisDao diagnosisDao, UserDetailsDao userDetailsDao) {
        this.icdDao = icdDao;
        this.userDao = userDao;
        this.therapyDao = therapyDao;
        this.diagnosisDao = diagnosisDao;
        this.userDetailsDao = userDetailsDao;
    }

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
            throw new ServiceException("FindByRegistrationData failed.", e);
        }
        return optionalUser;
    }

    @Override
    public boolean diagnoseDisease(Diagnosis diagnosis, String patientLogin, CardType cardType)
            throws ServiceException {
        boolean result = false;
        try {
            Optional<User> optionalDoctor = userDao.findByLogin(diagnosis.getDoctor().getLogin());
            Optional<User> optionalPatient = userDao.findByLogin(patientLogin);
            Optional<Icd> optionalIcd = icdDao.findByCode(diagnosis.getIcd().getCode());
            if (optionalDoctor.isPresent() && optionalPatient.isPresent() &&
                    optionalDoctor.get().getRoles().contains(Role.DOCTOR) && optionalIcd.isPresent()) {
                diagnosis.setIcd(optionalIcd.get());
                diagnosis.setDoctor(optionalDoctor.get());
                int therapyId = therapyDao.create(diagnosis.getDoctor().getLogin(), patientLogin, cardType);
                diagnosisDao.create(diagnosis, patientLogin, therapyId);
                result = true;
            }
        } catch (DaoException e) {
            throw new ServiceException("DiagnoseDisease failed.", e);
        }
        return result;
    }
}
