package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

@Test(groups = "UserDaoImplTest")
public class UserDaoImplTest {
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        cleaner = new Cleaner();
        userDao = new UserDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void createClientWithUserDetails_correctCreate_nonZero(User user) throws DaoException {
        int userId = userDao.createClientWithUserDetails(user);
        cleaner.delete(user);
        Assert.assertTrue(userId != 0);
    }

    @Test
    public void createClientWithUserDetails_nullField_zero() throws DaoException {
        UserDetails userDetails = new UserDetails();
        userDetails.setGender(UserDetails.Gender.MALE);
        User user = new User();
        user.setUserDetails(userDetails);
        Assert.assertEquals(userDao.createClientWithUserDetails(user), 0);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "createClientWithUserDetails_correctCreate_nonZero")
    public void findByLogin_correctFind_userPresent(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        Optional<User> optionalUser = userDao.findByLogin(user.getLogin());

        cleaner.delete(user);

        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test
    public void findByLogin_nonExistentLogin_userEmpty() throws DaoException {
        Assert.assertTrue(userDao.findByLogin("").isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "createClientWithUserDetails_correctCreate_nonZero")
    public void findByLoginWithUserDetails_correctFind_userPresent(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        Optional<User> optionalUser = userDao.findByLoginWithUserDetails(user.getLogin());

        cleaner.delete(user);

        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test
    public void findByLoginWithUserDetails_nonExistentLogin_userEmpty() throws DaoException {
        Assert.assertTrue(userDao.findByLoginWithUserDetails("").isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "createClientWithUserDetails_correctCreate_nonZero")
    public void findById_correctFind_userPresent(User user) throws DaoException {
        int userId = userDao.createClientWithUserDetails(user);
        Optional<User> optionalUser = userDao.findById(userId);

        cleaner.delete(user);

        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test
    public void findById_nonExistentId_userEmpty() throws DaoException {
        Assert.assertTrue(userDao.findById(0).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "createClientWithUserDetails_correctCreate_nonZero")
    public void findByIdWithUserDetails_correctFind_userPresent(User user) throws DaoException {
        int userId = userDao.createClientWithUserDetails(user);
        Optional<User> optionalUser = userDao.findByIdWithUserDetails(userId);

        cleaner.delete(user);

        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test
    public void findByIdWithUserDetails_nonExistentId_userEmpty() throws DaoException {
        Assert.assertTrue(userDao.findByIdWithUserDetails(0).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "createClientWithUserDetails_correctCreate_nonZero")
    public void findUserWithUserDetailsByPassportData_correctFind_userPresent(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        Optional<User> optionalUser = userDao
                .findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
                        user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                        user.getUserDetails().getBirthday());

        cleaner.delete(user);

        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findUserWithUserDetailsByPassportData_nonExistentId_userEmpty(User user) throws DaoException {
        Assert.assertTrue(userDao.findUserWithUserDetailsByPassportData(user.getUserDetails().getFirstName(),
                user.getUserDetails().getSurname(), user.getUserDetails().getLastName(),
                user.getUserDetails().getBirthday()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"createClientWithUserDetails_correctCreate_nonZero",
                    "findByLogin_correctFind_userPresent"})
    public void update_correctUpdate_updatedUser(User user) throws DaoException {
        User newUser = new User();
        newUser.setUserDetails(user.getUserDetails());
        newUser.setPassword(user.getPassword());
        newUser.setRoles(user.getRoles());
        newUser.setLogin(user.getLogin() + "1");
        user.setId(userDao.createClientWithUserDetails(user));
        newUser.setId(user.getId());

        Optional<User> updatedUser = userDao.updateLoginAndPassword(user.getLogin(), newUser);
        cleaner.delete(newUser);

        Assert.assertTrue(updatedUser.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void update_nonExistentLogin_exception(User user) throws DaoException {
        Assert.assertFalse(userDao.updateLoginAndPassword(user.getLogin(), new User()).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"createClientWithUserDetails_correctCreate_nonZero"})
    public void addUserRole_correctAdd_true(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        boolean isSuccess = userDao.addUserRole(user.getLogin(), Role.DOCTOR);

        cleaner.delete(user);
        Assert.assertTrue(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"createClientWithUserDetails_correctCreate_nonZero"},
            expectedExceptions = DaoException.class)
    public void addUserRole_addExistingRole_daoException(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        userDao.addUserRole(user.getLogin(), Role.CLIENT);

        cleaner.delete(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"createClientWithUserDetails_correctCreate_nonZero"})
    public void addUserRole_nonExistingUser_daoException(User user) throws DaoException {
        Assert.assertFalse(userDao.addUserRole(user.getLogin(), Role.CLIENT));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"createClientWithUserDetails_correctCreate_nonZero"})
    public void deleteUserRole_correctDelete_true(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        boolean isSuccess = userDao.deleteUserRole(user.getLogin(), Role.CLIENT);

        cleaner.delete(user);
        Assert.assertTrue(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"createClientWithUserDetails_correctCreate_nonZero"})
    public void deleteUserRole_deleteNonExistentRole_daoException(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        boolean isSuccess = userDao.deleteUserRole(user.getLogin(), Role.DOCTOR);

        cleaner.delete(user);
        Assert.assertFalse(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"createClientWithUserDetails_correctCreate_nonZero"})
    public void deleteUserRole_nonExistingUser_daoException(User user) throws DaoException {
        Assert.assertFalse(userDao.deleteUserRole(user.getLogin(), Role.CLIENT));
    }
}
