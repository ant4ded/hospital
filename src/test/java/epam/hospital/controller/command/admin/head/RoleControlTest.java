package epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.admin.head.RoleControl;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceAction;
import by.epam.hospital.service.ServiceException;
import epam.hospital.util.Provider;
import org.apache.log4j.Logger;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoleControlTest {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Update roles was not perform.";

    @Mock
    private Logger logger;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AdminHeadService adminHeadService;
    private HttpCommand httpCommand;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        httpCommand = new RoleControl(adminHeadService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_performUserRolesAction_mapWithSuccessMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Map<String, Object> expected = new HashMap<>();
        expected.put(ParameterName.DEPARTMENT, null);
        expected.put(ParameterName.USER_ROLES, roles);
        expected.put(ParameterName.MESSAGE, MESSAGE_SUCCESS);
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_ROLE_CONTROL);

        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ParameterName.ROLE))
                .thenReturn(String.valueOf(Role.ADMIN));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(adminHeadService.performUserRolesAction(user.getLogin(), ServiceAction.ADD, Role.ADMIN))
                .thenReturn(true);
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_appointDepartmentHead_mapWithSuccessMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Map<String, Object> expected = new HashMap<>();
        expected.put(ParameterName.DEPARTMENT, Department.INFECTIOUS);
        expected.put(ParameterName.USER_ROLES, roles);
        expected.put(ParameterName.MESSAGE, MESSAGE_SUCCESS);
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_ROLE_CONTROL);

        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ParameterName.ROLE))
                .thenReturn(String.valueOf(Role.DEPARTMENT_HEAD));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()))
                .thenReturn(true);
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_performDepartmentStaffAction_mapWithSuccessMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Map<String, Object> expected = new HashMap<>();
        expected.put(ParameterName.DEPARTMENT, Department.INFECTIOUS);
        expected.put(ParameterName.USER_ROLES, roles);
        expected.put(ParameterName.MESSAGE, MESSAGE_SUCCESS);
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_ROLE_CONTROL);

        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ParameterName.ROLE))
                .thenReturn(String.valueOf(Role.DOCTOR));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(adminHeadService.performDepartmentStaffAction(Department.INFECTIOUS, ServiceAction.ADD,
                user.getLogin(), Role.DOCTOR))
                .thenReturn(true);
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_performDepartmentStaffActionFalse_mapWithWrongResultMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Map<String, Object> expected = new HashMap<>();
        expected.put(ParameterName.MESSAGE, MESSAGE_WRONG_RESULT);
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_ROLE_CONTROL);

        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ParameterName.ROLE))
                .thenReturn(String.valueOf(Role.DOCTOR));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(adminHeadService.performDepartmentStaffAction(Department.INFECTIOUS, ServiceAction.ADD,
                user.getLogin(), Role.DOCTOR))
                .thenReturn(false);
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_appointDepartmentHeadFalse_mapWithSuccessMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Map<String, Object> expected = new HashMap<>();
        expected.put(ParameterName.MESSAGE, MESSAGE_WRONG_RESULT);
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_ROLE_CONTROL);

        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ParameterName.ROLE))
                .thenReturn(String.valueOf(Role.DEPARTMENT_HEAD));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()))
                .thenReturn(false);
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_performUserRolesActionFalse_mapWithWrongResultMessage(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ParameterName.ROLE))
                .thenReturn(String.valueOf(Role.ADMIN));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(adminHeadService.performUserRolesAction(user.getLogin(), ServiceAction.ADD, Role.ADMIN))
                .thenReturn(false);
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.get(ParameterName.MESSAGE), MESSAGE_WRONG_RESULT);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_serviceException_commandExceptionParameter(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ParameterName.ROLE))
                .thenReturn(String.valueOf(Role.ADMIN));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(adminHeadService.performUserRolesAction(user.getLogin(), ServiceAction.ADD, Role.ADMIN))
                .thenReturn(true);
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenThrow(ServiceException.class);
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}