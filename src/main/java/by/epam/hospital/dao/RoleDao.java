package by.epam.hospital.dao;

import by.epam.hospital.entity.Role;

import java.util.List;

public interface RoleDao {
    List<Role> findAll() throws DaoException;
    int findRoleId(Role role) throws DaoException;
}
