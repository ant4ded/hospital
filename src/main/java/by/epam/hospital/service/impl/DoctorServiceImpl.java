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

    public DoctorServiceImpl(IcdDao icdDao, UserDao userDao, TherapyDao therapyDao,
                             DiagnosisDao diagnosisDao) {
        this.icdDao = icdDao;
        this.userDao = userDao;
        this.therapyDao = therapyDao;
        this.diagnosisDao = diagnosisDao;
    }

    @Override
    public Optional<User> findPatientByUserDetails(UserDetails userDetails) throws ServiceException {
        Optional<User> optionalUser;
        try {
            optionalUser = userDao.findUserWithUserDetailsByPassportData(userDetails.getFirstName(),
                    userDetails.getSurname(), userDetails.getLastName(), userDetails.getBirthday());
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
                Optional<Therapy> currentTherapy = therapyDao
                        .findCurrentPatientTherapy(doctorLogin, patientLogin, cardType);

                if (currentTherapy.isPresent()) {
                    result = diagnosisDao.createDiagnosis(diagnosis, patientLogin, cardType) != 0;
                } else {
                    Therapy therapy = new Therapy();
                    therapy.setPatient(optionalPatient.get());
                    therapy.setDoctor(optionalDoctor.get());
                    result = therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType) != 0;
                }
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
        } catch (DaoException e) {
            throw new ServiceException("Find therapy failed.", e);
        }
        return optionalTherapy;
    }

    @Override
    public List<Therapy> findPatientTherapies(UserDetails userDetails, CardType cardType) throws ServiceException {
        List<Therapy> therapies;
        try {
            therapies = therapyDao.findPatientTherapies(userDetails, cardType);
        } catch (DaoException e) {
            throw new ServiceException("FindPatientTherapies failed.", e);
        }
        return therapies;
    }

    @Override
    public List<Therapy> findOpenDoctorTherapies(String doctorLogin, CardType cardType) throws ServiceException {
        List<Therapy> therapies = new ArrayList<>();
        try {
            Optional<User> optionalUser = userDao.findByLogin(doctorLogin);
            if (optionalUser.isPresent()) {
                therapies = therapyDao.findOpenDoctorTherapies(doctorLogin, cardType);
            }
        } catch (DaoException e) {
            throw new ServiceException("FindPatientTherapies failed.", e);
        }
        return therapies;
    }

    @Override
    public boolean makeLastDiagnosisFinal(String doctorLogin, String patientLogin, CardType cardType)
            throws ServiceException {
        boolean result = false;
        try {
            Optional<User> doctor = userDao.findByLogin(doctorLogin);
            Optional<User> patient = userDao.findByLogin(patientLogin);
            Optional<Therapy> therapy = therapyDao.findCurrentPatientTherapy(doctorLogin, patientLogin, cardType);
            boolean isPresent = doctor.isPresent() && patient.isPresent() && therapy.isPresent();
            if (isPresent && !therapy.get().getDiagnoses().isEmpty() && therapy.get().getFinalDiagnosis().isEmpty()) {
                result = therapyDao.setFinalDiagnosisToTherapy(doctorLogin, patientLogin, cardType);
            }
        } catch (DaoException e) {
            throw new ServiceException("MakeLastDiagnosisFinal failed.", e);
        }
        return result;
    }

    @Override
    public boolean closeTherapy(String doctorLogin, String patientLogin, CardType cardType)
            throws ServiceException {
        boolean result = false;
        try {
            Optional<User> doctor = userDao.findByLogin(doctorLogin);
            Optional<User> patient = userDao.findByLogin(patientLogin);
            Optional<Therapy> therapy = therapyDao.findCurrentPatientTherapy(doctorLogin, patientLogin, cardType);
            boolean isPresent = doctor.isPresent() && patient.isPresent() && therapy.isPresent();
            if (isPresent && therapy.get().getFinalDiagnosis().isPresent()) {
                result = therapyDao.setTherapyEndDate(doctorLogin, patientLogin, Date.valueOf(LocalDate.now()), cardType);
            }
        } catch (DaoException e) {
            throw new ServiceException("SetEndDate failed.", e);
        }
        return result;
    }

    @Override
    public boolean assignProcedureToLastDiagnosis(ProcedureAssignment assignment, String doctorLogin,
                                                  String patientLogin, CardType cardType) throws ServiceException {
        boolean result;
        try {
            result = diagnosisDao.assignProcedureToLastDiagnosis(assignment, doctorLogin, patientLogin, cardType);
        } catch (DaoException e) {
            throw new ServiceException("Can not assign procedure to diagnosis.", e);
        }
        return result;
    }

    @Override
    public boolean assignMedicamentToLastDiagnosis(MedicamentAssignment assignment, String doctorLogin,
                                                   String patientLogin, CardType cardType) throws ServiceException {
        boolean result;
        try {
            result = diagnosisDao.assignMedicamentToLastDiagnosis(assignment, doctorLogin, patientLogin, cardType);
        } catch (DaoException e) {
            throw new ServiceException("Can not assign medicament to diagnosis.", e);
        }
        return result;
    }
}
