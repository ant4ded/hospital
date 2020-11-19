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
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);

        int therapyId = therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.AMBULATORY);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);
        Assert.assertNotEquals(therapyId, 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createAmbulatoryTherapyWithDiagnosis_nonExistentPatient_daoException(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        try {
            therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);
        } finally {
            cleaner.delete(diagnosis.getDoctor());
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createAmbulatoryTherapyWithDiagnosis_nonExistentDoctor_daoException(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);

        try {
            therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);
        } finally {
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createAmbulatoryTherapyWithDiagnosis_doctorWithoutDoctorRole_daoException
            (Diagnosis diagnosis, User patient) throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());

        try {
            therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);
        } finally {
            cleaner.delete(patient);
            cleaner.delete(diagnosis.getDoctor());
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            groups = "createTherapy")
    public void createStationaryTherapyWithDiagnosis_correctCreate_therapyId(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);

        int therapyId = therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.STATIONARY);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);
        Assert.assertNotEquals(therapyId, 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createStationaryTherapyWithDiagnosis_nonExistentPatient_daoException(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        try {
            therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);
        } finally {
            cleaner.delete(diagnosis.getDoctor());
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createStationaryTherapyWithDiagnosis_nonExistentDoctor_daoException(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);

        try {
            therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);
        } finally {
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createTherapy")
    public void createStationaryTherapyWithDiagnosis_doctorWithoutDoctorRole_daoException
            (Diagnosis diagnosis, User patient) throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());

        try {
            therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);
        } finally {
            cleaner.delete(diagnosis.getDoctor());
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientAmbulatoryTherapy_correctFind_therapyPresent(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientAmbulatoryTherapy(diagnosis.getDoctor().getLogin(), patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.AMBULATORY);
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
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientAmbulatoryTherapy(diagnosis.getDoctor().getLogin().concat("x"),
                        patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.AMBULATORY);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientAmbulatoryTherapy_nonExistentPatient_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientAmbulatoryTherapy(diagnosis.getDoctor().getLogin(),
                        patient.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.AMBULATORY);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

//////////////////////////////////////////////////////

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientStationaryTherapy_correctFind_therapyPresent(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientStationaryTherapy(diagnosis.getDoctor().getLogin(), patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.STATIONARY);
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
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientStationaryTherapy(diagnosis.getDoctor().getLogin().concat("x"),
                        patient.getLogin());

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.STATIONARY);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findCurrentPatientStationaryTherapy_nonExistentPatient_emptyTherapy(Diagnosis diagnosis, User patient)
            throws DaoException {
        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);

        Optional<Therapy> optionalTherapy = therapyDao
                .findCurrentPatientStationaryTherapy(diagnosis.getDoctor().getLogin(),
                        patient.getLogin().concat("x"));

        cleaner.deleteTherapyWithDiagnosis(therapy, CardType.STATIONARY);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);

        Assert.assertTrue(optionalTherapy.isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setEndTherapy_existingTherapy_true(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);
        boolean isClosed = therapyDao.setEndTherapy(doctor.getLogin(), patient.getLogin(), new Date(0), cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setEndTherapy_nonExistentTherapy_false(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        boolean isClosed = therapyDao
                .setEndTherapy(doctor.getLogin(), patient.getLogin(), new Date(0), CardType.AMBULATORY);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void setFinalDiagnosisToTherapy_existingTherapy_true(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);
        boolean isClosed = therapyDao.setFinalDiagnosisToTherapy(doctor.getLogin(), patient.getLogin(), cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findById_correctFind_therapyPresent(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        int therapyId = therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy, diagnosis);
        Optional<Therapy> optionalTherapy = therapyDao.findById(therapyId, cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(optionalTherapy.isPresent());
    }

    @Test
    public void findById_incorrectLogins_therapyPresent() throws DaoException {
        Assert.assertTrue(therapyDao.findById(0, CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findPatientTherapies_correctFind_ListWithCreatedTherapies(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        CardType cardType = CardType.AMBULATORY;

        Therapy first = new Therapy();
        first.setCardType(cardType);
        first.setPatient(patient);
        first.setDoctor(doctor);
        int therapyId = therapyDao.createAmbulatoryTherapyWithDiagnosis(first, diagnosis);
        first.setId(therapyId);
        therapyDao.setEndTherapy(doctor.getLogin(), patient.getLogin(), new Date(0), CardType.AMBULATORY);
        Therapy second = new Therapy();
        second.setCardType(cardType);
        second.setPatient(patient);
        second.setDoctor(doctor);
        therapyId = therapyDao.createAmbulatoryTherapyWithDiagnosis(second, diagnosis);
        second.setId(therapyId);

        List<Therapy> actual = therapyDao.findPatientTherapies(patient.getLogin(), cardType);

        cleaner.deleteTherapyWithDiagnosis(first, cardType);
        cleaner.deleteTherapyWithDiagnosis(second, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertEquals(actual.size(), 2);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createTherapy")
    public void findOpenDoctorTherapies_correctFind_therapies(Diagnosis diagnosis, User patient) throws DaoException {
        User patient1 = new User();
        User doctor = diagnosis.getDoctor();
        patient1.setLogin(patient.getLogin() + "1");
        patient1.setPassword(patient.getLogin() + "1");
        patient1.getUserDetails().setPassportId(patient.getUserDetails().getPassportId().replace("T", "1"));
        patient1.getUserDetails().setGender(patient.getUserDetails().getGender());
        patient1.getUserDetails().setFirstName(patient.getUserDetails().getFirstName() + "1");
        patient1.getUserDetails().setSurname(patient.getUserDetails().getSurname());
        patient1.getUserDetails().setLastName(patient.getUserDetails().getLastName());
        patient1.getUserDetails().setBirthday(patient.getUserDetails().getBirthday());
        patient1.getUserDetails().setAddress(patient.getUserDetails().getAddress());
        patient1.getUserDetails().setPhone(patient.getUserDetails().getPhone());
        User patient2 = new User();
        patient2.setLogin(patient.getLogin() + "2");
        patient2.setPassword(patient.getLogin() + "2");
        patient2.getUserDetails().setPassportId(patient.getUserDetails().getPassportId().replace("T", "2"));
        patient2.getUserDetails().setGender(patient.getUserDetails().getGender());
        patient2.getUserDetails().setFirstName(patient.getUserDetails().getFirstName() + "2");
        patient2.getUserDetails().setSurname(patient.getUserDetails().getSurname());
        patient2.getUserDetails().setLastName(patient.getUserDetails().getLastName());
        patient2.getUserDetails().setBirthday(patient.getUserDetails().getBirthday());
        patient2.getUserDetails().setAddress(patient.getUserDetails().getAddress());
        patient2.getUserDetails().setPhone(patient.getUserDetails().getPhone());
        User patient3 = new User();
        patient3.setLogin(patient.getLogin() + "3");
        patient3.setPassword(patient.getLogin() + "3");
        patient3.getUserDetails().setPassportId(patient.getUserDetails().getPassportId().replace("T", "3"));
        patient3.getUserDetails().setGender(patient.getUserDetails().getGender());
        patient3.getUserDetails().setFirstName(patient.getUserDetails().getFirstName() + "3");
        patient3.getUserDetails().setSurname(patient.getUserDetails().getSurname());
        patient3.getUserDetails().setLastName(patient.getUserDetails().getLastName());
        patient3.getUserDetails().setBirthday(patient.getUserDetails().getBirthday());
        patient3.getUserDetails().setAddress(patient.getUserDetails().getAddress());
        patient3.getUserDetails().setPhone(patient.getUserDetails().getPhone());

        userDao.createClientWithUserDetails(patient1);
        userDao.createClientWithUserDetails(patient2);
        userDao.createClientWithUserDetails(patient3);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        CardType cardType = CardType.AMBULATORY;

        Therapy therapy1 = new Therapy();
        therapy1.setPatient(patient1);
        therapy1.setDoctor(doctor);
        therapy1.setId(therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy1, diagnosis));
        therapyDao.setEndTherapy(doctor.getLogin(), patient1.getLogin(), new Date(0), cardType);
        Therapy therapy2 = new Therapy();
        therapy2.setPatient(patient2);
        therapy2.setDoctor(doctor);
        therapy2.setId(therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy2, diagnosis));
        Therapy therapy3 = new Therapy();
        therapy3.setPatient(patient3);
        therapy3.setDoctor(doctor);
        therapy3.setId(therapyDao.createAmbulatoryTherapyWithDiagnosis(therapy3, diagnosis));

        List<Therapy> result = therapyDao.findOpenDoctorTherapies(doctor.getLogin(), cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy1, CardType.AMBULATORY);
        cleaner.deleteTherapyWithDiagnosis(therapy2, CardType.AMBULATORY);
        cleaner.deleteTherapyWithDiagnosis(therapy3, CardType.AMBULATORY);
        cleaner.delete(patient1);
        cleaner.delete(patient2);
        cleaner.delete(patient3);
        cleaner.delete(doctor);

        Assert.assertEquals(result.size(), 2);
    }
}
