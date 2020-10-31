package by.epam.hospital.dao;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ServiceAction;

import java.util.Map;

public interface DepartmentStaffDao {
    boolean updateStaffDepartment(Department department, ServiceAction serviceAction, String login) throws DaoException;

    Map<String, User> findDepartmentStaff(Department department) throws DaoException;
}
