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
import java.time.LocalDate;
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
    private DoctorService doctorService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorService = new DoctorServiceImpl(icdDao, userDao, therapyDao, diagnosisDao);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_correctFind_userPresent(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.of(user));
        doctorService.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_nonExistentUser_userPresent(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.empty());
        doctorService.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findByRegistrationData_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
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
    public void diagnoseDisease_ambulatoryTherapyPresent_true(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Mockito.when(userDao.findByLogin(Mockito.anyString()))
                .thenReturn(Optional.of(doctor))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), cardType))
                .thenReturn(Optional.of(new Therapy()));
        Mockito.when(diagnosisDao.createAmbulatoryDiagnosis(diagnosis, patient.getLogin()))
                .thenReturn(1);
        Assert.assertTrue(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                doctor.getLogin(), patient.getLogin(), cardType));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_stationaryTherapyPresent_true(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.STATIONARY;
        Mockito.when(userDao.findByLogin(Mockito.anyString()))
                .thenReturn(Optional.of(doctor))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), cardType))
                .thenReturn(Optional.of(new Therapy()));
        Mockito.when(diagnosisDao.createStationaryDiagnosis(diagnosis, patient.getLogin()))
                .thenReturn(1);
        Assert.assertTrue(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                doctor.getLogin(), patient.getLogin(), cardType));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_currentTherapyEmptyAmbulatoryCardType_true(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        User doctor = diagnosis.getDoctor();
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.empty());
        Mockito.when(therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis))
                .thenReturn(1);
        Assert.assertTrue(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void diagnoseDisease_currentTherapyEmptyStationaryCardType_true(Diagnosis diagnosis, User patient)
            throws DaoException, ServiceException {
        User doctor = diagnosis.getDoctor();
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(icdDao.findByCode(diagnosis.getIcd().getCode()))
                .thenReturn(Optional.of(diagnosis.getIcd()));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.STATIONARY))
                .thenReturn(Optional.empty());
        Mockito.when(therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis))
                .thenReturn(1);
        Assert.assertTrue(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                doctor.getLogin(), patient.getLogin(), CardType.STATIONARY));
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
                .thenThrow(DaoException.class);
        doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findPatientTherapies_existingUserDetails_therapiesList(User user)
            throws DaoException, ServiceException {
        Therapy first = new Therapy();
        Therapy second = new Therapy();
        List<Therapy> therapies = new ArrayList<>(Arrays.asList(first, second));
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(userDetails.getFirstName(),
                userDetails.getSurname(), userDetails.getLastName(), userDetails.getBirthday()))
                .thenReturn(Optional.of(user));
        Mockito.when(therapyDao.findPatientTherapies(user.getLogin(), CardType.AMBULATORY))
                .thenReturn(therapies);
        Assert.assertEquals(doctorService.
                findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY), therapies);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findPatientTherapies_nonExistentUserDetails_emptyList(User user)
            throws DaoException, ServiceException {
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(userDetails.getFirstName(),
                userDetails.getSurname(), userDetails.getLastName(), userDetails.getBirthday()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(doctorService.
                findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findPatientTherapies_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(userDetails.getFirstName(),
                userDetails.getSurname(), userDetails.getLastName(), userDetails.getBirthday()))
                .thenThrow(DaoException.class);
        Assert.assertEquals(doctorService.
                findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY), new ArrayList<>());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findOpenDoctorTherapies_existingDoctorAndTherapies_therapies(User user)
            throws DaoException, ServiceException {
        List<Therapy> therapies = new ArrayList<>(List.of(new Therapy()));
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(therapyDao.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY))
                .thenReturn(therapies);
        Assert.assertFalse(doctorService.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findOpenDoctorTherapies_nonExistentDoctor_emptyList(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(doctorService.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findOpenDoctorTherapies_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertTrue(doctorService.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient")
    public void makeLastDiagnosisFinal_doctorAndPatientExist_true(User doctor, User patient)
            throws DaoException, ServiceException {
        Diagnosis diagnosis = new Diagnosis();
        Therapy therapy = new Therapy();
        therapy.setDiagnoses(List.of(diagnosis));
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(therapy));
        Mockito.when(therapyDao.setFinalDiagnosisToTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(true);
        Assert.assertTrue(doctorService.makeLastDiagnosisFinal(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient")
    public void makeLastDiagnosisFinal_diagnosisEmpty_false(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertFalse(doctorService.makeLastDiagnosisFinal(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            expectedExceptions = ServiceException.class)
    public void makeLastDiagnosisFinal_daoException_serviceException(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertFalse(doctorService.makeLastDiagnosisFinal(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient")
    public void setEndDate_doctorAndPatientExist_true(User doctor, User patient)
            throws DaoException, ServiceException {
        Diagnosis diagnosis = new Diagnosis();
        Therapy therapy = new Therapy();
        therapy.setFinalDiagnosis(diagnosis);
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(therapy));
        Mockito.when(therapyDao.setEndTherapy(doctor.getLogin(), patient.getLogin(),
                Date.valueOf(LocalDate.now()), CardType.AMBULATORY))
                .thenReturn(true);
        Assert.assertTrue(doctorService.closeTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient")
    public void setEndDate_finalDiagnosisEmpty_false(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertFalse(doctorService.closeTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            expectedExceptions = ServiceException.class)
    public void setEndDate_daoException_serviceException(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertFalse(doctorService.closeTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY));
    }
}