package by.epam.hospital.service;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.service.util.Action;

import java.util.ArrayList;
import java.util.Map;

public interface AdminHeadService {
    ArrayList<Role> findUserRoles(String login) throws ServiceException;

    void performUserRolesAction(String login, Action action, Role role) throws ServiceException;

    boolean appointDepartmentHead(Department department, String login) throws ServiceException;

    boolean performDepartmentStaffAction(Department department, Action action, String login) throws ServiceException;

    Department findDepartmentByUsername(String login) throws ServiceException;

    Map<Department, String> findDepartmentsHeads() throws ServiceException;
}
