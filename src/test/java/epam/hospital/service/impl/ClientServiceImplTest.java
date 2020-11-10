package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.ClientServiceImpl;
import epam.hospital.util.Provider;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

public class ClientServiceImplTest {
    @Mock
    private UserDao userDao;
    @Mock
    private UserDetailsDao userDetailsDao;
    private ClientService clientService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientServiceImpl(userDao, userDetailsDao);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void authorization_correctAuthorization_userPresent(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(java.util.Optional.of(user));
        Assert.assertTrue(clientService.authorization(user.getLogin(), user.getPassword()).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void authorization_nonExistentLogin_userEmpty(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(java.util.Optional.empty());
        Assert.assertTrue(clientService.authorization(user.getLogin(), user.getPassword()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void authorization_wrongPassword_userEmpty(User user) throws DaoException, ServiceException {
        User withAnotherPassword = new User(user.getLogin(), "");
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(java.util.Optional.of(withAnotherPassword));
        Assert.assertTrue(clientService.authorization(user.getLogin(), user.getPassword()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void authorization_daoException_serviceException(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertTrue(clientService.authorization(user.getLogin(), user.getPassword()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateUserDetails_existingUser_userPresent(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(userDetailsDao.update(user.getUserDetails(), user.getId()))
                .thenReturn(Optional.of(user.getUserDetails()));
        Assert.assertTrue(clientService.updateUserDetails(user.getUserDetails(), user.getLogin()).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateUserDetails_nonExistentUser_userEmpty(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(userDetailsDao.update(user.getUserDetails(), user.getId()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(clientService.updateUserDetails(user.getUserDetails(), user.getLogin()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void updateUserDetails_daoException_serviceException(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(userDetailsDao.update(user.getUserDetails(), user.getId()))
                .thenThrow(DaoException.class);
        clientService.updateUserDetails(user.getUserDetails(), user.getLogin());
    }
}
