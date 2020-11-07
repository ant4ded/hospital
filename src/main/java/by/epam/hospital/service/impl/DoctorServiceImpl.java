package by.epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.entity.*;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
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
    public Optional<User> findPatientByRegistrationData(String firstName, String surname, String lastName, Date birthday)
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

    // TODO: 07.11.2020 instead diagnosis send diagnosis fields
    @Override
    public boolean diagnoseDisease(String icdCode, String reason, String doctorLogin,
                                   String patientLogin, CardType cardType) throws ServiceException {
        boolean result = false;
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setReason(reason);
        diagnosis.setDiagnosisDate(Date.valueOf(LocalDate.now()));
        try {
            Optional<User> optionalDoctor = userDao.findByLogin(doctorLogin);
            Optional<User> optionalPatient = userDao.findByLogin(patientLogin);
            Optional<Icd> optionalIcd = icdDao.findByCode(icdCode);
            if (optionalDoctor.isPresent() && optionalPatient.isPresent() &&
                    optionalDoctor.get().getRoles().contains(Role.DOCTOR) && optionalIcd.isPresent()) {
                diagnosis.setIcd(optionalIcd.get());
                diagnosis.setDoctor(optionalDoctor.get());
                int therapyId = therapyDao.create(doctorLogin, patientLogin, cardType);
                diagnosisDao.create(diagnosis, patientLogin, therapyId);
                result = true;
            }
        } catch (DaoException e) {
            throw new ServiceException("DiagnoseDisease failed.", e);
        }
        return result;
    }

    // TODO: 06.11.2020 test
    public Optional<Therapy> findCurrentPatientTherapy(String patientLogin, String doctorLogin, CardType cardType)
            throws ServiceException {
        Optional<Therapy> optionalTherapy;
        try {
            optionalTherapy = therapyDao.findCurrentPatientTherapy(patientLogin, doctorLogin, cardType);
        } catch (DaoException e) {
            throw new ServiceException("Find therapy failed.", e);
        }
        return optionalTherapy;
    }

    // TODO: 06.11.2020 find all therapies with all diagnoses
    public List<Therapy> findAllPatientTherapies(String patientLogin, CardType cardType) {
        return null;
    }
}
