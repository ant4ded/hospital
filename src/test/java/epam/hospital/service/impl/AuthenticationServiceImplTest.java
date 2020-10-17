package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.AuthenticationService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AuthenticationServiceImpl;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        authenticationService = new AuthenticationServiceImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void isHasRole_user_true(User user) throws DaoException, ServiceException {
        userDao.create(user);
        boolean result = authenticationService.isHasRole(user.getLogin(), Role.CLIENT);
        cleaner.delete(user);

        Assert.assertTrue(result);
    }
}
