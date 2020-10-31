package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentDaoImpl;
import by.epam.hospital.dao.impl.DepartmentStaffDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ServiceAction;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

@Test(groups = "dao", dependsOnGroups = {"UserDaoImplTest", "DepartmentStaffDaoImplTest"})
public class DepartmentDaoImplTest {
    private DepartmentStaffDao departmentStaffDao;
    private DepartmentDao departmentDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void init() {
        departmentStaffDao = new DepartmentStaffDaoImpl();
        departmentDao = new DepartmentDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test
    public void findHeadDepartment_user_user() throws DaoException {
        Assert.assertTrue(departmentDao.findHeadDepartment(Department.INFECTIOUS).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateDepartmentHead_departmentAndUsername_newHead(User user) throws DaoException {
        User firstHead = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new);

        userDao.create(user);
        if (!departmentDao.updateDepartmentHead(Department.INFECTIOUS, user.getLogin())) {
            Assert.fail("UpdateDepartmentHead failed.");
        }

        departmentDao.updateDepartmentHead(Department.INFECTIOUS, firstHead.getLogin());
        cleaner.delete(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findDepartment_username_user(User user) throws DaoException {
        userDao.create(user);
        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin());

        Optional<Department> optionalDepartment = departmentDao.findDepartment(user.getLogin());

        if (optionalDepartment.isEmpty() || !optionalDepartment.get().equals(Department.INFECTIOUS)) {
            departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.REMOVE, user.getLogin());
            cleaner.delete(user);
            Assert.fail("FindDepartment failed.");
        }

        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.REMOVE, user.getLogin());
        cleaner.delete(user);
    }

    @Test
    public void findDepartmentsHeads() throws DaoException {
        Assert.assertEquals(departmentDao.findDepartmentsHeads().values().size(), 9);
    }
}
