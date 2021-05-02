package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Medicament;
import by.epam.hospital.entity.table.ProceduresFieldName;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceAction;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class MedicamentControl implements HttpCommand {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Something wrong. Medicament was not updated.";

    private final AdminHeadService service;
    private final Logger logger;

    public MedicamentControl(AdminHeadService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String name = request.getParameter(ParameterName.PROCEDURE_OR_MEDICAMENT_NAME);
        boolean isEnabled = Boolean.parseBoolean(request.getParameter(ProceduresFieldName.IS_ENABLED));
        ServiceAction serviceAction = ServiceAction.valueOf(request.getParameter(ParameterName.ACTION));

        boolean isSuccess = false;
        try {
            if (serviceAction == ServiceAction.ADD) {
                isSuccess = service.createProcedureOrMedicament(new Medicament(name, true), Medicament.class);
            }
            if (serviceAction == ServiceAction.UPDATE) {
                isSuccess = service.updateEnabledStatusOnProcedureOrMedicament(new Medicament(name, isEnabled),
                        isEnabled, Medicament.class);
            }
            result.put(ParameterName.MESSAGE, isSuccess ? MESSAGE_SUCCESS : MESSAGE_WRONG_RESULT);
//            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
