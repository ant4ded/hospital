package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.ClientServiceImpl;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

@Test(groups = "services", dependsOnGroups = "dao")
public class ClientServiceImplTest {
    private ClientService clientService;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        clientService = new ClientServiceImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void authorization_userWithRoles(User user) throws DaoException, ServiceException {
        userDao.create(user);
        ArrayList<Role> roles;

        roles = clientService.authorization(user.getLogin(), user.getPassword()).getRoles();
        cleaner.delete(user);

        Assert.assertEquals(user.getRoles(), roles);
    }
}
