package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.User;
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
    public void registerClient_user_true(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user));
        Assert.assertTrue(receptionistService.registerClient(user));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void registerClient_user_serviceException(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        receptionistService.registerClient(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void registerClient_daoException_serviceException(User user) throws ServiceException, DaoException {
        Mockito.doThrow(DaoException.class).when(userDao).create(user);
        receptionistService.registerClient(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void registerClient_userPresent_serviceException(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        receptionistService.registerClient(user);
    }
}
