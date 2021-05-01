package epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.admin.head.ProcedureControl;
import by.epam.hospital.entity.Procedure;
import by.epam.hospital.entity.table.ProceduresFieldName;
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
import java.util.Map;

public class ProcedureControlTest {
    private static final String MESSAGE_SUCCESS = "Success.";

    @Mock
    private Logger logger;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AdminHeadService service;
    private HttpCommand httpCommand;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        httpCommand = new ProcedureControl(service, logger);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectProcedure")
    public void procedureControl_addActionAndValidRequestParameters_success(Procedure procedure)
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getParameter(ParameterName.ACTION))
                .thenReturn(String.valueOf(ServiceAction.ADD));
        Mockito.when(request.getParameter(ProceduresFieldName.NAME))
                .thenReturn(procedure.getName());
        Mockito.when(request.getParameter(ProceduresFieldName.COST))
                .thenReturn(String.valueOf(procedure.getCost()));
        Mockito.when(request.getParameter(ProceduresFieldName.IS_ENABLED))
                .thenReturn(String.valueOf(procedure.isEnabled()));
        Mockito.when(service.createProcedureOrMedicament(procedure, Procedure.class))
                .thenReturn(true);

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue(result.containsValue(MESSAGE_SUCCESS));
    }
}
