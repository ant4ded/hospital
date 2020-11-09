package epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.entity.*;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public void findByRegistrationData_correctFind_userPresent(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDetailsDao.findByRegistrationData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.of(user.getUserDetails()));
        doctorService.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_nonExistentUser_userPresent(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDetailsDao.findByRegistrationData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.empty());
        doctorService.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findByRegistrationData_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDetailsDao.findByRegistrationData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenThrow(DaoException.class);
        doctorService.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy")
    public void findCurrentPatientTherapy_patientTherapyWithoutEndTherapy_therapyPresent(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertTrue(doctorService.findCurrentPatientTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy")
    public void findCurrentPatientTherapy_patientTherapyWithEndTherapy_therapyEmpty(User doctor, User patient)
            throws DaoException, ServiceException {
        Therapy therapy = new Therapy();
        therapy.setEndTherapy(new Date(0));
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(therapy));
        Assert.assertTrue(doctorService.findCurrentPatientTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy")
    public void findCurrentPatientTherapy_patientTherapyEmpty_therapyEmpty(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.empty());
        Assert.assertTrue(doctorService.findCurrentPatientTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy", expectedExceptions = ServiceException.class)
    public void findCurrentPatientTherapy_daoException_serviceException(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenThrow(new DaoException());
        Assert.assertTrue(doctorService.findCurrentPatientTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_currentTherapyPresent_true(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        User doctor = diagnosis.getDoctor();
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertTrue(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_currentTherapyEmpty_true(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        User doctor = diagnosis.getDoctor();
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertTrue(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_doctorEmpty_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_doctorNotHaveDoctorRole_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_patientEmpty_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(diagnosis.getDoctor()));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_icdEmpty_false(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(diagnosis.getDoctor().getLogin()))
                .thenReturn(Optional.of(diagnosis.getDoctor()));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.empty());
        Assert.assertFalse(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy", expectedExceptions = ServiceException.class)
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
        doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findAllPatientTherapies_existingUserDetails_therapiesList(User user)
            throws DaoException, ServiceException {
        Therapy first = new Therapy();
        Therapy second = new Therapy();
        List<Therapy> therapies = new ArrayList<>(Arrays.asList(first, second));
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDetailsDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()))
                .thenReturn(Optional.of(userDetails));
        Mockito.when(userDao.findById(userDetails.getUserId()))
                .thenReturn(Optional.of(user));
        Mockito.when(therapyDao.findAllTherapies(user.getLogin(), CardType.AMBULATORY))
                .thenReturn(therapies);
        Assert.assertEquals(doctorService.
                findAllPatientTherapies(user.getUserDetails(), CardType.AMBULATORY), therapies);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findAllPatientTherapies_nonExistentUserDetails_emptyList(User user)
            throws DaoException, ServiceException {
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDetailsDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(doctorService.
                findAllPatientTherapies(user.getUserDetails(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findAllPatientTherapies_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDetailsDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()))
                .thenThrow(DaoException.class);
        Assert.assertEquals(doctorService.
                findAllPatientTherapies(user.getUserDetails(), CardType.AMBULATORY), new ArrayList<>());
    }
}