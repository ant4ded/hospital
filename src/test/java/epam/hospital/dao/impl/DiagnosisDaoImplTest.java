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

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void create_correctCreate_notZero(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        userDao.create(doctor);
        userDao.create(patient);
        doctor = userDao.findByLogin(doctor.getLogin()).orElseThrow(DaoException::new);
        patient = userDao.findByLogin(patient.getLogin()).orElseThrow(DaoException::new);
        therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), cardType)
                .orElseThrow(DaoException::new);

        int diagnosisId = diagnosisDao.create(diagnosis, patient.getLogin(), therapy.getId());
        diagnosis.setId(diagnosisId);
        List<Diagnosis> diagnoses = new ArrayList<>(List.of(diagnosis));
        therapy.setDiagnoses(diagnoses);

        cleaner.delete(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
        Assert.assertTrue(diagnosisId != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class)
    public void create_incorrectDoctor_exception(Diagnosis diagnosis, User patient) throws DaoException {
        diagnosisDao.create(diagnosis, patient.getLogin(), 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class)
    public void create_incorrectIcd_exception(Diagnosis diagnosis, User patient) throws DaoException {
        Icd icd = diagnosis.getIcd();
        icd.setId(0);
        icd.setCode("");
        diagnosisDao.create(diagnosis, patient.getLogin(), 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            expectedExceptions = DaoException.class)
    public void create_incorrectTherapyId_exception(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        userDao.create(doctor);
        userDao.create(patient);
        doctor = userDao.findByLogin(doctor.getLogin()).orElseThrow(DaoException::new);
        patient = userDao.findByLogin(patient.getLogin()).orElseThrow(DaoException::new);

        try {
            diagnosisDao.create(diagnosis, patient.getLogin(), 0);
        } catch (DaoException e) {
            throw new DaoException(e);
        } finally {
            cleaner.delete(doctor);
            cleaner.delete(patient);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnMethods = "create_correctCreate_notZero")
    public void findById_correctFind_diagnosisPresent(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        userDao.create(doctor);
        userDao.create(patient);
        doctor = userDao.findByLogin(doctor.getLogin()).orElseThrow(DaoException::new);
        patient = userDao.findByLogin(patient.getLogin()).orElseThrow(DaoException::new);
        therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), cardType)
                .orElseThrow(DaoException::new);

        int diagnosisId = diagnosisDao.create(diagnosis, patient.getLogin(), therapy.getId());
        diagnosis.setId(diagnosisId);
        List<Diagnosis> diagnoses = new ArrayList<>(List.of(diagnosis));
        therapy.setDiagnoses(diagnoses);

        Optional<Diagnosis> optionalDiagnosis = diagnosisDao.findById(diagnosisId);

        cleaner.delete(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);

        Assert.assertTrue(optionalDiagnosis.isPresent());
    }

    @Test
    public void findById_incorrectId_diagnosisEmpty() throws DaoException {
        Assert.assertTrue(diagnosisDao.findById(0).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient",
            dependsOnMethods = "create_correctCreate_notZero")
    public void findByTherapyId_correctFind_afterCreateResultWithCreatedDiagnosis(Diagnosis diagnosis, User patient)
            throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        userDao.create(doctor);
        userDao.create(patient);
        doctor = userDao.findByLogin(doctor.getLogin()).orElseThrow(DaoException::new);
        patient = userDao.findByLogin(patient.getLogin()).orElseThrow(DaoException::new);
        therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), cardType)
                .orElseThrow(DaoException::new);

        List<Diagnosis> diagnoses = diagnosisDao.findByTherapyId(therapy.getId());
        if (diagnoses.size() != 0) {
            throw new DaoException("FindByTherapyId failed.");
        }
        int diagnosisId = diagnosisDao.create(diagnosis, patient.getLogin(), therapy.getId());
        diagnosis.setId(diagnosisId);
        diagnoses = diagnosisDao.findByTherapyId(therapy.getId());
        if (diagnoses.size() == 0 || !diagnoses.get(0).equals(diagnosis)) {
            throw new DaoException("FindByTherapyId failed.");
        }
        therapy.setDiagnoses(diagnoses);

        cleaner.delete(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
    }

    @Test
    public void findByTherapyId_incorrectTherapyId_emptyList() throws DaoException {
        Assert.assertTrue(diagnosisDao.findByTherapyId(0).isEmpty());
    }
}
