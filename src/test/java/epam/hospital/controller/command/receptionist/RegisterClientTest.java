package epam.hospital.controller.command.receptionist;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.receptionist.RegisterClient;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;
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
import java.util.Map;

public class RegisterClientTest {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Move doctor to another department was not perform.";

    @Mock
    private Logger logger;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ReceptionistService receptionistService;
    private HttpCommand httpCommand;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        httpCommand = new RegisterClient(receptionistService, logger);
    }

    @Test
    public void execute_successRegisterClient_mapWithSuccessMessage()
            throws ServiceException, IOException, ServletException {
        Mockito.when(receptionistService.registerClient(Mockito.any(User.class)))
                .thenReturn(true);
        Mockito.when(request.getParameter(Mockito.anyString()))
                .thenReturn(Mockito.anyString());
        Mockito.when(request.getParameter(UsersDetailsFieldName.GENDER))
                .thenReturn(UserDetails.Gender.MALE.toString());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn("2000-01-12");
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.get(ParameterName.MESSAGE), MESSAGE_SUCCESS);
    }

    @Test
    public void execute_nonSuccessRegisterClient_mapWithWrongResultMessage()
            throws ServiceException, IOException, ServletException {
        Mockito.when(receptionistService.registerClient(Mockito.any(User.class)))
                .thenReturn(false);
        Mockito.when(request.getParameter(Mockito.anyString()))
                .thenReturn(Mockito.anyString());
        Mockito.when(request.getParameter(UsersDetailsFieldName.GENDER))
                .thenReturn(UserDetails.Gender.MALE.toString());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn("2000-01-12");
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.get(ParameterName.MESSAGE), MESSAGE_WRONG_RESULT);
    }

    @Test
    public void execute_serviceException_commandExceptionParameter()
            throws ServiceException, IOException, ServletException {
        Mockito.when(receptionistService.registerClient(Mockito.any(User.class)))
                .thenThrow(ServiceException.class);
        Mockito.when(request.getParameter(Mockito.anyString()))
                .thenReturn(Mockito.anyString());
        Mockito.when(request.getParameter(UsersDetailsFieldName.GENDER))
                .thenReturn(UserDetails.Gender.MALE.toString());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn("2000-01-12");
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}
