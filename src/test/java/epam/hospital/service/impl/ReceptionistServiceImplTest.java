package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.ReceptionistServiceImpl;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "services", dependsOnGroups = "dao")
public class ReceptionistServiceImplTest {
    private ReceptionistService receptionistService;
    private UserDetailsDao userDetailsDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        receptionistService = new ReceptionistServiceImpl();
        userDetailsDao = new UserDetailsDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void registerClient_user_recordedBDUser(User user) throws ServiceException, DaoException {
        receptionistService.registerClient(user);

        User userFromDb = userDao.find(user.getLogin()).orElseThrow(DaoException::new);
        userFromDb.setUserDetails(userDetailsDao.findByUserId(user.getId()).orElseThrow(DaoException::new));

        cleaner.delete(user);
        Assert.assertEquals(user, userFromDb);
    }
}
