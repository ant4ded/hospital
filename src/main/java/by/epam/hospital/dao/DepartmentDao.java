package by.epam.hospital.dao;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;

import java.util.Map;
import java.util.Optional;

public interface DepartmentDao {
    Optional<User> findHeadDepartment(Department department) throws DaoException;

    boolean updateDepartmentHead(Department department, String login) throws DaoException;

    Department findDepartment(String login) throws DaoException;

    Map<Department, String> findDepartmentsHeads() throws DaoException;
}
