package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.ReceptionistServiceImpl;
import epam.hospital.util.Provider;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

public class ReceptionistServiceImplTest {
    @Mock
    private UserDao userDao;
    @Mock
    private UserDetailsDao userDetailsDao;
    private ReceptionistService receptionistService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        receptionistService = new ReceptionistServiceImpl(userDao, userDetailsDao);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void registerClient_nonExistentUser_true(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(userDao.createClientWithUserDetails(user))
                .thenReturn(1);
        Assert.assertTrue(receptionistService.registerClient(user));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void registerClient_existingUser_false(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        receptionistService.registerClient(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void registerClient_duplicateFields_false(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(userDao.createClientWithUserDetails(user))
                .thenReturn(0);
        Assert.assertFalse(receptionistService.registerClient(user));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void registerClient_daoException_serviceException(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.createClientWithUserDetails(user))
                .thenThrow(DaoException.class);
        receptionistService.registerClient(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findUserCredentials_existingUser_userPresent(User user) throws DaoException, ServiceException {
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDetailsDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()))
                .thenReturn(Optional.of(userDetails));
        Mockito.when(userDao.findById(user.getId()))
                .thenReturn(Optional.of(user));
        Assert.assertTrue(receptionistService.findUserCredentials(userDetails).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findUserCredentials_nonExistentUser_emptyUser(User user) throws DaoException, ServiceException {
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDetailsDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(receptionistService.findUserCredentials(userDetails).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findUserCredentials_daoException_serviceException(User user) throws DaoException, ServiceException {
        UserDetails userDetails = user.getUserDetails();
        Mockito.when(userDetailsDao.findByRegistrationData(userDetails.getFirstName(), userDetails.getSurname(),
                userDetails.getLastName(), userDetails.getBirthday()))
                .thenThrow(DaoException.class);
        Assert.assertTrue(receptionistService.findUserCredentials(userDetails).isEmpty());
    }
}
