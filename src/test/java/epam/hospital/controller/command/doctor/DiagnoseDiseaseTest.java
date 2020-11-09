package epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.doctor.DiagnoseDisease;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.DiagnosesFieldName;
import by.epam.hospital.entity.table.IcdFieldName;
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
import java.util.Map;
import java.util.Optional;

public class DiagnoseDiseaseTest {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Patient not exist.";
    private static final String MESSAGE_INCORRECT_ICD = "Non existent icd code.";

    @Mock
    private Logger logger;
    @Mock
    private HttpSession httpSession;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private DoctorService doctorService;
    private HttpCommand httpCommand;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        httpCommand = new DiagnoseDisease(doctorService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithMessageSuccess(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(diagnosis.getDoctor().getLogin());
        Mockito.when(request.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(patient.getUserDetails().getFirstName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(patient.getUserDetails().getSurname());
        Mockito.when(request.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(patient.getUserDetails().getLastName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(patient.getUserDetails().getBirthday()));
        Mockito.when(request.getParameter(IcdFieldName.CODE))
                .thenReturn(String.valueOf(diagnosis.getIcd().getCode()));
        Mockito.when(request.getParameter(DiagnosesFieldName.REASON))
                .thenReturn(String.valueOf(diagnosis.getReason()));
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(CardType.AMBULATORY.name());
        Mockito.when(doctorService.findPatientByUserDetails(patient.getUserDetails()))
                .thenReturn(Optional.of(patient));
        Mockito.when(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(true);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.get(ParameterName.MESSAGE), MESSAGE_SUCCESS);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithMessageIncorrectIcd(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(diagnosis.getDoctor().getLogin());
        Mockito.when(request.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(patient.getUserDetails().getFirstName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(patient.getUserDetails().getSurname());
        Mockito.when(request.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(patient.getUserDetails().getLastName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(patient.getUserDetails().getBirthday()));
        Mockito.when(request.getParameter(IcdFieldName.CODE))
                .thenReturn(String.valueOf(diagnosis.getIcd().getCode()));
        Mockito.when(request.getParameter(DiagnosesFieldName.REASON))
                .thenReturn(String.valueOf(diagnosis.getReason()));
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(CardType.AMBULATORY.name());
        Mockito.when(doctorService.findPatientByUserDetails(patient.getUserDetails()))
                .thenReturn(Optional.of(patient));
        Mockito.when(doctorService.diagnoseDisease(diagnosis.getIcd().getCode(), diagnosis.getReason(),
                diagnosis.getDoctor().getLogin(), patient.getLogin(), CardType.AMBULATORY))
                .thenReturn(false);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.get(ParameterName.MESSAGE), MESSAGE_INCORRECT_ICD);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithMessageWrongResult(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(diagnosis.getDoctor().getLogin());
        Mockito.when(request.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(patient.getUserDetails().getFirstName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(patient.getUserDetails().getSurname());
        Mockito.when(request.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(patient.getUserDetails().getLastName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(patient.getUserDetails().getBirthday()));
        Mockito.when(request.getParameter(IcdFieldName.CODE))
                .thenReturn(String.valueOf(diagnosis.getIcd().getCode()));
        Mockito.when(request.getParameter(DiagnosesFieldName.REASON))
                .thenReturn(String.valueOf(diagnosis.getReason()));
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(CardType.AMBULATORY.name());
        Mockito.when(doctorService.findPatientByUserDetails(patient.getUserDetails()))
                .thenReturn(Optional.empty());

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertEquals(result.get(ParameterName.MESSAGE), MESSAGE_WRONG_RESULT);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithCommandException(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(ParameterName.LOGIN_USERNAME))
                .thenReturn(diagnosis.getDoctor().getLogin());
        Mockito.when(request.getParameter(UsersDetailsFieldName.FIRST_NAME))
                .thenReturn(patient.getUserDetails().getFirstName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.SURNAME))
                .thenReturn(patient.getUserDetails().getSurname());
        Mockito.when(request.getParameter(UsersDetailsFieldName.LAST_NAME))
                .thenReturn(patient.getUserDetails().getLastName());
        Mockito.when(request.getParameter(UsersDetailsFieldName.BIRTHDAY))
                .thenReturn(String.valueOf(patient.getUserDetails().getBirthday()));
        Mockito.when(request.getParameter(IcdFieldName.CODE))
                .thenReturn(String.valueOf(diagnosis.getIcd().getCode()));
        Mockito.when(request.getParameter(DiagnosesFieldName.REASON))
                .thenReturn(String.valueOf(diagnosis.getReason()));
        Mockito.when(request.getParameter(ParameterName.CARD_TYPE))
                .thenReturn(CardType.AMBULATORY.name());
        Mockito.when(doctorService.findPatientByUserDetails(patient.getUserDetails()))
                .thenThrow(new ServiceException());

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}