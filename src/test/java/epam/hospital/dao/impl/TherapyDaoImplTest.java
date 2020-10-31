package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.TherapyDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.User;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

@Test(groups = {"dao", "TherapyDaoImplTest"}, dependsOnGroups = {"UserDaoImplTest"})
public class TherapyDaoImplTest {
    private TherapyDao therapyDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        therapyDao = new TherapyDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient")
    public void create_find_findById(User doctor, User patient) throws DaoException {
        userDao.create(doctor);
        userDao.create(patient);
        CardType cardType = CardType.AMBULATORY;

        int therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);

        Optional<Therapy> therapyById = therapyDao.findById(therapyId, CardType.AMBULATORY);
        if (therapyById.isEmpty()) {
            Assert.fail("FindById failed.");
        }

        Optional<Therapy> therapy = therapyDao.find(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY);
        if (therapy.isEmpty()) {
            Assert.fail("Find failed.");
        }
        if (!therapy.get().equals(therapyById.get())){
            Assert.fail("Find and FindById failed. They returns non equals therapy.");
        }


        cleaner.delete(therapy.get(), cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);
        if (!therapy.equals(therapyById)) {
            throw new DaoException("find or findById work incorrect.");
        }
    }
}
