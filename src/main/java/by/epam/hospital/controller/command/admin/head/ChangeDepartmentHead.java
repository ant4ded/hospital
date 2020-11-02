package by.epam.hospital.controller.command.admin.head;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeDepartmentHead implements HttpCommand {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Change department head was not perform.";

    private final AdminHeadService adminHeadService;
    private final Logger logger;

    public ChangeDepartmentHead(AdminHeadService adminHeadService, Logger logger) {
        this.adminHeadService = adminHeadService;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String login = request.getParameter(UsersFieldName.LOGIN);
        Department department = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));
        try {
            if (adminHeadService.appointDepartmentHead(department, login)) {
                List<Role> roles = adminHeadService.findUserRoles(login);
                result.put(UsersFieldName.LOGIN, login);
                result.put(ParameterName.USER_ROLES, roles);
                result.put(ParameterName.DEPARTMENT, department);
                result.put(ParameterName.MESSAGE, MESSAGE_SUCCESS);
            } else {
                result.put(ParameterName.MESSAGE, MESSAGE_WRONG_RESULT);
            }
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
