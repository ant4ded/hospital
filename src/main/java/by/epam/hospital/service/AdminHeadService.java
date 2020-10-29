package by.epam.hospital.service;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;

import java.util.ArrayList;
import java.util.Map;

public interface AdminHeadService {
    ArrayList<Role> findUserRoles(String login) throws ServiceException;

    void performUserRolesAction(String login, ServiceAction serviceAction, Role role) throws ServiceException;

    void appointDepartmentHead(Department department, String login) throws ServiceException;

    void performDepartmentStaffAction(Department department, ServiceAction serviceAction,
                                      String login, Role appointedRole) throws ServiceException;

    Department findDepartmentByUsername(String login) throws ServiceException;

    Map<Department, String> findDepartmentsHeads() throws ServiceException;

    void roleControl(String login, Department department, ServiceAction serviceAction, Role role)
            throws ServiceException;
}
