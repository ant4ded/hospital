package by.epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.ProcedureAssignment;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.util.JsonConverter;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
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
        Diagnosis diagnosis = JsonConverter.fromJson(JsonConverter.recoverJson(request.getParameter(ParameterName.DIAGNOSIS)), Diagnosis.class);
        User patient = JsonConverter.fromJson(JsonConverter.recoverJson(request.getParameter(ParameterName.PATIENT)), User.class);
        try {
            List<ProcedureAssignment> assignments = service.findAllAssignmentProceduresToDiagnosis(diagnosis.getId());
            assignments.forEach(assignment -> assignment.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            result.put(ParameterName.DIAGNOSIS, diagnosis);
            result.put(ParameterName.PATIENT, patient);
            result.put(ParameterName.ASSIGNMENT_PROCEDURES_LIST, assignments);
            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_ASSIGNMENT_PROCEDURES);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
