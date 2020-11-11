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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Test(groups = {"dao", "TherapyDaoImplTest"}, dependsOnGroups = {"UserDaoImplTest"})
public class TherapyDaoImplTest {
    private TherapyDao therapyDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        therapyDao = new TherapyDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient")
    public void create_correctCreate_notZero(User doctor, User patient) throws DaoException {
        userDao.create(patient);
        userDao.create(doctor);
        CardType cardType = CardType.AMBULATORY;

        int therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = new Therapy();
        therapy.setId(therapyId);

        cleaner.delete(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(therapyId != 0);
    }

    @Test(expectedExceptions = DaoException.class)
    public void create_nonExistentDoctor_exception() throws DaoException {
        therapyDao.create("1", "", CardType.AMBULATORY);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = DaoException.class)
    public void create_nonExistentPatient_exception(User doctor) throws DaoException {
        therapyDao.create(doctor.getLogin(), "1", CardType.AMBULATORY);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnMethods = "create_correctCreate_notZero")
    public void find_correctFind_therapyPresent(User doctor, User patient) throws DaoException {
        userDao.create(doctor);
        userDao.create(patient);
        CardType cardType = CardType.AMBULATORY;

        int therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Optional<Therapy> optionalTherapy = therapyDao.findCurrentPatientTherapy(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = new Therapy();
        therapy.setId(therapyId);
        cleaner.delete(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(optionalTherapy.isPresent());
    }

    @Test
    public void find_incorrectLogins_therapyPresent() throws DaoException {
        Assert.assertTrue(therapyDao
                .findCurrentPatientTherapy("", "", CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnMethods = "create_correctCreate_notZero")
    public void findById_correctFind_therapyPresent(User doctor, User patient) throws DaoException {
        userDao.create(doctor);
        userDao.create(patient);
        CardType cardType = CardType.AMBULATORY;

        int therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Optional<Therapy> optionalTherapy = therapyDao.findById(therapyId, cardType);
        Therapy therapy = new Therapy();
        therapy.setId(therapyId);
        cleaner.delete(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(optionalTherapy.isPresent());
    }

    @Test
    public void findById_incorrectLogins_therapyPresent() throws DaoException {
        Assert.assertTrue(therapyDao.findById(0, CardType.AMBULATORY).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnMethods = "create_correctCreate_notZero")
    public void findPatientTherapies_correctFind_ListWithCreatedTherapies(User doctor, User patient)
            throws DaoException {
        patient.setId(userDao.create(patient));
        doctor.setId(userDao.create(doctor));
        CardType cardType = CardType.AMBULATORY;
        List<Therapy> expected = new ArrayList<>();

        int therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy first = new Therapy();
        first.setId(therapyId);
        first.setCardType(cardType);
        first.setPatient(patient);
        first.setDoctor(doctor);
        therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy second = new Therapy();
        second.setId(therapyId);
        second.setCardType(cardType);
        second.setPatient(patient);
        second.setDoctor(doctor);
        expected.add(first);
        expected.add(second);

        List<Therapy> actual = therapyDao.findPatientTherapies(patient.getLogin(), cardType);

        cleaner.delete(first, cardType);
        cleaner.delete(second, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertEquals(actual, expected);
    }
}
