package epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.doctor.DiagnoseDisease;
import by.epam.hospital.controller.command.doctor.FindPatientTherapies;
import by.epam.hospital.entity.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.testng.Assert.*;

public class FindPatientTherapiesTest {
    @Mock
    private Logger logger;
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
        httpCommand = new FindPatientTherapies(doctorService, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithMessageSuccess(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(patient.getUserDetails().getFirstName());
        userDetails.setSurname(patient.getUserDetails().getSurname());
        userDetails.setLastName(patient.getUserDetails().getLastName());
        userDetails.setBirthday(patient.getUserDetails().getBirthday());
        List<Therapy> therapies = new ArrayList<>(List.of(new Therapy[]{new Therapy()}));
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
        Mockito.when(doctorService.findAllPatientTherapies(userDetails, CardType.AMBULATORY))
                .thenReturn(therapies);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.THERAPIES_LIST));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithMessageIncorrectIcd(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(patient.getUserDetails().getFirstName());
        userDetails.setSurname(patient.getUserDetails().getSurname());
        userDetails.setLastName(patient.getUserDetails().getLastName());
        userDetails.setBirthday(patient.getUserDetails().getBirthday());
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
        Mockito.when(doctorService.findAllPatientTherapies(userDetails, CardType.AMBULATORY))
                .thenReturn(new ArrayList<>());

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.MESSAGE));
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectDiagnosisAndPatient")
    public void execute_correctAuthorization_mapWithCommandException(Diagnosis diagnosis, User patient)
            throws ServiceException, IOException, ServletException {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(patient.getUserDetails().getFirstName());
        userDetails.setSurname(patient.getUserDetails().getSurname());
        userDetails.setLastName(patient.getUserDetails().getLastName());
        userDetails.setBirthday(patient.getUserDetails().getBirthday());
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
        Mockito.when(doctorService.findAllPatientTherapies(userDetails, CardType.AMBULATORY))
                .thenThrow(ServiceException.class);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsKey(ParameterName.COMMAND_EXCEPTION));
    }
}