package epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.admin.head.MoveDoctorToDepartment;
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

public class MoveDoctorToDepartmentTest {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Move doctor to another department was not perform.";

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
        httpCommand = new MoveDoctorToDepartment(adminHeadService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_doctor_mapWithSuccessMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Map<String, Object> expected = new HashMap<>();
        expected.put(UsersFieldName.LOGIN, user.getLogin());
        expected.put(ParameterName.USER_ROLES, roles);
        expected.put(ParameterName.DEPARTMENT, Department.INFECTIOUS);
        expected.put(ParameterName.MESSAGE, MESSAGE_SUCCESS);
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);
        Mockito.when(adminHeadService.performDepartmentStaffAction(Department.INFECTIOUS, ServiceAction.ADD,
                user.getLogin(), Role.DOCTOR))
                .thenReturn(true);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_medicalAssistant_mapWithSuccessMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.MEDICAL_ASSISTANT);
        Map<String, Object> expected = new HashMap<>();
        expected.put(UsersFieldName.LOGIN, user.getLogin());
        expected.put(ParameterName.USER_ROLES, roles);
        expected.put(ParameterName.DEPARTMENT, Department.INFECTIOUS);
        expected.put(ParameterName.MESSAGE, MESSAGE_SUCCESS);
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);
        Mockito.when(adminHeadService.performDepartmentStaffAction(Department.INFECTIOUS, ServiceAction.ADD,
                user.getLogin(), Role.MEDICAL_ASSISTANT))
                .thenReturn(true);

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
        expected.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);
        Mockito.when(adminHeadService.performDepartmentStaffAction(Department.INFECTIOUS, ServiceAction.ADD,
                user.getLogin(), Role.DOCTOR))
                .thenReturn(false);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_serviceException_commandExceptionParameter(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(adminHeadService.findUserRoles(user.getLogin()))
                .thenReturn(roles);
        Mockito.when(adminHeadService.performDepartmentStaffAction(Department.INFECTIOUS, ServiceAction.ADD,
                user.getLogin(), Role.DOCTOR))
                .thenThrow(ServiceException.class);
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}
