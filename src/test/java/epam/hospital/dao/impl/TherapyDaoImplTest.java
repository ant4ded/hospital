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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Test(groups = {"dao", "TherapyDaoImplTest"}, dependsOnGroups = {"UserDaoImplTest"})
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
    public void closeTherapy_existingTherapy_true(User doctor, User patient) throws DaoException {
        userDao.create(patient);
        userDao.create(doctor);
        CardType cardType = CardType.AMBULATORY;

        int therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = new Therapy();
        therapy.setId(therapyId);
        boolean isClosed = therapyDao.setEndTherapy(doctor.getLogin(), patient.getLogin(), new Date(0), cardType);

        cleaner.delete(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertTrue(isClosed);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnMethods = "create_correctCreate_notZero")
    public void closeTherapy_nonExistentTherapy_false(User doctor, User patient) throws DaoException {
        userDao.create(patient);
        userDao.create(doctor);
        CardType cardType = CardType.AMBULATORY;

        int therapyId = therapyDao.create(doctor.getLogin(), patient.getLogin(), cardType);
        Therapy therapy = new Therapy();
        therapy.setId(therapyId);
        boolean isClosed = therapyDao
                .setEndTherapy(doctor.getLogin(), patient.getLogin(), new Date(0), CardType.STATIONARY);

        cleaner.delete(therapy, cardType);
        cleaner.delete(patient);
        cleaner.delete(doctor);

        Assert.assertFalse(isClosed);
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

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDoctorAndPatient",
            dependsOnMethods = "closeTherapy_existingTherapy_true")
    public void findOpenDoctorTherapies_correctFind_therapies(User doctor, User patient) throws DaoException {
        User patient1 = new User();
        patient1.setLogin(patient.getLogin() + "1");
        patient1.setPassword(patient.getLogin() + "1");
        User patient2 = new User();
        patient2.setLogin(patient.getLogin() + "2");
        patient2.setPassword(patient.getLogin() + "1");
        User patient3 = new User();
        patient3.setLogin(patient.getLogin() + "3");
        patient3.setPassword(patient.getLogin() + "1");
        userDao.create(patient1);
        userDao.create(patient2);
        userDao.create(patient3);
        userDao.create(doctor);
        CardType cardType = CardType.AMBULATORY;

        int therapyId = therapyDao.create(doctor.getLogin(), patient1.getLogin(), cardType);
        Therapy therapy1 = new Therapy();
        therapy1.setId(therapyId);
        therapyDao.setEndTherapy(doctor.getLogin(), patient1.getLogin(), new Date(0), cardType);
        therapyId = therapyDao.create(doctor.getLogin(), patient2.getLogin(), cardType);
        Therapy therapy2 = new Therapy();
        therapy2.setId(therapyId);
        therapyId = therapyDao.create(doctor.getLogin(), patient3.getLogin(), cardType);
        Therapy therapy3 = new Therapy();
        therapy3.setId(therapyId);

        List<Therapy> result = therapyDao.findOpenDoctorTherapies(doctor.getLogin(), cardType);

        cleaner.delete(therapy1, cardType);
        cleaner.delete(therapy2, cardType);
        cleaner.delete(therapy3, cardType);
        cleaner.delete(patient1);
        cleaner.delete(patient2);
        cleaner.delete(patient3);
        cleaner.delete(doctor);

        Assert.assertEquals(result.size(), 2);
    }
}
