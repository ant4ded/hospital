package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.ProceduresDao;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.ProceduresDaoImpl;
import by.epam.hospital.dao.impl.TherapyDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.*;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProceduresDaoImplTest {
    private ProceduresDao proceduresDao;
    private Cleaner cleaner;
    private UserDao userDao;
    private TherapyDao therapyDao;

    @BeforeMethod
    private void setUp() {
        cleaner = new Cleaner();
        proceduresDao = new ProceduresDaoImpl();
        userDao = new UserDaoImpl();
        therapyDao = new TherapyDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void create_correctEntity_nonZero(Procedure procedure) throws DaoException {
        int id = proceduresDao.create(procedure);
        cleaner.delete(procedure);
        Assert.assertTrue(id != 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void findById_correctId_entityPresent(Procedure procedure) throws DaoException {
        int id = proceduresDao.create(procedure);
        Optional<Procedure> procedureFromDb = proceduresDao.findById(id);
        cleaner.delete(procedure);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void findByName_correctName_entityPresent(Procedure procedure) throws DaoException {
        proceduresDao.create(procedure);
        Optional<Procedure> procedureFromDb = proceduresDao.findByName(procedure.getName());
        cleaner.delete(procedure);
        Assert.assertTrue(procedureFromDb.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void updateCost_correctProcedureAndId_updatedProcedure(Procedure procedure) throws DaoException {
        int id = proceduresDao.create(procedure);
        procedure.setId(id);
        procedure.setCost(200);
        Optional<Procedure> procedureFromDb = proceduresDao.updateCost(id, 200);
        cleaner.delete(procedureFromDb.orElseThrow(() -> new DaoException("Update failed")));
        Assert.assertEquals(procedureFromDb.get(), procedure);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void updateEnabledStatus_correctId_updatedProcedure(Procedure procedure) throws DaoException {
        int id = proceduresDao.create(procedure);
        procedure.setId(id);
        Optional<Procedure> procedureFromDb = proceduresDao.updateEnabledStatus(id, false);
        cleaner.delete(procedureFromDb.orElseThrow(() -> new DaoException("Update failed")));
        Assert.assertFalse(procedureFromDb.get().isEnabled());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedures")
    public void findAllByNamePartPaging_name_allMatches(Procedure[] procedures) throws DaoException {
        for (Procedure procedure : procedures) {
            proceduresDao.create(procedure);
        }
        PageResult<Procedure> pageResult = proceduresDao.findAllByNamePartPaging("thre", 1);
        for (Procedure procedure : procedures) {
            cleaner.delete(procedure);
        }
        Assert.assertEquals(pageResult.getList().size(), 4);
    }


    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void assignProcedureToDiagnosis_validParameters_true(Diagnosis diagnosis, User patient) throws DaoException {
        String temp = "temp";
        String procedure = "procedure";
        CardType cardType = CardType.STATIONARY;

        Therapy therapy = new Therapy();
        therapy.setDoctor(diagnosis.getDoctor());
        therapy.setPatient(patient);
        Procedure p = new Procedure(temp + procedure, 200, true);

        userDao.createClientWithUserDetails(diagnosis.getDoctor());
        userDao.addUserRole(diagnosis.getDoctor().getLogin(), Role.DOCTOR);
        userDao.createClientWithUserDetails(patient);
        therapyDao.createTherapyWithDiagnosis(therapy, diagnosis, cardType);
        proceduresDao.create(p);

        boolean result = proceduresDao.assignProcedureToDiagnosis(p.getName(), LocalDateTime.now(), temp,
                diagnosis.getDoctor().getLogin(), patient.getLogin(), cardType);

        cleaner.deleteTherapyWithDiagnosis(therapy, cardType);
        cleaner.delete(p);
        cleaner.delete(diagnosis.getDoctor());
        cleaner.delete(patient);
        Assert.assertTrue(result);
    }
}
