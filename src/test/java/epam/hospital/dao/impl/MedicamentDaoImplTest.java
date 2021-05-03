package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.MedicamentDao;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.MedicamentDaoImpl;
import by.epam.hospital.dao.impl.TherapyDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.*;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Optional;

public class MedicamentDaoImplTest {
    private MedicamentDao medicamentDao;
    private Cleaner cleaner;
    private UserDao userDao;
    private TherapyDao therapyDao;

    @BeforeMethod
    private void setUp() {
        cleaner = new Cleaner();
        medicamentDao = new MedicamentDaoImpl();
        userDao = new UserDaoImpl();
        therapyDao = new TherapyDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void create_correctEntity_nonZero(Medicament medicament) throws DaoException {
        int id = medicamentDao.create(medicament);
        cleaner.delete(medicament);
        Assert.assertTrue(id != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void findById_correctId_entityPresent(Medicament medicament) throws DaoException {
        int id = medicamentDao.create(medicament);
        Optional<Medicament> procedureFromDb = medicamentDao.findById(id);
        cleaner.delete(medicament);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void findByName_correctName_entityPresent(Medicament medicament) throws DaoException {
        medicamentDao.create(medicament);
        Optional<Medicament> procedureFromDb = medicamentDao.findByName(medicament.getName());
        cleaner.delete(medicament);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void updateEnabledStatus_correctId_updatedMedicament(Medicament medicament) throws DaoException {
        int id = medicamentDao.create(medicament);
        medicament.setId(id);
        Optional<Medicament> procedureFromDb = medicamentDao.updateEnabledStatus(id, false);
        cleaner.delete(procedureFromDb.orElseThrow(() -> new DaoException("Update failed")));
        Assert.assertFalse(procedureFromDb.get().isEnabled());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedications")
    public void findAllByNamePartPaging_name_allMatches(Medicament[] medicaments) throws DaoException {
        for (Medicament medicament : medicaments) {
            medicamentDao.create(medicament);
        }
        PageResult<Medicament> pageResult = medicamentDao.findAllByNamePartPaging("thre", 1);
        for (Medicament medicament : medicaments) {
            cleaner.delete(medicament);
        }
        Assert.assertEquals(pageResult.getList().size(), 4);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void assignMedicamentToDiagnosis_validParameters_true(Diagnosis diagnosis, User patient) throws DaoException {
        String temp = "temp";
        String medicament = "medicament";
        CardType cardType = CardType.STATIONARY;

        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        Medicament m = new Medicament(temp + medicament, true);

        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);
        therapyDao.createStationaryTherapyWithDiagnosis(therapy, diagnosis);
        medicamentDao.create(m);

        boolean result = medicamentDao.assignMedicamentToDiagnosis(m.getName(), LocalDateTime.now(), temp,
                diagnosis.getDoctor().getLogin(), patient.getLogin(), cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(m);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);
        Assert.assertTrue(result);
    }
}
