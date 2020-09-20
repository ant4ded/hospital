package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import epam.hospital.data.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserDetailsDaoImplTest {
    private UserDetailsDao userDetailsDao;

    @BeforeClass
    private void setUserDetailsDao() {
        userDetailsDao = new UserDetailsDaoImpl();
    }

    // TODO: 19.09.2020 tests methods

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    private void createAndFindAndDelete_correctUserDetails_correctWork(User actual) throws DaoException {
        UserDetails expected;
        userDetailsDao.create(actual.getUserDetails());
        expected = userDetailsDao.find(actual.getUserDetails());
        userDetailsDao.delete(actual.getUserDetails());
        Assert.assertEquals(actual.getUserDetails(), expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    private void update_correctUserDetails_updatedRow(User user) throws DaoException {
        UserDetails expected = new UserDetails();
        expected.setPassportId(user.getUserDetails().getPassportId());
        expected.setUserId(user.getUserDetails().getUserId());
        expected.setGender(UserDetails.Gender.FEMALE);
        expected.setFirstName(user.getUserDetails().getFirstName());
        expected.setSurname(user.getUserDetails().getSurname());
        expected.setLastName(user.getUserDetails().getLastName());
        expected.setBirthday(user.getUserDetails().getBirthday());
        expected.setAddress(user.getUserDetails().getAddress());
        expected.setPhone(user.getUserDetails().getPhone());

        userDetailsDao.create(user.getUserDetails());
        userDetailsDao.update(user.getUserDetails(), expected);
        UserDetails actual = userDetailsDao.find(expected);
        userDetailsDao.delete(actual);

        Assert.assertEquals(actual, expected);
    }
}
