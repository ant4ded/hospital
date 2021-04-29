package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.MedicationsDao;
import by.epam.hospital.dao.impl.MedicationsDaoImpl;
import by.epam.hospital.entity.Medication;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

public class MedicationsDaoImplTest {
    private MedicationsDao dao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        cleaner = new Cleaner();
        dao = new MedicationsDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedication")
    public void create_correctEntity_nonZero(Medication medication) throws DaoException {
        int id = dao.create(medication);
        cleaner.delete(medication);
        Assert.assertTrue(id != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedication")
    public void findById_correctId_entityPresent(Medication medication) throws DaoException {
        int id = dao.create(medication);
        Optional<Medication> procedureFromDb = dao.findById(id);
        cleaner.delete(medication);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedication")
    public void findByName_correctName_entityPresent(Medication medication) throws DaoException {
        dao.create(medication);
        Optional<Medication> procedureFromDb = dao.findByName(medication.getName());
        cleaner.delete(medication);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedication")
    public void updateEnabledStatus_correctId_updatedMedication(Medication medication) throws DaoException {
        int id = dao.create(medication);
        medication.setId(id);
        Optional<Medication> procedureFromDb = dao.updateEnabledStatus(id, false);
        cleaner.delete(procedureFromDb.orElseThrow(() -> new DaoException("Update failed")));
        Assert.assertFalse(procedureFromDb.get().isEnabled());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedications")
    public void findAllByNamePartPaging_name_allMatches(Medication[] medications) throws DaoException {
        for (Medication medication : medications) {
            dao.create(medication);
        }
        List<Medication> medicationsFromDb = dao.findAllByNamePartPaging("thre", 0, 10);
        for (Medication medication : medications) {
            cleaner.delete(medication);
        }
        Assert.assertEquals(medicationsFromDb.size(), 4);
    }
}
