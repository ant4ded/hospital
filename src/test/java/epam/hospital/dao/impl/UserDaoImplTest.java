package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.service.ServiceAction;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"dao", "UserDaoImplTest"}, dependsOnGroups = "UserDetailsDaoImplTest")
public class UserDaoImplTest {
    private static final Logger logger = Logger.getLogger(UserDaoImplTest.class);

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
        UserDetails userDetails = user.getUserDetails();
        newValue.setUserDetails(user.getUserDetails());
        newValue.setPassword(user.getPassword());
        newValue.setRoles(user.getRoles());
        newValue.setLogin(user.getLogin());
        newValue.setId(user.getId());

        userDao.create(user);
        if (userDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()).isEmpty()) {
            cleaner.delete(user);
            logger.fatal("Create or findByRegistrationData work incorrect");
            Assert.fail("Create or findByRegistrationData work incorrect");
        }

        if (userDao.findById(userDao.find(user.getLogin()).orElse(new User()).getId()).isEmpty()) {
            logger.fatal("Create or find work incorrect");
            Assert.fail("Create or find work incorrect");
        }

        userDao.update(user, newValue);
        user = userDao.find(newValue.getLogin()).orElse(new User());
        Assert.assertEquals(user, newValue);

        cleaner.delete(user);
        if (userDao.findById(userDao.find(user.getLogin()).orElse(new User()).getId()).isPresent()) {
            logger.fatal("Delete work incorrect");
            Assert.fail("Delete or find work incorrect");
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateUserRoles_userAndRole_userWithNewRole(User user) throws DaoException {
        userDao.create(user);
        userDao.updateUserRoles(user.getLogin(), ServiceAction.ADD, Role.MEDICAL_ASSISTANT);
        User userFromDb = userDao.find(user.getLogin()).orElse(new User());

        if (!userFromDb.getRoles().contains(Role.MEDICAL_ASSISTANT) ||
                !userFromDb.getRoles().contains(Role.CLIENT)) {
            Assert.fail("Update users_roles work incorrect");
        }

        userDao.updateUserRoles(user.getLogin(), ServiceAction.REMOVE, Role.MEDICAL_ASSISTANT);
        userFromDb = userDao.find(user.getLogin()).orElse(new User());

        if (userFromDb.getRoles().contains(Role.MEDICAL_ASSISTANT)) {
            Assert.fail("Update users_roles work incorrect");
        }

        cleaner.delete(user);
        Assert.assertTrue(userDao.find(user.getLogin()).isEmpty());
    }
}
