package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentStaffDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
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
    public void makeMedicalWorkerAndAddToDepartment_correctWork_true(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);

        boolean isSuccess = departmentStaffDao.
                makeMedicalWorkerAndAddToDepartment(Department.INFECTIOUS, user.getLogin(), Role.DOCTOR);

        cleaner.deleteUserFromDepartment(user);
        cleaner.delete(user);
        Assert.assertTrue(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", expectedExceptions = DaoException.class)
    public void makeMedicalWorkerAndAddToDepartment_alreadyMedicalWorker_daoException(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        userDao.addUserRole(user.getLogin(), Role.DOCTOR);
        try {
            departmentStaffDao.makeMedicalWorkerAndAddToDepartment(Department.INFECTIOUS, user.getLogin(), Role.DOCTOR);
        } finally {
            cleaner.delete(user);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", expectedExceptions = DaoException.class)
    public void makeMedicalWorkerAndAddToDepartment_departmentHead_daoException(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        userDao.addUserRole(user.getLogin(), Role.DEPARTMENT_HEAD);
        try {
            departmentStaffDao.makeMedicalWorkerAndAddToDepartment(Department.INFECTIOUS, user.getLogin(), Role.DOCTOR);
        } finally {
            cleaner.delete(user);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", expectedExceptions = DaoException.class)
    public void makeMedicalWorkerAndAddToDepartment_nonExistentUser_daoException(User user) throws DaoException {
        try {
            departmentStaffDao.makeMedicalWorkerAndAddToDepartment(Department.INFECTIOUS, user.getLogin(), Role.DOCTOR);
        } finally {
            cleaner.deleteUserFromDepartment(user);
            cleaner.delete(user);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "makeMedicalWorkerAndAddToDepartment_correctWork_true")
    public void updateDepartmentByLogin_correctUpdate_true(User user) throws DaoException {
        userDao.createClientWithUserDetails(user);
        departmentStaffDao.makeMedicalWorkerAndAddToDepartment(Department.INFECTIOUS, user.getLogin(), Role.DOCTOR);

        boolean isSuccess = departmentStaffDao.updateDepartmentByLogin(Department.INFECTIOUS, user.getLogin());

        cleaner.deleteUserFromDepartment(user);
        cleaner.delete(user);
        Assert.assertTrue(isSuccess);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            dependsOnMethods = "makeMedicalWorkerAndAddToDepartment_correctWork_true")
    public void findDepartmentStaff_correctFind_afterCreateResultWithCreatedUser(User user) throws DaoException {
        Map<String, User> userMap = departmentStaffDao.findDepartmentStaff(Department.INFECTIOUS);
        userDao.createClientWithUserDetails(user);
        departmentStaffDao.makeMedicalWorkerAndAddToDepartment(Department.INFECTIOUS, user.getLogin(), Role.DOCTOR);
        Map<String, User> userMapAfterCreate = departmentStaffDao.findDepartmentStaff(Department.INFECTIOUS);

        if (userMap.size() == userMapAfterCreate.size() || !userMapAfterCreate.containsKey(user.getLogin())) {
            Assert.fail("FindDepartmentStaff fail.");
        }

        cleaner.deleteUserFromDepartment(user);
        cleaner.delete(user);
    }
}
