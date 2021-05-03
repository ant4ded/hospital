package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DiagnosisDao;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DiagnosisDaoImpl;
import by.epam.hospital.dao.impl.TherapyDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.*;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Test(groups = {"dao", "DiagnosisDaoImplTest"},
        dependsOnGroups = {"UserDaoImplTest", "TherapyDaoImplTest", "IcdDaoImplTest"})
public class DiagnosisDaoImplTest {
    private DiagnosisDao diagnosisDao;
    private TherapyDao therapyDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        diagnosisDao = new DiagnosisDaoImpl();
        therapyDao = new TherapyDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            groups = "createDiagnosis")
    public void createAmbulatoryDiagnosis_correctCreate_notZero(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);

        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);

        therapy.setId(therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType));

        int diagnosisId = diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType);
        diagnosis.setId(diagnosisId);
        therapy.setDiagnoses(List.of(diagnosis));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
        Assert.assertTrue(diagnosisId != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createDiagnosis")
    public void createAmbulatoryDiagnosis_nonExistentDoctor_exception(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDiagnoses(new ArrayList<>());
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);
        therapy.setCardType(cardType);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapy.setId(therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType));

        String doctorLogin = doctor.getLogin();
        doctor.setLogin("1");
        try {
            diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType);
        } finally {
            doctor.setLogin(doctorLogin);
            cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
            cleaner.delete(doctor);
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createDiagnosis")
    public void createAmbulatoryDiagnosis_DoctorWithoutDoctorRole_exception(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDiagnoses(new ArrayList<>());
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);
        therapy.setCardType(cardType);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapy.setId(therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType));
        userDao.deleteUserRole(doctor.getLogin(), Role.DOCTOR);
        try {
            diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType);
        } finally {
            cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
            cleaner.delete(doctor);
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createDiagnosis")
    public void createAmbulatoryDiagnosis_nonExistentPatient_exception(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDiagnoses(new ArrayList<>());
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);
        therapy.setCardType(cardType);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        therapy.setId(therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType));
        try {
            diagnosisDao.createDiagnosis(diagnosis, patient.getLogin() + "1", cardType);
        } finally {
            cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
            cleaner.delete(diagnosis.getDoctor());
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class, groups = "createDiagnosis")
    public void createAmbulatoryDiagnosis_nonExistentTherapy_exception(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setDiagnoses(new ArrayList<>());
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);
        therapy.setCardType(cardType);
        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);

        try {
            diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType);
        } finally {
            cleaner.delete(diagnosis.getDoctor());
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            groups = "createDiagnosis")
    public void createStationaryDiagnosis_correctCreate_notZero(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.STATIONARY;
        Therapy therapy = new Therapy();
        therapy.setDoctor(doctor);
        therapy.setPatient(patient);
        therapy.setCardType(cardType);

        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);

        therapy.setId(therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType));

        int diagnosisId = diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType);
        diagnosis.setId(diagnosisId);
        therapy.setDiagnoses(List.of(diagnosis));

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
        Assert.assertTrue(diagnosisId != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createDiagnosis")
    public void findById_correctFind_diagnosisPresent(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setPatient(patient);
        therapy.setDoctor(doctor);

        userDao.createClientWithUserDetails(patient);
        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        int diagnosisId = diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType);

        Optional<Diagnosis> optionalDiagnosis = diagnosisDao.findById(diagnosisId);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);

        Assert.assertTrue(optionalDiagnosis.isPresent());
    }

    @Test
    public void findById_incorrectId_diagnosisEmpty() throws DaoException {
        Assert.assertTrue(diagnosisDao.findById(0).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnGroups = "createDiagnosis")
    public void findByTherapyId_correctFind_afterCreateResultWithCreatedDiagnosis(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        Therapy therapy = new Therapy();
        therapy.setPatient(patient);
        therapy.setDoctor(doctor);

        userDao.createClientWithUserDetails(doctor);
        userDao.addUserRole(doctor.getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);

        int therapyId = therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        diagnosisDao.createDiagnosis(diagnosis, patient.getLogin(), cardType);

        List<Diagnosis> diagnoses = diagnosisDao.findByTherapyId(therapyId);
        if (diagnoses.size() != 2) {
            throw new DaoException("FindByTherapyId failed.");
        }

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
    }

    @Test
    public void findByTherapyId_incorrectTherapyId_emptyList() throws DaoException {
        Assert.assertTrue(diagnosisDao.findByTherapyId(0).isEmpty());
    }
}
