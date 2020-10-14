package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserDaoImplTest {
    private static final Logger logger = Logger.getLogger(UserDaoImplTest.class);

    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setUserDao() {
        cleaner = new Cleaner();
        userDao = new UserDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void create_find_update(User user) throws DaoException {
        user.setLogin("pppp");

        User newValue = new User();
        newValue.setUserDetails(user.getUserDetails());
        newValue.setPassword(user.getPassword());
        newValue.setRoles(user.getRoles());
        newValue.setLogin(user.getLogin());
        newValue.setId(user.getId());

        userDao.create(user);
        if (userDao.find(user).isEmpty()) {
            logger.fatal("Create or find work incorrect");
            Assert.fail("Create or find work incorrect");
        }

        userDao.update(user, newValue);
        user = userDao.find(newValue).orElse(new User());
        Assert.assertEquals(user, newValue);

        cleaner.delete(user);
        if (userDao.find(user).isPresent()) {
            logger.fatal("Delete work incorrect");
            Assert.fail("Delete or find work incorrect");
        }
    }
}
