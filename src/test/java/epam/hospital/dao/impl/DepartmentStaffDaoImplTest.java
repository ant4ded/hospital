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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DepartmentStaffDaoImplTest {
    private DepartmentStaffDao departmentStaffDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        departmentStaffDao = new DepartmentStaffDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateStaffDepartment_findDepartmentStaff(User user) throws DaoException {
        userDao.create(user);

        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin());
        if (!departmentStaffDao.findDepartmentStaff(Department.INFECTIOUS).containsKey(user.getLogin())) {
            Assert.fail("findDepartmentStaff or updateStaffDepartment work incorrect");
        }

        departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS, ServiceAction.REMOVE, user.getLogin());
        if (departmentStaffDao.findDepartmentStaff(Department.INFECTIOUS).containsKey(user.getLogin())) {
            Assert.fail("updateStaffDepartment work incorrect");
        }

        cleaner.delete(user);
    }
}
