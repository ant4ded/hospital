package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.AuthenticationService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AuthenticationServiceImpl;
import epam.hospital.util.Provider;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

public class AuthenticationServiceImplTest {
    @Mock
    private UserDao userDao;
    private AuthenticationService authenticationService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(userDao);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void isHasRole_userHaveRole_true(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Assert.assertTrue(authenticationService.isHasRole(user.getLogin(), Role.CLIENT));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void isHasRole_nonExistentUser_false(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Assert.assertFalse(authenticationService.isHasRole(user.getLogin(), Role.CLIENT));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void isHasRole_userDoesNotHaveRole_False(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Assert.assertFalse(authenticationService.isHasRole(user.getLogin(), Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void isHasRole_daoException_serviceException(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertFalse(authenticationService.isHasRole(user.getLogin(), Role.DOCTOR));
    }
}
