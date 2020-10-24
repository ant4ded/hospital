package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class ChangeDepartmentHead implements HttpCommand {
    private static final String MESSAGE_PART1 = "Head of ";
    private static final String SUCCESSFUL_MESSAGE_PART2 = " department was changed";
    private static final String UNSUCCESSFUL_MESSAGE_PART2 = " department was not changed";

    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(UsersFieldName.LOGIN);
        Department department = Department.valueOf(request.getParameter(ParameterName.DEPARTMENT));

        try {
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);
            String message = MESSAGE_PART1 + department.name().toLowerCase() + UNSUCCESSFUL_MESSAGE_PART2;
            if (adminHeadService.appointDepartmentHead(department, login)) {
                message = MESSAGE_PART1 + department.name().toLowerCase() + SUCCESSFUL_MESSAGE_PART2;
                roles = adminHeadService.findUserRoles(login);
            }
            request.setAttribute(UsersFieldName.LOGIN, login);
            request.setAttribute(ParameterName.MESSAGE, message);
            request.setAttribute(ParameterName.USER_ROLES, roles);
            request.setAttribute(ParameterName.DEPARTMENT, department);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_DEPARTMENT_CONTROL).forward(request, response);
    }
}
