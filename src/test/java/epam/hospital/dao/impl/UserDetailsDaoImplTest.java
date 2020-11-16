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
    public void update_correctUpdate_userDetailsPresent(User user) throws DaoException {
        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setAddress(user.getUserDetails().getAddress() + "1");
        newUserDetails.setPhone(user.getUserDetails().getPhone());

        user.getUserDetails().setUserId(userDao.createClientWithUserDetails(user));
        newUserDetails.setUserId(user.getUserDetails().getUserId());
        Optional<UserDetails> optionalUserDetails = userDetailsDao.update(newUserDetails, newUserDetails.getUserId());
        cleaner.delete(user);

        Assert.assertTrue(optionalUserDetails.isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void update_nonExistentUserId_userDetailsEmpty(User user) throws DaoException {
        Assert.assertTrue(userDetailsDao
                .update(user.getUserDetails(), user.getUserDetails().getUserId()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByUserId_correctFind_userDetailsPresent(User user) throws DaoException {
        user.getUserDetails().setUserId(userDao.createClientWithUserDetails(user));

        Optional<UserDetails> userDetails = userDetailsDao.findByUserId(user.getUserDetails().getUserId());
        cleaner.delete(user);

        Assert.assertTrue(userDetails.isPresent());
    }

    @Test
    public void findByUserId_nonExistentId_userDetailsEmpty() throws DaoException {
        Assert.assertTrue(userDetailsDao.findByUserId(0).isEmpty());
    }
}
