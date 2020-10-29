package by.epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.dao.impl.*;
import by.epam.hospital.entity.*;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;

import java.sql.Date;
import java.util.Optional;

public class DoctorServiceImpl implements DoctorService {
    private final UserDao userDao = new UserDaoImpl();
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();
    private final TherapyDao therapyDao = new TherapyDaoImpl();
    private final DiagnosisDao diagnosisDao = new DiagnosisDaoImpl();
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
            throw new ServiceException("FindByRegistrationData failed.", e);
        }
        return optionalUser;
    }

    @Override
    public boolean diagnoseDisease(Diagnosis diagnosis, String patientLogin, CardType cardType)
            throws ServiceException {
        try {
            Optional<User> optionalDoctor = userDao.findByLogin(diagnosis.getDoctor().getLogin());
            Optional<User> optionalPatient = userDao.findByLogin(patientLogin);
            Optional<Icd> optionalIcd = icdDao.findByCode(diagnosis.getIcd().getCode());
            if (optionalDoctor.isEmpty() || optionalPatient.isEmpty()) {
                throw new ServiceException("DiagnoseDisease failed. User not existing.");
            }
            if (optionalDoctor.get().getRoles().contains(Role.DOCTOR)) {
                throw new ServiceException("DiagnoseDisease failed. User is not a doctor.");
            }
            if (optionalIcd.isEmpty()) {
                throw new ServiceException("DiagnoseDisease failed. Icd not existing.");
            }
            diagnosis.setIcd(optionalIcd.get());
            diagnosis.setDoctor(optionalDoctor.get());

            therapyDao.create(diagnosis.getDoctor().getLogin(), patientLogin, cardType);
            Therapy therapy = therapyDao.find(diagnosis.getDoctor().getLogin(), patientLogin, cardType)
                    .orElseThrow(ServiceException::new);
            diagnosisDao.create(diagnosis, patientLogin, therapy.getId());

        } catch (DaoException e) {
            throw new ServiceException("DiagnoseDisease failed.", e);
        }
        return true;
    }
}
