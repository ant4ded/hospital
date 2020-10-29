package epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.dao.impl.DiagnosisDaoImpl;
import by.epam.hospital.dao.impl.TherapyDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.*;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.DoctorServiceImpl;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "services", dependsOnGroups = "dao")
public class DoctorServiceImplTest {
    private DoctorService doctorService;
    private TherapyDao therapyDao;
    private DiagnosisDao diagnosisDao;
    private UserDetailsDao userDetailsDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        doctorService = new DoctorServiceImpl();
        therapyDao = new TherapyDaoImpl();
        diagnosisDao = new DiagnosisDaoImpl();
        userDetailsDao = new UserDetailsDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData(User user) throws DaoException, ServiceException {
        userDao.create(user);
        user.getUserDetails().setUserId(userDao.findByLogin(user.getLogin()).orElseThrow(DaoException::new).getId());
        userDetailsDao.create(user.getUserDetails());
        UserDetails userDetails = user.getUserDetails();

        if (doctorService.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()).isEmpty()) {
            cleaner.delete(user);
            Assert.fail("Create or findByRegistrationData work incorrect");
        }
        cleaner.delete(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void diagnoseDisease(Diagnosis diagnosis, User patient) throws DaoException, ServiceException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        userDao.create(doctor);
        userDao.create(patient);
        doctor = userDao.findByLogin(doctor.getLogin()).orElseThrow(DaoException::new);
        patient = userDao.findByLogin(patient.getLogin()).orElseThrow(DaoException::new);
        diagnosis.setDoctor(doctor);

        doctorService.diagnoseDisease(diagnosis, patient.getLogin(), cardType);

        Therapy therapy = therapyDao.find(doctor.getLogin(), patient.getLogin(), cardType)
                .orElseThrow(DaoException::new);
        int numberDiagnoses = diagnosisDao.findAllByTherapyId(therapy.getId()).size();

        cleaner.delete(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
        if (numberDiagnoses != 1) {
            Assert.fail("Diagnose disease failed. Size of diagnosis must be 1 but is " + numberDiagnoses);
        }
    }
}
