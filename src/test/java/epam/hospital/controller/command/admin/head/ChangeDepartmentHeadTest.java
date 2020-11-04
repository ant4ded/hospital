package epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.admin.head.ChangeDepartmentHead;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.AdminHeadService;
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

public class ChangeDepartmentHeadTest {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Change department head was not perform.";

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
        httpCommand = new ChangeDepartmentHead(adminHeadService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_correctFind_mapWithSuccessMessage(User user)
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
        Mockito.when(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()))
                .thenReturn(true);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_appointDepartmentHeadFalse_mapWithWrongResultMessage(User user)
            throws ServiceException, IOException, ServletException {
        ArrayList<Role> roles = user.getRoles();
        roles.add(Role.DOCTOR);
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()))
                .thenReturn(false);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.get(ParameterName.MESSAGE), MESSAGE_WRONG_RESULT);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_serviceException_commandExceptionParameter(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(user.getLogin());
        Mockito.when(request.getParameter(ParameterName.DEPARTMENT))
                .thenReturn(String.valueOf(Department.INFECTIOUS));
        Mockito.when(adminHeadService.appointDepartmentHead(Department.INFECTIOUS, user.getLogin()))
                .thenThrow(ServiceException.class);
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}
