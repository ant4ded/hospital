package epam.hospital.controller.command.receptionist;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.receptionist.FindUserCredentials;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ReceptionistService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FindUserCredentialsTest {
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private ReceptionistService receptionistService;
    @Mock
    private Logger logger;
    private HttpCommand httpCommand;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        httpCommand = new FindUserCredentials(receptionistService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_userPresent_userLoginAndPassword(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(user.getUserDetails().getFirstName());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(user.getUserDetails().getSurname());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(user.getUserDetails().getLastName());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(user.getUserDetails().getBirthday()));
        Mockito.when(receptionistService.findUserCredentials(user.getUserDetails()))
                .thenReturn(Optional.of(user));
        Map<String, Object> result = httpCommand.execute(httpServletRequest, httpServletResponse);
        Assert.assertTrue(result.containsKey(UsersFieldName.LOGIN) &&
                result.containsKey(UsersFieldName.PASSWORD));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_userEmpty_mapWithMessage(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(user.getUserDetails().getFirstName());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(user.getUserDetails().getSurname());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(user.getUserDetails().getLastName());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(user.getUserDetails().getBirthday()));
        Mockito.when(receptionistService.findUserCredentials(user.getUserDetails()))
                .thenReturn(Optional.empty());
        Assert.assertTrue(httpCommand.execute(httpServletRequest, httpServletResponse)
                .containsKey(ParameterName.MESSAGE));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_serviceException_mapWithCommandException(User user)
            throws ServiceException, IOException, ServletException {
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(user.getUserDetails().getFirstName());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(user.getUserDetails().getSurname());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(user.getUserDetails().getLastName());
        Mockito.when(httpServletRequest.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(user.getUserDetails().getBirthday()));
        Mockito.when(receptionistService.findUserCredentials(user.getUserDetails()))
                .thenThrow(ServiceException.class);
        Assert.assertTrue(httpCommand.execute(httpServletRequest, httpServletResponse)
                .containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}