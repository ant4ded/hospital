package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.table.UsersFieldName;
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
    private static final String MESSAGE_SUCCESS = "Success.";

    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(UsersFieldName.LOGIN);
        Department doctorDepartment = null;
        ServiceAction serviceAction = ServiceAction.valueOf(request.getParameter(ParameterName.ACTION));
        Role role = Role.valueOf(request.getParameter(ParameterName.ROLE));
        try {
            if (role != Role.DOCTOR && role != Role.MEDICAL_ASSISTANT && role != Role.DEPARTMENT_HEAD) {
                adminHeadService.performUserRolesAction(login, serviceAction, role);
            }
            if (role == Role.DEPARTMENT_HEAD) {
                doctorDepartment = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));
                adminHeadService.appointDepartmentHead(doctorDepartment, login);
            }
            if (role == Role.DOCTOR || role == Role.MEDICAL_ASSISTANT) {
                doctorDepartment = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));
                adminHeadService.performDepartmentStaffAction(doctorDepartment, serviceAction, login, role);
            }
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);

            request.setAttribute(ParameterName.DEPARTMENT, doctorDepartment);
            request.setAttribute(ParameterName.USER_ROLES, roles);
            request.setAttribute(ParameterName.MESSAGE, MESSAGE_SUCCESS);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(request, response);
    }
}
