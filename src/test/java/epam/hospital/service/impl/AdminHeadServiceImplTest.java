package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentDaoImpl;
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

import java.util.HashMap;
import java.util.Map;

public class AdminHeadServiceImplTest {
    private AdminHeadService adminHeadService;
    private DepartmentDao departmentDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        adminHeadService = new AdminHeadServiceImpl();
        departmentDao = new DepartmentDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void findUserRoles_user_roles(User user) throws DaoException, ServiceException {
        userDao.create(user);
        Map<String, Role> actual = adminHeadService.findUserRoles(user.getLogin());
        cleaner.delete(user);

        Assert.assertEquals(actual, user.getRoles());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void performUserRolesAction(User user) throws DaoException, ServiceException {
        boolean result = false;
        Map<String, Role> roles = new HashMap<>();

        roles.put(Role.CLIENT.name(), Role.CLIENT);
        roles.put(Role.MEDICAL_ASSISTANT.name(), Role.MEDICAL_ASSISTANT);
        userDao.create(user);
        adminHeadService.performUserRolesAction(user.getLogin(), Action.ADD, Role.MEDICAL_ASSISTANT);
        result = adminHeadService.findUserRoles(user.getLogin()).equals(roles);

        if (!result) {
            cleaner.delete(user);
            Assert.fail("performUserRolesAction work incorrect");
        }

        roles.remove(Role.MEDICAL_ASSISTANT.name());
        adminHeadService.performUserRolesAction(user.getLogin(), Action.REMOVE, Role.MEDICAL_ASSISTANT);
        result = adminHeadService.findUserRoles(user.getLogin()).equals(roles);

        cleaner.delete(user);
        Assert.assertTrue(result);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void appointDepartmentHead_user_newDepartmentHead(User user) throws DaoException, ServiceException {
        boolean result;
        User previousHead = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new);
        userDao.create(user);

        adminHeadService.performUserRolesAction(user.getLogin(), Action.ADD, Role.DOCTOR);
        adminHeadService.performUserRolesAction(user.getLogin(), Action.ADD, Role.DEPARTMENT_HEAD);
        adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin());
        previousHead = userDao.find(previousHead.getLogin()).orElseThrow(DaoException::new);
        user = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new);

        result = previousHead.equals(user) || previousHead.getRoles().containsValue(Role.DEPARTMENT_HEAD);

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
}
