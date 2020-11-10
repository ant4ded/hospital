package epam.hospital.controller.command.client;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.client.FindUserDetails;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ClientService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class FindUserDetailsTest {
    @Mock
    private Logger logger;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private ClientService clientService;
    private HttpCommand httpCommand;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        httpCommand = new FindUserDetails(clientService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_successRegisterClient_mapWithSuccessMessage(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(user.getLogin());
        Mockito.when(clientService.findUserDetails(user.getLogin()))
                .thenReturn(Optional.of(user.getUserDetails()));
        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.size(), 9);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_nonSuccessRegisterClient_mapWithWrongResultMessage(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(user.getLogin());
        Mockito.when(clientService.findUserDetails(user.getLogin()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(httpCommand.execute(request, response).containsKey(ParameterName.MESSAGE));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_serviceException_commandExceptionParameter(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(user.getLogin());
        Mockito.when(clientService.findUserDetails(user.getLogin()))
                .thenThrow(ServiceException.class);
        Assert.assertTrue(httpCommand.execute(request, response).containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}