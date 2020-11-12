package epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.doctor.FindOpenDoctorTherapies;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindOpenDoctorTherapiesTest {
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
        httpCommand = new FindOpenDoctorTherapies(doctorService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_correctAuthorization_mapWithMessageSuccess(User patient)
            throws ServiceException, IOException, ServletException {
        List<Therapy> therapies = new ArrayList<>(List.of(new Therapy()));
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(String.valueOf(CardType.AMBULATORY));
        Mockito.when(doctorService.findOpenDoctorTherapies(patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(therapies);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.THERAPIES_LIST));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithMessage(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(String.valueOf(CardType.AMBULATORY));
        Mockito.when(doctorService.findOpenDoctorTherapies(patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(new ArrayList<>());


        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.MESSAGE));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_correctAuthorization_mapWithCommandException(User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(String.valueOf(CardType.AMBULATORY));
        Mockito.when(doctorService.findOpenDoctorTherapies(patient.getLogin(), CardType.AMBULATORY))
                .thenThrow(new ServiceException());

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}