package epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.doctor.MakeLastDiagnosisFinal;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.DoctorService;
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

public class MakeLastDiagnosisFinalTest {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Therapies list empty or patient not exist.";
    @Mock
    private Logger logger;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private DoctorService doctorService;
    private HttpCommand httpCommand;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        httpCommand = new MakeLastDiagnosisFinal(doctorService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_makeLastDiagnosisFinal_mapWithMessageSuccess(User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(String.valueOf(CardType.AMBULATORY));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(patient.getLogin());
        Mockito.when(doctorService.makeLastDiagnosisFinal(patient.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(true);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsValue(MESSAGE_SUCCESS));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_makeLastDiagnosisFinal_mapWithMessage(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(String.valueOf(CardType.AMBULATORY));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(patient.getLogin());
        Mockito.when(doctorService.makeLastDiagnosisFinal(patient.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(false);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsValue(MESSAGE_WRONG_RESULT));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_makeLastDiagnosisFinal_mapWithCommandException(User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(String.valueOf(CardType.AMBULATORY));
        Mockito.when(request.getParameter(UsersFieldName.LOGIN))
                .thenReturn(patient.getLogin());
        Mockito.when(doctorService.makeLastDiagnosisFinal(patient.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenThrow(ServiceException.class);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}