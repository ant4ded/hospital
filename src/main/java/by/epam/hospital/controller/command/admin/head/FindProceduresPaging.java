package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.PageResult;
import by.epam.hospital.entity.Procedure;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class FindProceduresPaging implements HttpCommand {
    private final AdminHeadService service;
    private final Logger logger;

    public FindProceduresPaging(AdminHeadService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String namePart = request.getParameter(ParameterName.NAME_PART);
        int pageNumber = Integer.parseInt(request.getParameter(ParameterName.PAGE_NUMBER));
        try {
            PageResult<Procedure> pageResult = service.findAllProceduresByNamePartPaging(namePart, pageNumber);
            result.put(ParameterName.TOTAL_PAGES, pageResult.getTotalPages());
            result.put(ParameterName.PAGE_NUMBER, pageNumber);
            result.put(ParameterName.PROCEDURE_LIST, pageResult.getList());
            // TODO: 02.05.2021 page forward
//            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
