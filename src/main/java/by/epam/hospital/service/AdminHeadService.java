package by.epam.hospital.service;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.service.util.Action;

import java.util.Map;

public interface AdminHeadService {
    Map<String, Role> findUserRoles(String login) throws ServiceException;

    boolean performUserRolesAction(String login, Action action, Role role) throws ServiceException;

    boolean appointDepartmentHead(Department department, String login) throws ServiceException;

    boolean performDepartmentStaffAction(Department department, Action action, String login) throws ServiceException;
}
