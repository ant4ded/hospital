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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"dao", "UserDetailsDaoImplTest"}, dependsOnGroups = "UserDaoImplTest")
public class UserDetailsDaoImplTest {
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
        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setPassportId(user.getUserDetails().getPassportId());
        newUserDetails.setGender(UserDetails.Gender.MALE);
        newUserDetails.setFirstName(user.getUserDetails().getFirstName());
        newUserDetails.setSurname(user.getUserDetails().getSurname());
        newUserDetails.setLastName(user.getUserDetails().getLastName());
        newUserDetails.setBirthday(user.getUserDetails().getBirthday());
        newUserDetails.setAddress(user.getUserDetails().getAddress());
        newUserDetails.setPhone(user.getUserDetails().getPhone());

        int userId = userDao.create(user);
        newUserDetails.setUserId(userId);
        user.getUserDetails().setUserId(userId);
        if (!userDetailsDao.create(user.getUserDetails())) {
            Assert.fail("Create user details failed.");
        }

        UserDetails userDetailsFindByRegistrationData = userDetailsDao.findByRegistrationData(
                user.getUserDetails().getFirstName(), user.getUserDetails().getSurname(),
                user.getUserDetails().getLastName(), user.getUserDetails().getBirthday())
                .orElseGet(UserDetails::new);

        if (!user.getUserDetails().equals(userDetailsFindByRegistrationData)) {
            Assert.fail("FindByRegistrationData failed.");
        }

        userDetailsDao.update(userDetailsFindByRegistrationData, newUserDetails);
        UserDetails userDetailsFindByUserId = userDetailsDao.findByUserId(user.getUserDetails().getUserId())
                .orElseGet(UserDetails::new);
        if (!newUserDetails.equals(userDetailsFindByUserId)){
            Assert.fail("FindByUserId failed.");
        }

        cleaner.delete(user);
    }
}
