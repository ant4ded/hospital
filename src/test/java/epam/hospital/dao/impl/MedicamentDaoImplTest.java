package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.MedicamentDao;
import by.epam.hospital.dao.impl.MedicamentDaoImpl;
import by.epam.hospital.entity.Medicament;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

public class MedicamentDaoImplTest {
    private MedicamentDao dao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        cleaner = new Cleaner();
        dao = new MedicamentDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void create_correctEntity_nonZero(Medicament medicament) throws DaoException {
        int id = dao.create(medicament);
        cleaner.delete(medicament);
        Assert.assertTrue(id != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void findById_correctId_entityPresent(Medicament medicament) throws DaoException {
        int id = dao.create(medicament);
        Optional<Medicament> procedureFromDb = dao.findById(id);
        cleaner.delete(medicament);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void findByName_correctName_entityPresent(Medicament medicament) throws DaoException {
        dao.create(medicament);
        Optional<Medicament> procedureFromDb = dao.findByName(medicament.getName());
        cleaner.delete(medicament);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedicament")
    public void updateEnabledStatus_correctId_updatedMedicament(Medicament medicament) throws DaoException {
        int id = dao.create(medicament);
        medicament.setId(id);
        Optional<Medicament> procedureFromDb = dao.updateEnabledStatus(id, false);
        cleaner.delete(procedureFromDb.orElseThrow(() -> new DaoException("Update failed")));
        Assert.assertFalse(procedureFromDb.get().isEnabled());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectMedications")
    public void findAllByNamePartPaging_name_allMatches(Medicament[] medicaments) throws DaoException {
        for (Medicament medicament : medicaments) {
            dao.create(medicament);
        }
        List<Medicament> medicationsFromDb = dao.findAllByNamePartPaging("thre", 0, 10);
        for (Medicament medicament : medicaments) {
            cleaner.delete(medicament);
        }
        Assert.assertEquals(medicationsFromDb.size(), 4);
    }
}
