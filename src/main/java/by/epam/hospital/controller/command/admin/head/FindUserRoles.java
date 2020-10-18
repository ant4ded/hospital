package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class FindUserRoles implements Command {
    private static final String UNSUCCESSFUL_MESSAGE = "Can not find user roles";

    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(ParameterName.LOGIN);
        try {
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);
            if (roles.isEmpty()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, UNSUCCESSFUL_MESSAGE);
            }
            if (roles.contains(Role.DEPARTMENT_HEAD) || roles.contains(Role.DOCTOR) ||
                    roles.contains(Role.MEDICAL_ASSISTANT)) {
                Department department = adminHeadService.findDepartmentByUsername(login);
                request.setAttribute(ParameterName.DEPARTMENT, department);
            }
            request.setAttribute(ParameterName.USER_ROLES, roles);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(request, response);
    }
}
