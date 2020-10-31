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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "UserDaoImplTest")
public class UserDaoImplTest {
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        cleaner = new Cleaner();
        userDao = new UserDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void create_find_update_findByRegistrationData(User user) throws DaoException {
        User newValue = new User();
        newValue.setUserDetails(user.getUserDetails());
        newValue.setPassword(user.getPassword());
        newValue.setRoles(user.getRoles());
        newValue.setLogin(user.getLogin() + "1");
        newValue.setId(user.getId());

        int userId = userDao.create(user);
        User findById = userDao.findById(userId).orElseThrow(DaoException::new);
        User userFind = userDao.findByLogin(user.getLogin()).orElseThrow(DaoException::new);
        if (!userFind.equals(findById)) {
            Assert.fail("create or find work incorrect.");
        }

        User updatedUser = userDao.update(user, newValue);
        newValue = userDao.findByLogin(newValue.getLogin()).orElseGet(User::new);
        Assert.assertEquals(updatedUser, newValue);

        cleaner.delete(updatedUser);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateUserRoles(User user) throws DaoException {
        userDao.create(user);
        user.getRoles().add(Role.MEDICAL_ASSISTANT);
        boolean result = userDao.updateUserRoles(user.getLogin(), ServiceAction.ADD, Role.MEDICAL_ASSISTANT);
        User userFromDb = userDao.findByLogin(user.getLogin()).orElseGet(User::new);

        if (!userFromDb.getRoles().equals(user.getRoles()) && !result) {
            Assert.fail("update users_roles work incorrect.");
        }

        result = userDao.updateUserRoles(user.getLogin(), ServiceAction.REMOVE, Role.MEDICAL_ASSISTANT);
        userFromDb = userDao.findByLogin(user.getLogin()).orElseGet(User::new);

        if (userFromDb.getRoles().contains(Role.MEDICAL_ASSISTANT) && result) {
            Assert.fail("update users_roles work incorrect.");
        }

        cleaner.delete(user);
    }
}
