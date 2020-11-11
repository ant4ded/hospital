package by.epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.entity.*;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public Optional<User> findPatientByUserDetails(UserDetails userDetails) throws ServiceException {
        Optional<User> optionalUser = Optional.empty();
        try {
            Optional<UserDetails> optionalUserDetails = userDetailsDao
                    .findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                            userDetails.getLastName(), userDetails.getBirthday());
            if (optionalUserDetails.isPresent()) {
                optionalUser = userDao.findById(optionalUserDetails.get().getUserId());
            }
        } catch (DaoException e) {
            throw new ServiceException("FindByRegistrationData failed.", e);
        }
        return optionalUser;
    }

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
            boolean isParametersPresent = optionalDoctor.isPresent() &&
                    optionalPatient.isPresent() &&
                    optionalIcd.isPresent();
            if (isParametersPresent && optionalDoctor.get().getRoles().contains(Role.DOCTOR)) {
                diagnosis.setIcd(optionalIcd.get());
                diagnosis.setDoctor(optionalDoctor.get());
                Optional<Therapy> currentTherapy = findCurrentPatientTherapy(doctorLogin, patientLogin, cardType);

                int therapyId = currentTherapy.isPresent() ?
                        currentTherapy.get().getId() :
                        therapyDao.create(doctorLogin, patientLogin, cardType);
                diagnosisDao.create(diagnosis, patientLogin, therapyId);
                result = true;
            }
        } catch (DaoException e) {
            throw new ServiceException("DiagnoseDisease failed.", e);
        }
        return result;
    }

    @Override
    public Optional<Therapy> findCurrentPatientTherapy(String doctorLogin, String patientLogin, CardType cardType)
            throws ServiceException {
        Optional<Therapy> optionalTherapy;
        try {
            optionalTherapy = therapyDao.findCurrentPatientTherapy(doctorLogin, patientLogin, cardType);
            if (optionalTherapy.isPresent()) {
                optionalTherapy = optionalTherapy.get().getEndTherapy().isPresent() ?
                        Optional.empty() :
                        optionalTherapy;
            }
        } catch (DaoException e) {
            throw new ServiceException("Find therapy failed.", e);
        }
        return optionalTherapy;
    }

    @Override
    public List<Therapy> findPatientTherapies(UserDetails userDetails, CardType cardType) throws ServiceException {
        List<Therapy> therapies = new ArrayList<>();
        try {
            Optional<UserDetails> optionalUserDetails = userDetailsDao
                    .findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                            userDetails.getLastName(), userDetails.getBirthday());
            if (optionalUserDetails.isPresent()) {
                Optional<User> optionalUser = userDao.findById(optionalUserDetails.get().getUserId());
                if (optionalUser.isPresent()) {
                    therapies = therapyDao.findPatientTherapies(optionalUser.get().getLogin(), cardType);
                }
            }
        } catch (DaoException e) {
            throw new ServiceException("FindPatientTherapies failed.", e);
        }
        return therapies;
    }
}
