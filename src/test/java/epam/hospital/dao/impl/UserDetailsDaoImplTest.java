package epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDetailsDaoImplTest {
    private final Logger logger = Logger.getLogger(UserDetailsDaoImplTest.class);

    private UserDetailsDao userDetailsDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setUserDetailsDao() {
        userDetailsDao = new UserDetailsDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void create_find_update(User user) throws DaoException {
        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setPassportId(user.getUserDetails().getPassportId());
        newUserDetails.setUserId(user.getUserDetails().getUserId());
        newUserDetails.setGender(UserDetails.Gender.FEMALE);
        newUserDetails.setFirstName(user.getUserDetails().getFirstName());
        newUserDetails.setSurname(user.getUserDetails().getSurname());
        newUserDetails.setLastName(user.getUserDetails().getLastName());
        newUserDetails.setBirthday(user.getUserDetails().getBirthday());
        newUserDetails.setAddress(user.getUserDetails().getAddress());
        newUserDetails.setPhone(user.getUserDetails().getPhone());

        userDetailsDao.create(user.getUserDetails());
        if (userDetailsDao.find(user.getUserDetails()).isEmpty()) {
            logger.fatal("Create or find work incorrect");
            Assert.fail("Create or find work incorrect");
        }

        userDetailsDao.update(user.getUserDetails(), newUserDetails);
        user.setUserDetails(userDetailsDao.find(newUserDetails).orElse(new UserDetails()));
        Assert.assertEquals(user.getUserDetails(), newUserDetails);

        cleaner.delete(user.getUserDetails());
        if (userDetailsDao.find(user.getUserDetails()).isPresent()){
            logger.fatal("Delete work incorrect");
            Assert.fail("Delete or find work incorrect");
        }
    }
}
