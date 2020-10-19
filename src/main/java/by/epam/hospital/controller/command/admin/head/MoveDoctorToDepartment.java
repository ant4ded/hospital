package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;
import by.epam.hospital.service.ServiceAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class MoveDoctorToDepartment implements HttpCommand {
    private static final String SUCCESSFUL_MESSAGE_PART1 = "Doctor was moved to ";
    private static final String UNSUCCESSFUL_MESSAGE_PART1 = "Doctor was not moved to ";
    private static final String MESSAGE_PART2 = " department";

    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(ParameterName.LOGIN);
        Department department = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));

        try {
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);
            String message = UNSUCCESSFUL_MESSAGE_PART1 + department.name().toLowerCase() + MESSAGE_PART2;
            if (adminHeadService.performDepartmentStaffAction(department, ServiceAction.ADD, login)) {
                message = SUCCESSFUL_MESSAGE_PART1 + department.name().toLowerCase() + MESSAGE_PART2;
            }
            department = adminHeadService.findDepartmentByUsername(login);
            request.setAttribute(ParameterName.LOGIN, login);
            request.setAttribute(ParameterName.MESSAGE, message);
            request.setAttribute(ParameterName.USER_ROLES, roles);
            request.setAttribute(ParameterName.DEPARTMENT, department);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_DEPARTMENT_CONTROL).forward(request, response);
    }
}
