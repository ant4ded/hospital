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

public class MoveDoctorToDepartment implements HttpCommand {
    private static final String MESSAGE_SUCCESS = "Success.";

    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(UsersFieldName.LOGIN);
        Department department = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));
        try {
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);
            Role currentRole = roles.contains(Role.DOCTOR) ? Role.DOCTOR : Role.MEDICAL_ASSISTANT;
            adminHeadService.performDepartmentStaffAction(department, ServiceAction.ADD, login, currentRole);
            department = adminHeadService.findDepartmentByUsername(login);

            request.setAttribute(UsersFieldName.LOGIN, login);
            request.setAttribute(ParameterName.USER_ROLES, roles);
            request.setAttribute(ParameterName.DEPARTMENT, department);
            request.setAttribute(ParameterName.MESSAGE, MESSAGE_SUCCESS);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_DEPARTMENT_CONTROL).forward(request, response);
    }
}
