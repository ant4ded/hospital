package by.epam.hospital.dao;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.util.Action;

import java.util.Map;

public interface DepartmentStaffDao {
    void updateStaffDepartment(Department department, Action action, String login) throws DaoException;

    Map<String, User> findDepartmentStaff(Department department) throws DaoException;
}
