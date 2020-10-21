package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.DoctorServiceImpl;
import epam.hospital.dao.impl.UserDaoImplTest;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "services", dependsOnGroups = "dao")
public class DoctorServiceImplTest {
    private static final Logger logger = Logger.getLogger(UserDaoImplTest.class);

    private DoctorService doctorService;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        doctorService = new DoctorServiceImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findByRegistrationData(User user) throws DaoException, ServiceException {
        userDao.create(user);
        UserDetails userDetails = user.getUserDetails();

        if (doctorService.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()).isEmpty()) {
            cleaner.delete(user);
            logger.fatal("Create or findByRegistrationData work incorrect");
            Assert.fail("Create or findByRegistrationData work incorrect");
        }

        cleaner.delete(user);
    }
}
