package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceAction;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class RoleControl implements HttpCommand {
    private static final String SUCCESSFUL_MESSAGE_ROLES_UPDATED = "Roles have been updated";
    private static final String SUCCESSFUL_MESSAGE_DEPARTMENT_HEAD = "Department head was set";
    private static final String SUCCESSFUL_MESSAGE_DEPARTMENT_STAFF = "Department staff have been updated";
    private static final String UNSUCCESSFUL_DEPARTMENT_HEAD_CHANGE = "User not exist or actually head of department";

    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean result;
        Department department = null;
        String login = request.getParameter(ParameterName.LOGIN);
        ServiceAction serviceAction = ServiceAction.valueOf(request.getParameter(ParameterName.ACTION));
        Role role = Role.valueOf(request.getParameter(ParameterName.ROLE));

        try {
            if (role != Role.DOCTOR && role != Role.MEDICAL_ASSISTANT && role != Role.DEPARTMENT_HEAD) {
                adminHeadService.performUserRolesAction(login, serviceAction, role);
                request.setAttribute(ParameterName.MESSAGE, SUCCESSFUL_MESSAGE_ROLES_UPDATED);
            }
            if (role == Role.DEPARTMENT_HEAD) {
                department = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));
                result = adminHeadService.appointDepartmentHead(department, login);
                request.setAttribute(ParameterName.MESSAGE, result ?
                        SUCCESSFUL_MESSAGE_DEPARTMENT_HEAD : UNSUCCESSFUL_DEPARTMENT_HEAD_CHANGE);
            }
            if (role == Role.DOCTOR || role == Role.MEDICAL_ASSISTANT) {
                department = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));
                result = adminHeadService.performDepartmentStaffAction(department, serviceAction, login);
                if (result) {
                    adminHeadService.performUserRolesAction(login, serviceAction, role);
                }
                request.setAttribute(ParameterName.MESSAGE, result ?
                        SUCCESSFUL_MESSAGE_DEPARTMENT_STAFF : UNSUCCESSFUL_DEPARTMENT_HEAD_CHANGE);
            }
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);
            request.setAttribute(ParameterName.DEPARTMENT, department);
            request.setAttribute(ParameterName.USER_ROLES, roles);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(request, response);
    }
}
