package epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceAction;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;
import epam.hospital.util.Provider;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminHeadServiceImplTest {
    @Mock
    private UserDao userDao;
    @Mock
    private DepartmentDao departmentDao;
    @Mock
    private DepartmentStaffDao departmentStaffDao;
    private AdminHeadService adminHeadService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        adminHeadService = new AdminHeadServiceImpl(userDao, departmentDao, departmentStaffDao);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "findUserRoles")
    public void findUserRoles_correctFind_userRoles(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Assert.assertEquals(adminHeadService.findUserRoles(user.getLogin()), user.getRoles());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "findUserRoles")
    public void findUserRoles_nonExistentUser_rolesEmpty(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(adminHeadService.findUserRoles(user.getLogin()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findUserRoles_daoException_serviceException(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        Assert.assertTrue(adminHeadService.findUserRoles(user.getLogin()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performUserRolesAction")
    public void performUserRolesAction_roleClient_false(User user) throws ServiceException {
        Assert.assertFalse(adminHeadService
                .updateUserRoles(user.getLogin(), ServiceAction.ADD, Role.CLIENT));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performUserRolesAction")
    public void performUserRolesAction_correctAdd_true(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(userDao.addUserRole(user.getLogin(), Role.DOCTOR))
                .thenReturn(true);
        Assert.assertTrue(adminHeadService
                .updateUserRoles(user.getLogin(), ServiceAction.ADD, Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performUserRolesAction")
    public void performUserRolesAction_correctDelete_true(User user) throws DaoException, ServiceException {
        user.getRoles().add(Role.DOCTOR);
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(userDao.deleteUserRole(user.getLogin(), Role.DOCTOR))
                .thenReturn(true);
        Assert.assertTrue(adminHeadService
                .updateUserRoles(user.getLogin(), ServiceAction.REMOVE, Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performUserRolesAction")
    public void performUserRolesAction_nonExistentUser_false(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Assert.assertFalse(adminHeadService
                .updateUserRoles(user.getLogin(), ServiceAction.ADD, Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performUserRolesAction")
    public void performUserRolesAction_addExistingRole_false(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Assert.assertFalse(adminHeadService
                .updateUserRoles(user.getLogin(), ServiceAction.ADD, Role.CLIENT));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performUserRolesAction")
    public void performUserRolesAction_deleteNonExistentRole_false(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Assert.assertFalse(adminHeadService
                .updateUserRoles(user.getLogin(), ServiceAction.REMOVE, Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void performUserRolesAction_daoException_serviceException(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        adminHeadService.updateUserRoles(user.getLogin(), ServiceAction.REMOVE, Role.DOCTOR);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "findDepartmentByUsername")
    public void findDepartmentByUsername_correctFindWithDoctor_departmentPresent(User user)
            throws DaoException, ServiceException {
        user.getRoles().add(Role.DOCTOR);
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin()))
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Assert.assertTrue(adminHeadService.findDepartmentByUsername(user.getLogin()).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "findDepartmentByUsername")
    public void findDepartmentByUsername_correctFindWithMedicalAssistant_departmentPresent(User user)
            throws DaoException, ServiceException {
        user.getRoles().add(Role.MEDICAL_ASSISTANT);
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin()))
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Assert.assertTrue(adminHeadService.findDepartmentByUsername(user.getLogin()).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "findDepartmentByUsername")
    public void findDepartmentByUsername_nonExistentUser_departmentEmpty(User user)
            throws DaoException, ServiceException {
        user.getRoles().add(Role.MEDICAL_ASSISTANT);
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(departmentDao.findDepartment(user.getLogin()))
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Assert.assertTrue(adminHeadService.findDepartmentByUsername(user.getLogin()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "findDepartmentByUsername")
    public void findDepartmentByUsername_userDoesNotHaveNecessaryRoles_departmentEmpty(User user)
            throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(departmentDao.findDepartment(user.getLogin()))
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Assert.assertTrue(adminHeadService.findDepartmentByUsername(user.getLogin()).isEmpty());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void findDepartmentByUsername_daoException_serviceException(User user)
            throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        adminHeadService.findDepartmentByUsername(user.getLogin());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "appointDepartmentHead",
            dependsOnGroups = {"findDepartmentByUsername", "performUserRolesAction"})
    public void appointDepartmentHead_correctAppointment_true(User user) throws DaoException, ServiceException {
        user.getRoles().add(Role.DOCTOR);
        User previous = new User();
        previous.setId(1);
        previous.setLogin("qwe");
        previous.setPassword("qwe");
        previous.setRoles(List.of(new Role[]{Role.CLIENT, Role.DOCTOR, Role.DEPARTMENT_HEAD}));
        Mockito.when(userDao.findByLogin(user.getLogin())) //1
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findHeadDepartment(Department.INFECTIOUS)) //2
                .thenReturn(Optional.of(previous));
        Mockito.when(userDao.findByLogin(user.getLogin())) //3
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin())) //4
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Mockito.when(userDao.findByLogin(previous.getLogin())). //5
                thenReturn(Optional.of(previous));
        Mockito.when(userDao.deleteUserRole(previous.getLogin(), Role.DEPARTMENT_HEAD)) //6
                .thenReturn(true);
        Mockito.when(departmentDao.updateDepartmentHead(Department.INFECTIOUS, user.getLogin())) //7
                .thenReturn(true);
        Mockito.when(userDao.findByLogin(user.getLogin())). //8
                thenReturn(Optional.of(user));
        Mockito.when(userDao.addUserRole(user.getLogin(), Role.DEPARTMENT_HEAD)) //9
                .thenReturn(true);
        Assert.assertTrue(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "appointDepartmentHead",
            dependsOnGroups = {"findDepartmentByUsername", "performUserRolesAction"}, enabled = false)
    public void appointDepartmentHead_nonExistentNewHead_false(User user) throws DaoException, ServiceException {
        user.getRoles().add(Role.DOCTOR);
        User previous = new User();
        previous.setId(1);
        previous.setLogin("qwe");
        previous.setPassword("qwe");
        previous.setRoles(List.of(new Role[]{Role.CLIENT, Role.DOCTOR, Role.DEPARTMENT_HEAD}));
        Mockito.when(userDao.findByLogin(user.getLogin())) //1
                .thenReturn(Optional.empty());
        Assert.assertFalse(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "appointDepartmentHead",
            dependsOnGroups = {"findDepartmentByUsername", "performUserRolesAction"})
    public void appointDepartmentHead_departmentNewHeadIsNotEqualsThisDepartment_false(User user)
            throws DaoException, ServiceException {
        user.getRoles().add(Role.DOCTOR);
        User previous = new User();
        previous.setId(1);
        previous.setLogin("qwe");
        previous.setPassword("qwe");
        previous.setRoles(List.of(new Role[]{Role.CLIENT, Role.DOCTOR, Role.DEPARTMENT_HEAD}));
        Mockito.when(userDao.findByLogin(user.getLogin())) //1
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findHeadDepartment(Department.INFECTIOUS)) //2
                .thenReturn(Optional.of(previous));
        Mockito.when(userDao.findByLogin(user.getLogin())) //3
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin())) //4
                .thenReturn(Optional.of(Department.SURGERY));
        Assert.assertFalse(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "appointDepartmentHead",
            dependsOnGroups = {"findDepartmentByUsername", "performUserRolesAction"})
    public void appointDepartmentHead_newHeadEqualsPrevious_false(User user) throws DaoException, ServiceException {
        user.getRoles().add(Role.DOCTOR);
        User previous = new User();
        previous.setId(1);
        previous.setLogin("qwe");
        previous.setPassword("qwe");
        previous.setRoles(List.of(new Role[]{Role.CLIENT, Role.DOCTOR, Role.DEPARTMENT_HEAD}));
        Mockito.when(userDao.findByLogin(user.getLogin())) //1
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findHeadDepartment(Department.INFECTIOUS)) //2
                .thenReturn(Optional.of(user));
        Mockito.when(userDao.findByLogin(user.getLogin())) //3
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin())) //4
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Assert.assertFalse(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "appointDepartmentHead",
            dependsOnGroups = {"findDepartmentByUsername", "performUserRolesAction"})
    public void appointDepartmentHead_newHeadIsNotDoctor_false(User user) throws DaoException, ServiceException {
        User previous = new User();
        previous.setId(1);
        previous.setLogin("qwe");
        previous.setPassword("qwe");
        previous.setRoles(List.of(new Role[]{Role.CLIENT, Role.DOCTOR, Role.DEPARTMENT_HEAD}));
        Mockito.when(userDao.findByLogin(user.getLogin())) //1
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findHeadDepartment(Department.INFECTIOUS)) //2
                .thenReturn(Optional.of(user));
        Mockito.when(userDao.findByLogin(user.getLogin())) //3
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin())) //4
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Assert.assertFalse(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "appointDepartmentHead",
            dependsOnGroups = {"findDepartmentByUsername", "performUserRolesAction"})
    public void appointDepartmentHead_previousNonExistent_true(User user) throws DaoException, ServiceException {
        user.getRoles().add(Role.DOCTOR);
        User previous = new User();
        previous.setId(1);
        previous.setLogin("qwe");
        previous.setPassword("qwe");
        previous.setRoles(List.of(new Role[]{Role.CLIENT, Role.DOCTOR, Role.DEPARTMENT_HEAD}));
        Mockito.when(userDao.findByLogin(user.getLogin())) //1
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findHeadDepartment(Department.INFECTIOUS)) //2
                .thenReturn(Optional.empty());
        Mockito.when(userDao.findByLogin(user.getLogin())) //3
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin())) //4
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Mockito.when(departmentDao.updateDepartmentHead(Department.INFECTIOUS, user.getLogin())) //7
                .thenReturn(true);
        Mockito.when(userDao.findByLogin(user.getLogin())). //8
                thenReturn(Optional.of(user));
        Mockito.when(userDao.addUserRole(user.getLogin(), Role.DEPARTMENT_HEAD)) //9
                .thenReturn(true);
        Assert.assertTrue(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void appointDepartmentHead_daoException_serviceException(User user) throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performDepartmentStaffAction",
            dependsOnGroups = "performUserRolesAction")
    public void performDepartmentStaffAction_changeDepartment_true(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin()))
                .thenReturn(Optional.of(Department.INFECTIOUS));
        Mockito.when(departmentStaffDao.updateStaffDepartment(Department.SURGERY,
                ServiceAction.ADD, user.getLogin()))
                .thenReturn(true);
        Assert.assertTrue(adminHeadService
                .updateDepartmentStaff(Department.SURGERY, ServiceAction.ADD, user.getLogin(), Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performDepartmentStaffAction",
            dependsOnGroups = "performUserRolesAction")
    public void performDepartmentStaffAction_addToDepartment_true(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Mockito.when(departmentDao.findDepartment(user.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(departmentStaffDao.updateStaffDepartment(Department.INFECTIOUS,
                ServiceAction.ADD, user.getLogin()))
                .thenReturn(true);
        Assert.assertTrue(adminHeadService
                .updateDepartmentStaff(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin(), Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performDepartmentStaffAction",
            dependsOnGroups = "performUserRolesAction")
    public void performDepartmentStaffAction_nonExistentUser_false(User user) throws DaoException, ServiceException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.empty());
        Assert.assertFalse(adminHeadService
                .updateDepartmentStaff(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin(), Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser", groups = "performDepartmentStaffAction",
            dependsOnGroups = "performUserRolesAction")
    public void performDepartmentStaffAction_headOfDepartment_false(User user) throws DaoException, ServiceException {
        user.getRoles().add(Role.DEPARTMENT_HEAD);
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenReturn(Optional.of(user));
        Assert.assertFalse(adminHeadService
                .updateDepartmentStaff(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin(), Role.DOCTOR));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser",
            expectedExceptions = ServiceException.class)
    public void performDepartmentStaffAction_daoException_serviceException(User user)
            throws ServiceException, DaoException {
        Mockito.when(userDao.findByLogin(user.getLogin()))
                .thenThrow(DaoException.class);
        adminHeadService
                .updateDepartmentStaff(Department.INFECTIOUS, ServiceAction.ADD, user.getLogin(), Role.DOCTOR);
    }

    @Test(groups = "findDepartmentsHeads")
    public void findDepartmentsHeads_correctFind_allHeads() throws DaoException, ServiceException {
        Map<Department, String> headMap = new HashMap<>();
        headMap.put(Department.INFECTIOUS, "user1");
        headMap.put(Department.CARDIOLOGY, "user2");
        headMap.put(Department.NEUROLOGY, "user3");
        headMap.put(Department.OTORHINOLARYNGOLOGY, "user4");
        headMap.put(Department.PEDIATRIC, "user5");
        headMap.put(Department.THERAPEUTIC, "user6");
        headMap.put(Department.UROLOGY, "user7");
        headMap.put(Department.TRAUMATOLOGY, "user8");
        headMap.put(Department.SURGERY, "user9");

        Mockito.when(departmentDao.findDepartmentsHeads()).thenReturn(headMap);
        Assert.assertEquals(adminHeadService.findDepartmentsHeads().size(), 9);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void findDepartmentsHeads_daoException_serviceException() throws DaoException, ServiceException {
        Mockito.when(departmentDao.findDepartmentsHeads()).thenThrow(DaoException.class);
        adminHeadService.findDepartmentsHeads();
    }
}
