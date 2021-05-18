package by.epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.*;
import by.epam.hospital.entity.table.ProceduresAssignmentFieldName;
import by.epam.hospital.entity.table.ProceduresFieldName;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.util.JsonConverter;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AssignProcedure implements HttpCommand {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Something wrong.";

    private final DoctorService service;
    private final Logger logger;

    public AssignProcedure(DoctorService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        User patient = JsonConverter.fromJson(JsonConverter.recoverJson(request.getParameter(ParameterName.PATIENT)), User.class);
        String doctorLogin = String.valueOf(request.getSession().getAttribute(ParameterName.LOGIN_USERNAME));
        CardType cardType = CardType.valueOf(request.getParameter(ParameterName.CARD_TYPE));
        String procedureName = request.getParameter(ProceduresFieldName.NAME);
        String description = request.getParameter(ProceduresAssignmentFieldName.DESCRIPTION);
        ProcedureAssignment assignment = new ProcedureAssignment(new Procedure(procedureName), description, LocalDateTime.now());
        boolean isSuccess;
        try {
            int pageNumber = 0;
            isSuccess = service.assignProcedureToLastDiagnosis(assignment, doctorLogin, patient.getLogin(), cardType);

            PageResult<Procedure> pageResult = service.findAllProceduresByNamePartPaging(null, pageNumber);
            pageResult.getList().removeIf(procedure -> !procedure.isEnabled());
            result.put(ParameterName.TOTAL_PAGES, pageResult.getTotalPages());
            result.put(ParameterName.PAGE_NUMBER, pageNumber);
            result.put(ParameterName.PROCEDURE_LIST, pageResult.getList());
            result.put(ParameterName.MESSAGE, isSuccess ? MESSAGE_SUCCESS : MESSAGE_WRONG_RESULT);
            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_ASSIGN_PROCEDURE);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
