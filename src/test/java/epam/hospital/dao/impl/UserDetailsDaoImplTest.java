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
    public void create_find_update_findByRegistrationData(User user) throws DaoException {
        UserDetails userDetails = user.getUserDetails();
        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setPassportId(user.getUserDetails().getPassportId());
        newUserDetails.setGender(UserDetails.Gender.MALE);
        newUserDetails.setFirstName(user.getUserDetails().getFirstName());
        newUserDetails.setSurname(user.getUserDetails().getSurname());
        newUserDetails.setLastName(user.getUserDetails().getLastName());
        newUserDetails.setBirthday(user.getUserDetails().getBirthday());
        newUserDetails.setAddress(user.getUserDetails().getAddress());
        newUserDetails.setPhone(user.getUserDetails().getPhone());

        userDao.create(user);

        if (userDetailsDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()).isEmpty()) {
            cleaner.delete(user);
            logger.fatal("Create or findByRegistrationData work incorrect");
            Assert.fail("Create or findByRegistrationData work incorrect");
        }

        if (userDao.find(user.getLogin()).orElse(new User()).getUserDetails().getUserId() == 0) {
            cleaner.delete(user);
            logger.fatal("Create or find work incorrect");
            Assert.fail("Create or find work incorrect");
        }

        userDetailsDao.update(user.getUserDetails(), newUserDetails);
        user.setUserDetails(userDetailsDao.find(user.getUserDetails().getUserId()).orElse(new UserDetails()));

        cleaner.delete(user);
        if (userDetailsDao.find(user.getUserDetails().getUserId()).isPresent() || userDao.find(user.getLogin()).isPresent()) {
            logger.fatal("Delete work incorrect");
            Assert.fail("Delete or find work incorrect");
        }

        Assert.assertEquals(user.getUserDetails(), newUserDetails);
    }
}
