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
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserDetailsDaoImplTest {
    private final Logger logger = Logger.getLogger(UserDetailsDaoImplTest.class);

    private UserDetailsDao userDetailsDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        userDetailsDao = new UserDetailsDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void create_find_update(User user) throws DaoException {
        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setPassportId(user.getUserDetails().getPassportId());
        newUserDetails.setGender(UserDetails.Gender.FEMALE);
        newUserDetails.setFirstName(user.getUserDetails().getFirstName());
        newUserDetails.setSurname(user.getUserDetails().getSurname());
        newUserDetails.setLastName(user.getUserDetails().getLastName());
        newUserDetails.setBirthday(user.getUserDetails().getBirthday());
        newUserDetails.setAddress(user.getUserDetails().getAddress());
        newUserDetails.setPhone(user.getUserDetails().getPhone());

        userDao.create(user);

        user.setId(userDao.find(user.getLogin()).orElse(new User()).getId());
        user.getUserDetails().setUserId(user.getId());
        newUserDetails.setUserId(user.getId());

        userDetailsDao.create(user.getUserDetails());
        if (userDetailsDao.find(user.getUserDetails().getUserId()).isEmpty()) {
            logger.fatal("Create or find work incorrect");
            Assert.fail("Create or find work incorrect");
        }

        userDetailsDao.update(user.getUserDetails(), newUserDetails);
        user.setUserDetails(userDetailsDao.find(newUserDetails.getUserId()).orElse(new UserDetails()));
        Assert.assertEquals(user.getUserDetails(), newUserDetails);

        cleaner.delete(user.getUserDetails());
        cleaner.delete(user);
        if (userDetailsDao.find(user.getUserDetails().getUserId()).isPresent() || userDao.find(user.getLogin()).isPresent()) {
            logger.fatal("Delete work incorrect");
            Assert.fail("Delete or find work incorrect");
        }
    }
}
