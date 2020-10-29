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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

@Test(groups = {"dao", "DiagnosisDaoImplTest"},
        dependsOnGroups = {"UserDaoImplTest", "TherapyDaoImplTest", "IcdDaoImplTest"})
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
        doctor = userDao.find(doctor.getLogin()).orElseThrow(DaoException::new);
        patient = userDao.find(patient.getLogin()).orElseThrow(DaoException::new);
        diagnosis.setDoctor(doctor);
        therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = therapyDao.find(doctor.getLogin(), patient.getLogin(), cardType)
                .orElseThrow(DaoException::new);

        diagnosisDao.create(diagnosis, patient.getLogin(), therapy.getId());
        ArrayList<Diagnosis> diagnoses = (ArrayList<Diagnosis>) diagnosisDao.findAllByTherapyId(therapy.getId());
        if (diagnoses.size() == 0) {
            throw new DaoException("Incorrect work findAllByTherapyId or create.");
        }
        therapy.setDiagnoses(diagnoses);
        Diagnosis diagnosis1 = diagnosisDao.findById(diagnoses.get(0).getId()).orElseGet(Diagnosis::new);
        if (!diagnoses.get(0).equals(diagnosis1)) {
            throw new DaoException("Incorrect work find or findById");
        }

        cleaner.delete(therapy, cardType);
        cleaner.delete(doctor);
        cleaner.delete(patient);
    }
}
