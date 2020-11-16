package epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.doctor.CloseTherapy;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
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
import java.util.Optional;

public class CloseTherapyTest {
    private static final String MESSAGE_WRONG_RESULT = "Patient not exist.";
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
        httpCommand = new CloseTherapy(doctorService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_makeLastDiagnosisFinal_mapWithMessageSuccess(User patient)
            throws ServiceException, IOException, ServletException {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(patient.getUserDetails().getFirstName());
        userDetails.setSurname(patient.getUserDetails().getSurname());
        userDetails.setLastName(patient.getUserDetails().getLastName());
        userDetails.setBirthday(patient.getUserDetails().getBirthday());
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(patient.getUserDetails().getFirstName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(patient.getUserDetails().getSurname());
        Mockito.when(request.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(patient.getUserDetails().getLastName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(patient.getUserDetails().getBirthday()));
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(CardType.AMBULATORY.name());
        Mockito.when(doctorService.findPatientByUserDetails(patient.getUserDetails()))
                .thenReturn(Optional.of(patient));
        Mockito.when(doctorService.closeTherapy(patient.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(true);
        Mockito.when(doctorService.findOpenDoctorTherapies(patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(new ArrayList<>(List.of(new Therapy())));

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.THERAPIES_LIST));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_makeLastDiagnosisFinal_mapWithMessage(User patient)
            throws ServiceException, IOException, ServletException {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(patient.getUserDetails().getFirstName());
        userDetails.setSurname(patient.getUserDetails().getSurname());
        userDetails.setLastName(patient.getUserDetails().getLastName());
        userDetails.setBirthday(patient.getUserDetails().getBirthday());
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(patient.getUserDetails().getFirstName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(patient.getUserDetails().getSurname());
        Mockito.when(request.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(patient.getUserDetails().getLastName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(patient.getUserDetails().getBirthday()));
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(CardType.AMBULATORY.name());
        Mockito.when(doctorService.findPatientByUserDetails(patient.getUserDetails()))
                .thenReturn(Optional.of(patient));
        Mockito.when(doctorService.closeTherapy(patient.getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(false);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsValue(MESSAGE_WRONG_RESULT));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void execute_makeLastDiagnosisFinal_mapWithCommandException(User patient)
            throws ServiceException, IOException, ServletException {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(patient.getUserDetails().getFirstName());
        userDetails.setSurname(patient.getUserDetails().getSurname());
        userDetails.setLastName(patient.getUserDetails().getLastName());
        userDetails.setBirthday(patient.getUserDetails().getBirthday());
        Mockito.when(request.getSession())
                .thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(patient.getLogin());
        Mockito.when(request.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(patient.getUserDetails().getFirstName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(patient.getUserDetails().getSurname());
        Mockito.when(request.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(patient.getUserDetails().getLastName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(patient.getUserDetails().getBirthday()));
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(CardType.AMBULATORY.name());
        Mockito.when(doctorService.findPatientByUserDetails(patient.getUserDetails()))
                .thenThrow(ServiceException.class);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}