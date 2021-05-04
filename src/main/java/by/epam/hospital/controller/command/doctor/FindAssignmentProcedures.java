package by.epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.ProcedureAssignment;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindAssignmentProcedures implements HttpCommand {
    private final DoctorService service;
    private final Logger logger;

    public FindAssignmentProcedures(DoctorService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        int diagnosisId = Integer.parseInt(request.getParameter(ParameterName.DIAGNOSIS_ID));
        try {
            List<ProcedureAssignment> assignments = service.findAllAssignmentProceduresToDiagnosis(diagnosisId);
            result.put(ParameterName.ASSIGNMENT_PROCEDURES, assignments);
            // TODO: 02.05.2021 page forward
//            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
