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
import java.time.LocalDateTime;
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
    private ProceduresDao procedureDao;
    @Mock
    private MedicamentDao medicamentDao;
    private DoctorService service;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new DoctorServiceImpl(icdDao, userDao, therapyDao, diagnosisDao, procedureDao, medicamentDao);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_correctFind_userPresent(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.of(user));
        service.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_nonExistentUser_userPresent(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenReturn(Optional.empty());
        service.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findByRegistrationData_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()))
                .thenThrow(DaoException.class);
        service.findPatientByUserDetails(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy")
    public void findCurrentPatientTherapy_ambulatoryPatientTherapy_therapyPresent(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertTrue(service.findCurrentPatientTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy")
    public void findCurrentPatientTherapy_stationaryPatientTherapy_therapyPresent(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.STATIONARY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertTrue(service.findCurrentPatientTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.STATIONARY).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy")
    public void findCurrentPatientTherapy_ambulatoryPatientTherapyEmpty_therapyEmpty(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.empty());
        Assert.assertTrue(service.findCurrentPatientTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            groups = "findCurrentPatientTherapy", expectedExceptions = ServiceException.class)
    public void findCurrentPatientTherapy_daoException_serviceException(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao
                .findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenThrow(new DaoException());
        Assert.assertTrue(service.findCurrentPatientTherapy(doctor.getLogin(),
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
        Mockito.when(diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType))
                .thenReturn(1);
        Assert.assertTrue(service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
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
        Mockito.when(diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType))
                .thenReturn(1);
        Assert.assertTrue(service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
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
        Mockito.when(therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, CardType.AMBULATORY))
                .thenReturn(1);
        Assert.assertTrue(service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
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
        Assert.assertFalse(service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
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
        Assert.assertFalse(service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
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
        Assert.assertFalse(service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
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
        Assert.assertFalse(service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
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
        service.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findPatientTherapies_existingUserDetailsAmbulatoryCard_therapiesList(User user)
            throws DaoException, ServiceException {
        Therapy first = new Therapy();
        Therapy second = new Therapy();
        List<Therapy> therapies = new ArrayList<>(Arrays.asList(first, second));
        Mockito.when(therapyDao.findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY))
                .thenReturn(therapies);
        Assert.assertEquals(service.
                findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY), therapies);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findPatientTherapies_existingUserDetailsStationaryCard_therapiesList(User user)
            throws DaoException, ServiceException {
        Therapy first = new Therapy();
        Therapy second = new Therapy();
        List<Therapy> therapies = new ArrayList<>(Arrays.asList(first, second));
        Mockito.when(therapyDao.findPatientTherapies(user.getUserDetails(), CardType.STATIONARY))
                .thenReturn(therapies);
        Assert.assertEquals(service.
                findPatientTherapies(user.getUserDetails(), CardType.STATIONARY), therapies);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findPatientTherapies_nonExistentUserDetailsAmbulatoryCard_emptyList(User user)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao.findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY))
                .thenReturn(new ArrayList<>());
        Assert.assertTrue(service.
                findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findPatientTherapies_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        Mockito.when(therapyDao.findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY))
                .thenThrow(DaoException.class);
        Assert.assertEquals(service.
                findPatientTherapies(user.getUserDetails(), CardType.AMBULATORY), new ArrayList<>());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findOpenDoctorTherapies_existingDoctorAndAmbulatoryTherapies_therapies(User user)
            throws DaoException, ServiceException {
        List<Therapy> therapies = new ArrayList<>(List.of(new Therapy()));
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(therapyDao.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY))
                .thenReturn(therapies);
        Assert.assertFalse(service.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findOpenDoctorTherapies_nonExistentDoctor_emptyList(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(service.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findOpenDoctorTherapies_daoException_serviceException(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertTrue(service.findOpenDoctorTherapies(user.getLogin(), CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void makeLastDiagnosisFinal_doctorAndPatientExistAmbulatoryCard_true(User doctor, User patient)
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
        Assert.assertTrue(service.makeLastDiagnosisFinal(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void makeLastDiagnosisFinal_diagnosisEmptyAmbulatoryCard_false(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertFalse(service.makeLastDiagnosisFinal(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            expectedExceptions = ServiceException.class, dependsOnGroups = "findCurrentPatientTherapy")
    public void makeLastDiagnosisFinal_daoException_serviceException(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertFalse(service.makeLastDiagnosisFinal(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void closeTherapy_doctorAndPatientExistAmbulatoryCard_true(User doctor, User patient)
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
        Mockito.when(therapyDao.setTherapyEndDate(doctor.getLogin(), patient.getLogin(),
                Date.valueOf(LocalDate.now()), CardType.AMBULATORY))
                .thenReturn(true);
        Assert.assertTrue(service.closeTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void closeTherapy_doctorAndPatientExistStationaryCard_true(User doctor, User patient)
            throws DaoException, ServiceException {
        Diagnosis diagnosis = new Diagnosis();
        Therapy therapy = new Therapy();
        therapy.setFinalDiagnosis(diagnosis);
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.STATIONARY))
                .thenReturn(Optional.of(therapy));
        Mockito.when(therapyDao.setTherapyEndDate(doctor.getLogin(), patient.getLogin(),
                Date.valueOf(LocalDate.now()), CardType.STATIONARY))
                .thenReturn(true);
        Assert.assertTrue(service.closeTherapy(doctor.getLogin(), patient.getLogin(), CardType.STATIONARY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnGroups = "findCurrentPatientTherapy")
    public void closeTherapy_finalDiagnosisEmptyAmbulatoryCard_false(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenReturn(Optional.of(doctor));
        Mockito.when(userDao.findByLogin(patient.getLogin()))
                .thenReturn(Optional.of(patient));
        Mockito.when(therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(Optional.of(new Therapy()));
        Assert.assertFalse(service.closeTherapy(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            expectedExceptions = ServiceException.class)
    public void closeTherapy_daoException_serviceException(User doctor, User patient)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(doctor.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertFalse(service.closeTherapy(doctor.getLogin(),
                patient.getLogin(), CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void assignProcedureToDiagnosis_procedure_true(Procedure procedure) throws DaoException, ServiceException {
        LocalDateTime time = LocalDateTime.now();
        String description = "description";
        String doctorLogin = "doctorLogin";
        String patientLogin = "patientLogin";
        ProcedureAssignment assignment = new ProcedureAssignment(procedure, description, time);
        Mockito.when(diagnosisDao.assignProcedureToLastDiagnosis(assignment, doctorLogin, patientLogin, CardType.AMBULATORY))
                .thenReturn(true);
        Assert.assertTrue(service.assignProcedureToLastDiagnosis(assignment, doctorLogin, patientLogin,
                CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void assignMedicamentToDiagnosis_medicament_true(Medicament medicament) throws DaoException, ServiceException {
        LocalDateTime time = LocalDateTime.now();
        String description = "description";
        String doctorLogin = "doctorLogin";
        String patientLogin = "patientLogin";
        MedicamentAssignment assignment = new MedicamentAssignment(medicament, description, time);
        Mockito.when(diagnosisDao.assignMedicamentToLastDiagnosis(assignment, doctorLogin, patientLogin, CardType.AMBULATORY))
                .thenReturn(true);
        Assert.assertTrue(service.assignMedicamentToLastDiagnosis(assignment, doctorLogin, patientLogin,
                CardType.AMBULATORY));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void findAllAssignmentProceduresToDiagnosis_validId_nonEmptyList(Procedure procedure) throws ServiceException, DaoException {
        int diagnosisId = 1;
        ProcedureAssignment assignment = new ProcedureAssignment(procedure, "d", LocalDateTime.now());
        Mockito.when(diagnosisDao.findAllAssignmentProcedures(diagnosisId))
                .thenReturn(List.of(assignment));
        Assert.assertTrue(service.findAllAssignmentProceduresToDiagnosis(diagnosisId).size() != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void findAllAssignmentMedicationsToDiagnosis_validId_nonEmptyList(Medicament medicament) throws ServiceException, DaoException {
        int diagnosisId = 1;
        MedicamentAssignment assignment = new MedicamentAssignment(medicament, "d", LocalDateTime.now());
        Mockito.when(diagnosisDao.findAllAssignmentMedications(diagnosisId))
                .thenReturn(List.of(assignment));
        Assert.assertTrue(service.findAllAssignmentMedicationsToDiagnosis(diagnosisId).size() != 0);
    }
}