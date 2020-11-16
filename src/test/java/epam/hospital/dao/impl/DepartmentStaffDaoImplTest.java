package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentStaffDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ServiceAction;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

@Test(groups = {"dao", "DepartmentStaffDaoImplTest"}, dependsOnGroups = "UserDaoImplTest")
public class DepartmentStaffDaoImplTest {
    private DepartmentStaffDao departmentStaffDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeMethod
    private void setUp() {
        departmentStaffDao = new DepartmentStaffDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateStaffDepartment_correctUpdate_true(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);

        if (!departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin())) {
            Assert.fail("UpdateStaffDepartment work incorrect.");
        }
        if (!departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.DELETE, user.getLogin())) {
            Assert.fail("UpdateStaffDepartment work incorrect.");
        }

        cleaner.delete(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "updateStaffDepartment_correctUpdate_true")
    public void findDepartmentStaff_correctFind_afterCreateResultWithCreatedUser(User user) throws DaoException {
        Map<String, User> userMap = departmentStaffDao.findDepartmentStaff(Department.INFECTIOUS);
        userDao.createClientWithUserDetails(user);
        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin());
        Map<String, User> userMapAfterCreate = departmentStaffDao.findDepartmentStaff(Department.INFECTIOUS);

        if (userMap.size() == userMapAfterCreate.size() || !userMapAfterCreate.containsKey(user.getLogin())) {
            Assert.fail("FindDepartmentStaff fail.");
        }

        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.DELETE, user.getLogin());
        cleaner.delete(user);
    }
}
