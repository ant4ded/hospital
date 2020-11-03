package epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.DoctorServiceImpl;
import epam.hospital.util.Provider;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

public class DoctorServiceImplTest {
    @Mock
    private IcdDao icdDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TherapyDao therapyDao;
    @Mock
    private DiagnosisDao diagnosisDao;
    @Mock
    private UserDetailsDao userDetailsDao;
    private DoctorService doctorService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorService = new DoctorServiceImpl(icdDao, userDao, therapyDao, diagnosisDao, userDetailsDao);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_correctFind_userPresent(User user) throws DaoException, ServiceException {
        Mockito.when(userDetailsDao.findByRegistrationData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.of(user.getUserDetails()));
        doctorService.findByRegistrationData(user.getUserDetails().getFirstName(), user.getUserDetails().getSurname(),
                user.getUserDetails().getLastName(), user.getUserDetails().getBirthday());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_nonExistentUser_userPresent(User user) throws DaoException, ServiceException {
        Mockito.when(userDetailsDao.findByRegistrationData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.empty());
        doctorService.findByRegistrationData(user.getUserDetails().getFirstName(), user.getUserDetails().getSurname(),
                user.getUserDetails().getLastName(), user.getUserDetails().getBirthday());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findByRegistrationData_daoException_serviceException(User user) throws DaoException, ServiceException {
        Mockito.when(userDetailsDao.findByRegistrationData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenThrow(DaoException.class);
        doctorService.findByRegistrationData(user.getUserDetails().getFirstName(), user.getUserDetails().getSurname(),
                user.getUserDetails().getLastName(), user.getUserDetails().getBirthday());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void diagnoseDisease_correctDiagnose_true(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(diagnosis.getDoctor()));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertTrue(doctorService.diagnoseDisease(diagnosis, patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void diagnoseDisease_doctorEmpty_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis, patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void diagnoseDisease_doctorNotHaveDoctorRole_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis, patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void diagnoseDisease_patientEmpty_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(diagnosis.getDoctor()));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis, patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void diagnoseDisease_icdEmpty_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(diagnosis.getDoctor()));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.empty());
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis, patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = ServiceException.class)
    public void diagnoseDisease_daoException_ServiceException(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(diagnosis.getDoctor()));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Mockito.when(therapyDao.create(diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenThrow(DaoException.class);
        doctorService.diagnoseDisease(diagnosis, patient.getLogin(), CardType.AMBULATORY);
    }
}
