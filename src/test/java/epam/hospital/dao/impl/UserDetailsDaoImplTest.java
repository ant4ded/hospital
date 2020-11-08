package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

@Test(groups = {"dao", "UserDetailsDaoImplTest"}, dependsOnGroups = "UserDaoImplTest")
public class UserDetailsDaoImplTest {
    private UserDetailsDao userDetailsDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        userDetailsDao = new UserDetailsDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void create_correctCreate_true(User user) throws DaoException {
        user.getUserDetails().setUserId(userDao.create(user));

        boolean result = userDetailsDao.create(user.getUserDetails());
        cleaner.delete(user);

        Assert.assertTrue(result);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", expectedExceptions = DaoException.class)
    public void create_nonExistentUser_exception(User user) throws DaoException {
        userDetailsDao.create(user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "create_correctCreate_true")
    public void update_correctUpdate_true(User user) throws DaoException {
        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setPassportId(user.getUserDetails().getPassportId());
        newUserDetails.setGender(UserDetails.Gender.MALE);
        newUserDetails.setFirstName(user.getUserDetails().getFirstName());
        newUserDetails.setSurname(user.getUserDetails().getSurname());
        newUserDetails.setLastName(user.getUserDetails().getLastName());
        newUserDetails.setBirthday(user.getUserDetails().getBirthday());
        newUserDetails.setAddress(user.getUserDetails().getAddress());
        newUserDetails.setPhone(user.getUserDetails().getPhone());

        user.getUserDetails().setUserId(userDao.create(user));
        newUserDetails.setUserId(user.getUserDetails().getUserId());
        UserDetails updatedUserDetails = userDetailsDao.update(user.getUserDetails(), newUserDetails);
        cleaner.delete(user);

        Assert.assertEquals(newUserDetails, updatedUserDetails);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void update_nonExistentUserId_exception(User user) throws DaoException {
        Assert.assertEquals(userDetailsDao
                .update(user.getUserDetails(), user.getUserDetails()), user.getUserDetails());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "create_correctCreate_true")
    public void findByUserId_correctFind_userDetailsPresent(User user) throws DaoException {
        user.getUserDetails().setUserId(userDao.create(user));

        userDetailsDao.create(user.getUserDetails());
        Optional<UserDetails> userDetails = userDetailsDao.findByUserId(user.getUserDetails().getUserId());
        cleaner.delete(user);

        Assert.assertTrue(userDetails.isPresent());
    }

    @Test
    public void findByUserId_nonExistentId_userDetailsEmpty() throws DaoException {
        Assert.assertTrue(userDetailsDao.findByUserId(0).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "create_correctCreate_true")
    public void findByRegistrationData_correctFind_userDetailsPresent(User user) throws DaoException {
        user.getUserDetails().setUserId(userDao.create(user));
        UserDetails userDetails = user.getUserDetails();

        userDetailsDao.create(user.getUserDetails());
        Optional<UserDetails> optionalUserDetails = userDetailsDao
                .findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                        userDetails.getLastName(), userDetails.getBirthday());
        cleaner.delete(user);

        Assert.assertTrue(optionalUserDetails.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData_nonExistentUserDetails_userDetailsEmpty(User user) throws DaoException {
        UserDetails userDetails = user.getUserDetails();

        Optional<UserDetails> optionalUserDetails = userDetailsDao
                .findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                        userDetails.getLastName(), userDetails.getBirthday());

        Assert.assertTrue(optionalUserDetails.isEmpty());
    }
}
