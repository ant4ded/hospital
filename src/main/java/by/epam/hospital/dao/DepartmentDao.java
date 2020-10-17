package by.epam.hospital.dao;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;

import java.util.Optional;

public interface DepartmentDao {
    Optional<User> findHeadDepartment(Department department) throws DaoException;

    void updateDepartmentHead(Department department, String login) throws DaoException ;
}
