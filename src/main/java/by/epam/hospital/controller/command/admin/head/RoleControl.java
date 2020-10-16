package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.Role;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RoleControl implements Command {
    private static final String SUCCESSFUL_MESSAGE = "Roles have been updated";
    private static final String UNSUCCESSFUL_MESSAGE = "Nonexistent login";
    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(ParameterName.LOGIN);
        String action = request.getParameter(ParameterName.ACTION);
        Role role = Role.valueOf(request.getParameter(ParameterName.ROLE));

        request.setAttribute(ParameterName.MESSAGE, UNSUCCESSFUL_MESSAGE);

        try {
            if (adminHeadService.updateUserRoles(login, action, role)) {
                Map<String, Role> roles = adminHeadService.findUserRoles(login);
                request.setAttribute(ParameterName.USER_ROLES, roles);
                request.setAttribute(ParameterName.MESSAGE, SUCCESSFUL_MESSAGE);
            }
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(request, response);
    }
}
