package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DiagnosisDao;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DiagnosisDaoImpl;
import by.epam.hospital.dao.impl.TherapyDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.User;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

//@Test(groups = {"dao", "DiagnosisDaoImplTest"},
//        dependsOnGroups = {"UserDaoImplTest", "TherapyDaoImplTest", "IcdDaoImplTest"})
public class DiagnosisDaoImplTest {
    private DiagnosisDao diagnosisDao;
    private TherapyDao therapyDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        diagnosisDao = new DiagnosisDaoImpl();
        therapyDao = new TherapyDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void create_findAllByTherapyId_findById(Diagnosis diagnosis, User patient) throws DaoException {
        User doctor = diagnosis.getDoctor();
        CardType cardType = CardType.AMBULATORY;
        userDao.create(doctor);
        userDao.create(patient);
        doctor = userDao.findByLogin(doctor.getLogin()).orElseThrow(DaoException::new);
        patient = userDao.findByLogin(patient.getLogin()).orElseThrow(DaoException::new);
        therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = therapyDao.find(doctor.getLogin(), patient.getLogin(), cardType)
                .orElseThrow(DaoException::new);

        int diagnosisId = diagnosisDao.create(diagnosis, patient.getLogin(), therapy.getId());
        diagnosis.setId(diagnosisId);
        Diagnosis diagnosisFindById = diagnosisDao.findById(diagnosisId).orElseGet(Diagnosis::new);
        if (!diagnosis.equals(diagnosisFindById)){
            Assert.fail("FindById failed.");
        }

        List<Diagnosis> diagnoses = diagnosisDao.findAllByTherapyId(therapy.getId());
        if (diagnoses.size() == 0) {
            throw new DaoException("FindAllByTherapyId failed.");
        }
        therapy.setDiagnoses(diagnoses);

        cleaner.delete(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
    }
}
