package by.epam.hospital.controller.command.admin.head;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.dao.impl.DepartmentDaoImpl;
import by.epam.hospital.dao.impl.DepartmentStaffDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
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

public class FindRoleControlAttributes implements HttpCommand {
    private final AdminHeadService adminHeadService = new AdminHeadServiceImpl(new UserDaoImpl(),
            new DepartmentDaoImpl(), new DepartmentStaffDaoImpl());

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(UsersFieldName.LOGIN);
        try {
            Department userDepartment = null;
            ArrayList<Role> roles = adminHeadService.findUserRoles(login);
            if (roles.contains(Role.DOCTOR)) {
                userDepartment = adminHeadService.findDepartmentByUsername(login).orElseThrow(ServiceException::new);
            }

            request.setAttribute(ParameterName.DEPARTMENT, userDepartment);
            request.setAttribute(ParameterName.USER_ROLES, roles);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }
        request.getRequestDispatcher(HospitalUrl.PAGE_ROLE_CONTROL).forward(request, response);
    }
}
