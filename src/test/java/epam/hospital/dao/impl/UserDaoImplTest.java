package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ServiceAction;
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
    public void create_correctCreate_notZero(User user) throws DaoException {
        int userId = userDao.create(user);
        cleaner.delete(user);
        Assert.assertTrue(userId != 0);
    }

    @Test(expectedExceptions = DaoException.class)
    public void create_nullField_exception() throws DaoException {
        User user = new User();
        userDao.create(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "create_correctCreate_notZero")
    public void findByLogin_correctFind_userPresent(User user) throws DaoException {
        userDao.create(user);
        Optional<User> optionalUser = userDao.findByLogin(user.getLogin());

        cleaner.delete(user);

        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test
    public void findByLogin_nonExistentLogin_userEmpty() throws DaoException {
        Assert.assertTrue(userDao.findByLogin("").isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "create_correctCreate_notZero")
    public void findById_correctFind_userPresent(User user) throws DaoException {
        int userId = userDao.create(user);
        Optional<User> optionalUser = userDao.findById(userId);

        cleaner.delete(user);

        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test
    public void findById_nonExistentId_userEmpty() throws DaoException {
        Assert.assertTrue(userDao.findById(0).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"create_correctCreate_notZero", "findByLogin_correctFind_userPresent"})
    public void update_correctUpdate_updatedUser(User user) throws DaoException {
        User newUser = new User();
        newUser.setUserDetails(user.getUserDetails());
        newUser.setPassword(user.getPassword());
        newUser.setRoles(user.getRoles());
        newUser.setLogin(user.getLogin() + "1");
        user.setId(userDao.create(user));
        newUser.setId(user.getId());

        User updatedUser = userDao.update(user, newUser);
        cleaner.delete(newUser);

        Assert.assertEquals(updatedUser, newUser);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = DaoException.class)
    public void update_nonExistentLogin_exception(User user) throws DaoException {
        userDao.update(user, new User());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = {"create_correctCreate_notZero", "findByLogin_correctFind_userPresent"})
    public void updateUserRoles_correctUpdate_true(User user) throws DaoException {
        userDao.create(user);
        user.getRoles().add(Role.MEDICAL_ASSISTANT);
        boolean result = userDao.updateUserRoles(user.getLogin(), ServiceAction.ADD, Role.MEDICAL_ASSISTANT);
        User userFromDb = userDao.findByLogin(user.getLogin()).orElseGet(User::new);

        if (!userFromDb.getRoles().equals(user.getRoles()) && !result) {
            cleaner.delete(user);
            Assert.fail("Update users_roles failed.");
        }

        result = userDao.updateUserRoles(user.getLogin(), ServiceAction.REMOVE, Role.MEDICAL_ASSISTANT);
        userFromDb = userDao.findByLogin(user.getLogin()).orElseGet(User::new);

        if (userFromDb.getRoles().contains(Role.MEDICAL_ASSISTANT) && result) {
            cleaner.delete(user);
            Assert.fail("Update users_roles failed.");
        }

        cleaner.delete(user);
    }

    @Test
    public void updateUserRoles_nonExitingId_false() throws DaoException {
        Assert.assertFalse(userDao.updateUserRoles("", ServiceAction.REMOVE, Role.MEDICAL_ASSISTANT));
    }
}
