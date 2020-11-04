package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FindDepartmentControlAttributes implements HttpCommand {
    private static final String MESSAGE_WRONG_RESULT = "Find roles and department was not perform.";

    private final AdminHeadService adminHeadService;
    private final Logger logger;

    public FindDepartmentControlAttributes(AdminHeadService adminHeadService, Logger logger) {
        this.adminHeadService = adminHeadService;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String login = request.getParameter(UsersFieldName.LOGIN);
        try {
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);
            Optional<Department> optionalDepartment = adminHeadService.findDepartmentByUsername(login);
            if (!roles.isEmpty() && optionalDepartment.isPresent()) {
                result.put(UsersFieldName.LOGIN, login);
                result.put(ParameterName.USER_ROLES, roles);
                result.put(ParameterName.DEPARTMENT, optionalDepartment.get());
            } else {
                result.put(ParameterName.MESSAGE, MESSAGE_WRONG_RESULT);
            }
            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DEPARTMENT_CONTROL);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
