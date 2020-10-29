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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

        therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = therapyDao.find(doctor.getLogin(), patient.getLogin(), CardType.AMBULATORY)
                .orElseThrow(DaoException::new);
        Therapy therapyById = therapyDao.findById(therapy.getId(), CardType.AMBULATORY)
                .orElseThrow(DaoException::new);

        cleaner.delete(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);
        if (!therapy.equals(therapyById)) {
            throw new DaoException("Find or findById work incorrect");
        }
    }
}
