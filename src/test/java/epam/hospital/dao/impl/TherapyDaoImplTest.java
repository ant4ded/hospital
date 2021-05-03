package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.TherapyDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.*;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Test(groups = {"dao", "TherapyDaoImplTest"}, dependsOnGroups = {"UserDaoImplTest"})
public class TherapyDaoImplTest {
    private TherapyDao therapyDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        therapyDao = new TherapyDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            groups = "createTherapy")
    public void createAmbulatoryTherapyWithDiagnosis_correctCreate_therapyId(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);

        int therapyId = therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);
        Assert.assertNotEquals(therapyId, 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createAmbulatoryTherapyWithDiagnosis_nonExistentPatient_daoException(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        try {
            therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        } finally {
            cleaner.delete(diagnosis.getDoctor());
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createAmbulatoryTherapyWithDiagnosis_nonExistentDoctor_daoException(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);

        try {
            therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        } finally {
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createAmbulatoryTherapyWithDiagnosis_doctorWithoutDoctorRole_daoException
            (Diagnosis diagnosis, User patient) throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());

        try {
            therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        } finally {
            cleaner.delete(patient);
            cleaner.delete(diagnosis.getDoctor());
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            groups = "createTherapy")
    public void createStationaryTherapyWithDiagnosis_correctCreate_therapyId(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);

        int therapyId = therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);
        Assert.assertNotEquals(therapyId, 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientAmbulatoryTherapy_correctFind_therapyPresent(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientAmbulatoryTherapy(diagnosis.getDoctor().getLogin(), patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientAmbulatoryTherapy_nonExistentTherapy_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientAmbulatoryTherapy(diagnosis.getDoctor().getLogin(), patient.getLogin());

        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientAmbulatoryTherapy_nonExistentDoctor_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientAmbulatoryTherapy(diagnosis.getDoctor().getLogin().concat("x"),
                        patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientAmbulatoryTherapy_nonExistentPatient_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientAmbulatoryTherapy(diagnosis.getDoctor().getLogin(),
                        patient.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientStationaryTherapy_correctFind_therapyPresent(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientStationaryTherapy(diagnosis.getDoctor().getLogin(), patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientStationaryTherapy_nonExistentTherapy_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientStationaryTherapy(diagnosis.getDoctor().getLogin(), patient.getLogin());

        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientStationaryTherapy_nonExistentDoctor_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientStationaryTherapy(diagnosis.getDoctor().getLogin().concat("x"),
                        patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientStationaryTherapy_nonExistentPatient_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientStationaryTherapy(diagnosis.getDoctor().getLogin(),
                        patient.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setAmbulatoryTherapyEndDate_existingAmbulatoryTherapy_true(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isClosed = therapyDao.setAmbulatoryTherapyEndDate(doctor.getLogin(), patient.getLogin(), new Date(0));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setStationaryTherapyEndDate_existingStationaryTherapy_true(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isClosed = therapyDao.setStationaryTherapyEndDate(doctor.getLogin(), patient.getLogin(), new Date(0));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setAmbulatoryTherapyEndDate_nonExistentTherapy_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        boolean isClosed = therapyDao
                .setAmbulatoryTherapyEndDate(doctor.getLogin(), patient.getLogin(), new Date(0));

        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setStationaryTherapyEndDate_nonExistentTherapy_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        boolean isClosed = therapyDao
                .setStationaryTherapyEndDate(doctor.getLogin(), patient.getLogin(), new Date(0));

        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToAmbulatoryTherapy_existingTherapy_true(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isSuccess = therapyDao.setFinalDiagnosisToAmbulatoryTherapy(doctor.getLogin(), patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToAmbulatoryTherapy_nonExistentTherapy_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        boolean isSuccess = therapyDao.setFinalDiagnosisToAmbulatoryTherapy(doctor.getLogin(), patient.getLogin());

        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToAmbulatoryTherapy_nonExistentDoctor_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isSuccess = therapyDao.setFinalDiagnosisToAmbulatoryTherapy(doctor.getLogin().concat("x"),
                patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToAmbulatoryTherapy_nonExistentPatient_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isSuccess = therapyDao.setFinalDiagnosisToAmbulatoryTherapy(doctor.getLogin(),
                patient.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToStationaryTherapy_existingTherapy_true(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isSuccess = therapyDao.setFinalDiagnosisToStationaryTherapy(doctor.getLogin(), patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToStationaryTherapy_nonExistentTherapy_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        boolean isSuccess = therapyDao.setFinalDiagnosisToStationaryTherapy(doctor.getLogin(), patient.getLogin());

        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToStationaryTherapy_nonExistentDoctor_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isSuccess = therapyDao.setFinalDiagnosisToStationaryTherapy(doctor.getLogin().concat("x"),
                patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToStationaryTherapy_nonExistentPatient_false(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        boolean isSuccess = therapyDao.setFinalDiagnosisToStationaryTherapy(doctor.getLogin(),
                patient.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findAmbulatoryPatientTherapies_correctFind_ListWithCreatedTherapies(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy first = new Therapy();
        first.setCardType(cardType);
        first.setPatient(patient);
        first.setDoctor(doctor);
        int therapyId = therapyDao.createTherapyWithDiagnosis(first, diagnosis, cardType);
        first.setId(therapyId);
        Therapy second = new Therapy();
        second.setCardType(cardType);
        second.setPatient(patient);
        second.setDoctor(doctor);
        therapyId = therapyDao.createTherapyWithDiagnosis(second, diagnosis, cardType);
        second.setId(therapyId);

        List<Therapy> actual = therapyDao.findAmbulatoryPatientTherapies(patient.getUserDetails());

        cleaner.deleteTherapyWithDiagnosis(first, cardType);
        cleaner.deleteTherapyWithDiagnosis(second, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertEquals(actual.size(), 2);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", dependsOnGroups = "createTherapy")
    public void findAmbulatoryPatientTherapies_therapiesNotExist_ListWithCreatedTherapies(User patient)
            throws DaoException {
        userDao.createClientWithUserDetails(patient);

        List<Therapy> actual = therapyDao.findAmbulatoryPatientTherapies(patient.getUserDetails());

        cleaner.delete(patient);

        Assert.assertTrue(actual.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findAmbulatoryPatientTherapies_patientNotExist_ListWithCreatedTherapies
            (Diagnosis diagnosis, User patient) throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy first = new Therapy();
        first.setCardType(cardType);
        first.setPatient(patient);
        first.setDoctor(doctor);
        int therapyId = therapyDao.createTherapyWithDiagnosis(first, diagnosis, cardType);
        first.setId(therapyId);
        Therapy second = new Therapy();
        second.setCardType(cardType);
        second.setPatient(patient);
        second.setDoctor(doctor);
        therapyId = therapyDao.createTherapyWithDiagnosis(second, diagnosis, cardType);
        second.setId(therapyId);

        patient.getUserDetails().setFirstName("qwe");
        List<Therapy> actual = therapyDao.findAmbulatoryPatientTherapies(patient.getUserDetails());

        cleaner.deleteTherapyWithDiagnosis(first, cardType);
        cleaner.deleteTherapyWithDiagnosis(second, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(actual.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findStationaryPatientTherapies_correctFind_ListWithCreatedTherapies
            (Diagnosis diagnosis, User patient) throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy first = new Therapy();
        first.setCardType(cardType);
        first.setPatient(patient);
        first.setDoctor(doctor);
        int therapyId = therapyDao.createTherapyWithDiagnosis(first, diagnosis, cardType);
        first.setId(therapyId);
        Therapy second = new Therapy();
        second.setCardType(cardType);
        second.setPatient(patient);
        second.setDoctor(doctor);
        therapyId = therapyDao.createTherapyWithDiagnosis(second, diagnosis, cardType);
        second.setId(therapyId);

        List<Therapy> actual = therapyDao.findStationaryPatientTherapies(patient.getUserDetails());

        cleaner.deleteTherapyWithDiagnosis(first, cardType);
        cleaner.deleteTherapyWithDiagnosis(second, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertEquals(actual.size(), 2);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", dependsOnGroups = "createTherapy")
    public void findStationaryPatientTherapies_therapiesNotExist_ListWithCreatedTherapies(User patient)
            throws DaoException {
        userDao.createClientWithUserDetails(patient);

        List<Therapy> actual = therapyDao.findStationaryPatientTherapies(patient.getUserDetails());

        cleaner.delete(patient);

        Assert.assertTrue(actual.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findStationaryPatientTherapies_patientNotExist_ListWithCreatedTherapies
            (Diagnosis diagnosis, User patient) throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy first = new Therapy();
        first.setCardType(cardType);
        first.setPatient(patient);
        first.setDoctor(doctor);
        int therapyId = therapyDao.createTherapyWithDiagnosis(first, diagnosis, cardType);
        first.setId(therapyId);
        Therapy second = new Therapy();
        second.setCardType(cardType);
        second.setPatient(patient);
        second.setDoctor(doctor);
        therapyId = therapyDao.createTherapyWithDiagnosis(second, diagnosis, cardType);
        second.setId(therapyId);

        patient.getUserDetails().setFirstName("qwe");
        List<Therapy> actual = therapyDao.findStationaryPatientTherapies(patient.getUserDetails());

        cleaner.deleteTherapyWithDiagnosis(first, cardType);
        cleaner.deleteTherapyWithDiagnosis(second, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(actual.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findOpenDoctorAmbulatoryTherapies_correctFind_therapies(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        User patient1 = getAnotherPatient(patient, "1");
        User patient2 = getAnotherPatient(patient, "2");
        User patient3 = getAnotherPatient(patient, "3");

        userDao.createClientWithUserDetails(patient1);
        userDao.createClientWithUserDetails(patient2);
        userDao.createClientWithUserDetails(patient3);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy therapy1 = new Therapy();
        therapy1.setPatient(patient1);
        therapy1.setDoctor(doctor);
        therapy1.setId(therapyDao.createTherapyWithDiagnosis(therapy1, diagnosis, cardType));
        therapyDao.setAmbulatoryTherapyEndDate(doctor.getLogin(), patient1.getLogin(), new Date(0));
        Therapy therapy2 = new Therapy();
        therapy2.setPatient(patient2);
        therapy2.setDoctor(doctor);
        therapy2.setId(therapyDao.createTherapyWithDiagnosis(therapy2, diagnosis, cardType));
        Therapy therapy3 = new Therapy();
        therapy3.setPatient(patient3);
        therapy3.setDoctor(doctor);
        therapy3.setId(therapyDao.createTherapyWithDiagnosis(therapy3, diagnosis, cardType));

        List<Therapy> result = therapyDao.findOpenDoctorAmbulatoryTherapies(doctor.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy1, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy2, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy3, cardType);
        cleaner.delete(patient1);
        cleaner.delete(patient2);
        cleaner.delete(patient3);
        cleaner.delete(doctor);

        Assert.assertEquals(result.size(), 2);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findOpenDoctorAmbulatoryTherapies_nonExistentDoctor_therapies(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.AMBULATORY;
        User doctor = diagnosis.getDoctor();
        User patient1 = getAnotherPatient(patient, "1");
        User patient2 = getAnotherPatient(patient, "2");
        User patient3 = getAnotherPatient(patient, "3");

        userDao.createClientWithUserDetails(patient1);
        userDao.createClientWithUserDetails(patient2);
        userDao.createClientWithUserDetails(patient3);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy therapy1 = new Therapy();
        therapy1.setPatient(patient1);
        therapy1.setDoctor(doctor);
        therapy1.setId(therapyDao.createTherapyWithDiagnosis(therapy1, diagnosis, cardType));
        therapyDao.setAmbulatoryTherapyEndDate(doctor.getLogin(), patient1.getLogin(), new Date(0));
        Therapy therapy2 = new Therapy();
        therapy2.setPatient(patient2);
        therapy2.setDoctor(doctor);
        therapy2.setId(therapyDao.createTherapyWithDiagnosis(therapy2, diagnosis, cardType));
        Therapy therapy3 = new Therapy();
        therapy3.setPatient(patient3);
        therapy3.setDoctor(doctor);
        therapy3.setId(therapyDao.createTherapyWithDiagnosis(therapy3, diagnosis, cardType));

        List<Therapy> result = therapyDao.findOpenDoctorAmbulatoryTherapies(doctor.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy1, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy2, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy3, cardType);
        cleaner.delete(patient1);
        cleaner.delete(patient2);
        cleaner.delete(patient3);
        cleaner.delete(doctor);

        Assert.assertTrue(result.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnGroups = "createTherapy")
    public void findOpenDoctorAmbulatoryTherapies_nonExistentTherapies_therapies(User doctor)
            throws DaoException {
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        List<Therapy> result = therapyDao.findOpenDoctorAmbulatoryTherapies(doctor.getLogin());

        cleaner.delete(doctor);

        Assert.assertTrue(result.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findOpenDoctorStationaryTherapies_correctFind_therapies(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        User patient1 = getAnotherPatient(patient, "1");
        User patient2 = getAnotherPatient(patient, "2");
        User patient3 = getAnotherPatient(patient, "3");

        userDao.createClientWithUserDetails(patient1);
        userDao.createClientWithUserDetails(patient2);
        userDao.createClientWithUserDetails(patient3);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy therapy1 = new Therapy();
        therapy1.setPatient(patient1);
        therapy1.setDoctor(doctor);
        therapy1.setId(therapyDao.createTherapyWithDiagnosis(therapy1, diagnosis, cardType));
        therapyDao.setStationaryTherapyEndDate(doctor.getLogin(), patient1.getLogin(), new Date(0));
        Therapy therapy2 = new Therapy();
        therapy2.setPatient(patient2);
        therapy2.setDoctor(doctor);
        therapy2.setId(therapyDao.createTherapyWithDiagnosis(therapy2, diagnosis, cardType));
        Therapy therapy3 = new Therapy();
        therapy3.setPatient(patient3);
        therapy3.setDoctor(doctor);
        therapy3.setId(therapyDao.createTherapyWithDiagnosis(therapy3, diagnosis, cardType));

        List<Therapy> result = therapyDao.findOpenDoctorStationaryTherapies(doctor.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy1, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy2, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy3, cardType);
        cleaner.delete(patient1);
        cleaner.delete(patient2);
        cleaner.delete(patient3);
        cleaner.delete(doctor);

        Assert.assertEquals(result.size(), 2);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findOpenDoctorStationaryTherapies_nonExistentDoctor_therapies(Diagnosis diagnosis, User patient)
            throws DaoException {
        CardType cardType = CardType.STATIONARY;
        User doctor = diagnosis.getDoctor();
        User patient1 = getAnotherPatient(patient, "1");
        User patient2 = getAnotherPatient(patient, "2");
        User patient3 = getAnotherPatient(patient, "3");

        userDao.createClientWithUserDetails(patient1);
        userDao.createClientWithUserDetails(patient2);
        userDao.createClientWithUserDetails(patient3);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        Therapy therapy1 = new Therapy();
        therapy1.setPatient(patient1);
        therapy1.setDoctor(doctor);
        therapy1.setId(therapyDao.createTherapyWithDiagnosis(therapy1, diagnosis, cardType));
        therapyDao.setStationaryTherapyEndDate(doctor.getLogin(), patient1.getLogin(), new Date(0));
        Therapy therapy2 = new Therapy();
        therapy2.setPatient(patient2);
        therapy2.setDoctor(doctor);
        therapy2.setId(therapyDao.createTherapyWithDiagnosis(therapy2, diagnosis, cardType));
        Therapy therapy3 = new Therapy();
        therapy3.setPatient(patient3);
        therapy3.setDoctor(doctor);
        therapy3.setId(therapyDao.createTherapyWithDiagnosis(therapy3, diagnosis, cardType));

        List<Therapy> result = therapyDao.findOpenDoctorStationaryTherapies(doctor.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy1, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy2, cardType);
        cleaner.deleteTherapyWithDiagnosis(therapy3, cardType);
        cleaner.delete(patient1);
        cleaner.delete(patient2);
        cleaner.delete(patient3);
        cleaner.delete(doctor);

        Assert.assertTrue(result.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnGroups = "createTherapy")
    public void findOpenDoctorStationaryTherapies_nonExistentTherapies_therapies(User doctor)
            throws DaoException {
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);

        List<Therapy> result = therapyDao.findOpenDoctorStationaryTherapies(doctor.getLogin());

        cleaner.delete(doctor);

        Assert.assertTrue(result.isEmpty());
    }

    private User getAnotherPatient(User patient, String replacement) {
        User user = new User();
        user.setLogin(patient.getLogin().replace("T", replacement));
        user.setPassword(patient.getPassword().replace("T", replacement));
        user.getUserDetails().setPassportId(patient.getUserDetails().getPassportId().replace("T", replacement));
        user.getUserDetails().setGender(patient.getUserDetails().getGender());
        user.getUserDetails().setFirstName(patient.getUserDetails().getFirstName().replace("T", replacement));
        user.getUserDetails().setSurname(patient.getUserDetails().getSurname().replace("T", replacement));
        user.getUserDetails().setLastName(patient.getUserDetails().getLastName().replace("T", replacement));
        user.getUserDetails().setBirthday(patient.getUserDetails().getBirthday());
        user.getUserDetails().setAddress(patient.getUserDetails().getAddress());
        user.getUserDetails().setPhone(patient.getUserDetails().getPhone());
        return user;
    }
}
