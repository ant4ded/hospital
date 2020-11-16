package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentDaoImpl;
import by.epam.hospital.dao.impl.DepartmentStaffDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ServiceAction;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

@Test(groups = "dao", dependsOnGroups = {"UserDaoImplTest", "DepartmentStaffDaoImplTest"})
public class DepartmentDaoImplTest {
    private DepartmentStaffDao departmentStaffDao;
    private DepartmentDao departmentDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        departmentStaffDao = new DepartmentStaffDaoImpl();
        departmentDao = new DepartmentDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test
    public void findHeadDepartment_correctFind_userPresent() throws DaoException {
        Assert.assertTrue(departmentDao.findHeadDepartment(Department.INFECTIOUS).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "findHeadDepartment_correctFind_userPresent")
    public void updateDepartmentHead_correctUpdate_true(User user) throws DaoException {
        User firstHead = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new);

        userDao.createClientWithUserDetails(user);
        userDao.addUserRole(user.getLogin(), Role.DOCTOR);
        if (!departmentDao.updateDepartmentHead(Department.INFECTIOUS, user.getLogin())) {
            Assert.fail("UpdateDepartmentHead failed.");
        }

        departmentDao.updateDepartmentHead(Department.INFECTIOUS, firstHead.getLogin());
        cleaner.delete(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", expectedExceptions = DaoException.class)
    public void updateDepartmentHead_notDoctor_daoException(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        try {
            departmentDao.updateDepartmentHead(Department.INFECTIOUS, user.getLogin());
        } finally {
            cleaner.delete(user);
        }
    }

    @Test(expectedExceptions = DaoException.class)
    public void updateDepartmentHead_nonExistentLogin_false() throws DaoException {
        Assert.assertFalse(departmentDao.updateDepartmentHead(Department.INFECTIOUS, "1"));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findDepartment_correctFind_departmentPresent(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin());

        Optional<Department> optionalDepartment = departmentDao.findDepartment(user.getLogin());
        if (optionalDepartment.isEmpty()) {
            departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.DELETE, user.getLogin());
            cleaner.delete(user);
            Assert.fail("FindDepartment failed.");
        }

        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.DELETE, user.getLogin());
        cleaner.delete(user);
    }

    @Test
    public void findDepartment_nonExistentLogin_departmentEmpty() throws DaoException {
        Assert.assertTrue(departmentDao.findDepartment("1").isEmpty());
    }

    @Test
    public void findDepartmentsHeads_correctFind_findAll() throws DaoException {
        Assert.assertEquals(departmentDao.findDepartmentsHeads().values().size(), 9);
    }
}
