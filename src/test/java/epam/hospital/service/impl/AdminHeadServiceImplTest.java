package epam.hospital.service.impl;

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
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;
import by.epam.hospital.service.util.Action;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

public class AdminHeadServiceImplTest {
    private AdminHeadService adminHeadService;
    private DepartmentStaffDao departmentStaffDao;
    private DepartmentDao departmentDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        adminHeadService = new AdminHeadServiceImpl();
        departmentStaffDao = new DepartmentStaffDaoImpl();
        departmentDao = new DepartmentDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findUserRoles_username_roles(User user) throws DaoException, ServiceException {
        userDao.create(user);
        ArrayList<Role> actual = adminHeadService.findUserRoles(user.getLogin());
        cleaner.delete(user);

        Assert.assertEquals(actual, user.getRoles());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void performUserRolesAction_username_updatedUserRoles(User user) throws DaoException, ServiceException {
        boolean result;
        ArrayList<Role> roles = new ArrayList<>();

        roles.add(Role.CLIENT);
        roles.add(Role.MEDICAL_ASSISTANT);
        userDao.create(user);
        adminHeadService.performUserRolesAction(user.getLogin(), Action.ADD, Role.MEDICAL_ASSISTANT);
        result = adminHeadService.findUserRoles(user.getLogin()).equals(roles);

        if (!result) {
            cleaner.delete(user);
            Assert.fail("performUserRolesAction work incorrect");
        }

        roles.remove(Role.MEDICAL_ASSISTANT);
        adminHeadService.performUserRolesAction(user.getLogin(), Action.REMOVE, Role.MEDICAL_ASSISTANT);
        result = adminHeadService.findUserRoles(user.getLogin()).equals(roles);

        cleaner.delete(user);
        Assert.assertTrue(result);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void appointDepartmentHead_departmentAndUsername_newDepartmentHead(User user)
            throws DaoException, ServiceException {
        boolean result;
        User previousHead = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new);
        userDao.create(user);

        adminHeadService.performUserRolesAction(user.getLogin(), Action.ADD, Role.DOCTOR);
        adminHeadService.performUserRolesAction(user.getLogin(), Action.ADD, Role.DEPARTMENT_HEAD);
        adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin());
        previousHead = userDao.find(previousHead.getLogin()).orElseThrow(DaoException::new);
        user = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new);

        result = previousHead.equals(user) || previousHead.getRoles().contains(Role.DEPARTMENT_HEAD);

        if (result) {
            cleaner.delete(user);
            Assert.fail("can not appointDepartmentHead");
        }

        userDao.updateUserRoles(previousHead.getLogin(), Action.ADD, Role.DEPARTMENT_HEAD);
        adminHeadService.appointDepartmentHead(Department.INFECTIOUS, previousHead.getLogin());
        result = previousHead.getLogin().equals(departmentDao.
                findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new).getLogin());

        Assert.assertTrue(result);
        cleaner.delete(user);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void performDepartmentStaffAction_findDepartmentByUsername(User user)
            throws ServiceException, DaoException {
        userDao.create(user);

        Department department = Department.INFECTIOUS;

        adminHeadService.performUserRolesAction(user.getLogin(), Action.ADD, Role.DOCTOR);
        adminHeadService.performDepartmentStaffAction(department, Action.ADD, user.getLogin());
        if (!adminHeadService.findDepartmentByUsername(user.getLogin()).equals(Department.INFECTIOUS)) {
            cleaner.delete(user);
            Assert.fail("performDepartmentStaffAction or findDepartmentByUsername work incorrect");
        }
        Map<String, User> departmentStaffMap = departmentStaffDao.findDepartmentStaff(department);
        if (!departmentStaffMap.containsKey(user.getLogin())) {
            cleaner.delete(user);
            Assert.fail("performDepartmentStaffAction work incorrect");
        }

        adminHeadService.performDepartmentStaffAction(department, Action.REMOVE, user.getLogin());
        departmentStaffMap = departmentStaffDao.findDepartmentStaff(department);

        cleaner.delete(user);
        Assert.assertFalse(departmentStaffMap.containsKey(user.getLogin()));
    }

    @Test
    public void findDepartmentsHeads() throws ServiceException {
        Assert.assertEquals(adminHeadService.findDepartmentsHeads().values().size(), 9);
    }
}
