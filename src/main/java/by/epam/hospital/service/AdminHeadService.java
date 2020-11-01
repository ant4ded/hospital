package by.epam.hospital.service;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public interface AdminHeadService {
    ArrayList<Role> findUserRoles(String login) throws ServiceException;

    boolean performUserRolesAction(String login, ServiceAction serviceAction, Role role) throws ServiceException;

    boolean appointDepartmentHead(Department department, String login) throws ServiceException;

    boolean performDepartmentStaffAction(Department department, ServiceAction serviceAction,
                                         String login, Role appointedRole) throws ServiceException;

    Optional<Department> findDepartmentByUsername(String login) throws ServiceException;

    Map<Department, String> findDepartmentsHeads() throws ServiceException;
}
