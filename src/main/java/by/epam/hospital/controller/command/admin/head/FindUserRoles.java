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

public class FindUserRoles implements Command {
    private static final String UNSUCCESSFUL_MESSAGE = "Can not find user roles";
    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(ParameterName.LOGIN);
        try {
            Map<String, Role> roles = adminHeadService.findUserRoles(login);
            if (roles.isEmpty()) {
                request.setAttribute(ParameterName.MESSAGE, UNSUCCESSFUL_MESSAGE);
                request.setAttribute(ParameterName.LOGIN, login);
            } else {
                request.setAttribute(ParameterName.USER_ROLES, roles);
            }
        } catch (ServiceException e) {
            request.setAttribute(ParameterName.MESSAGE, e.getMessage());
            request.getRequestDispatcher(HospitalUrl.PAGE_ERROR).forward(request, response);
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(request, response);
    }
}
