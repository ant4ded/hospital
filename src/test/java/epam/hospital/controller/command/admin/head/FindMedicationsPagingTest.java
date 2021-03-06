package epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.controller.command.admin.head.FindMedicationsPaging;
import by.epam.hospital.entity.PageResult;
import by.epam.hospital.service.AdminHeadService;
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
import java.util.Collections;
import java.util.Map;

public class FindMedicationsPagingTest {
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
        httpCommand = new FindMedicationsPaging(service, logger);
    }

    @Test
    public void procedureControl_addActionAndValidRequestParameters_success()
            throws ServiceException, IOException, ServletException {
        Mockito.when(request.getParameter(ParameterName.NAME_PART))
                .thenReturn("qwe");
        Mockito.when(request.getParameter(ParameterName.PAGE_NUMBER))
                .thenReturn(String.valueOf(3));
        Mockito.when(service.findAllMedicationsByNamePartPaging("qwe", 3))
                .thenReturn(PageResult.from(Collections.emptyList(), 3));

        Map<String, Object> result = httpCommand.execute(request, response);
        Assert.assertTrue((int) result.get(ParameterName.TOTAL_PAGES) != 0);
    }
}
