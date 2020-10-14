package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.RoleDao;
import by.epam.hospital.dao.impl.RoleDaoImpl;
import by.epam.hospital.entity.Role;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class RoleDaoImplTest {
    private RoleDao roleDao;

    @BeforeClass
    private void setRoleDao() {
        roleDao = new RoleDaoImpl();
    }

    @Test
    public void findRoleId() throws DaoException {
        int expected = 1;
        int actual = roleDao.findRoleId(Role.ADMIN_HEAD);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void findAll() throws DaoException {
        List<Role> expected = new ArrayList<>();
        expected.add(Role.ADMIN_HEAD);
        expected.add(Role.ADMIN);
        expected.add(Role.RECEPTIONIST);
        expected.add(Role.DOCTOR);
        expected.add(Role.DEPARTMENT_HEAD);
        expected.add(Role.CLIENT);
        expected.add(Role.MEDICAL_ASSISTANT);

        List<Role> actual = roleDao.findAll();

        Assert.assertEquals(actual, expected);
    }
}
