package by.epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.MedicamentAssignment;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindAssignmentMedications implements HttpCommand {
    private final DoctorService service;
    private final Logger logger;

    public FindAssignmentMedications(DoctorService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        int diagnosisId = Integer.parseInt(request.getParameter(ParameterName.DIAGNOSIS_ID));
        try {
            List<MedicamentAssignment> assignments = service.findAllAssignmentMedicationsToDiagnosis(diagnosisId);
            result.put(ParameterName.ASSIGNMENT_MEDICATIONS, assignments);
            // TODO: 02.05.2021 page forward
//            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
