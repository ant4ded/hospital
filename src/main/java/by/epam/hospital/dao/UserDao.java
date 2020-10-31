package by.epam.hospital.dao;

import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ServiceAction;

import java.util.Optional;

public interface UserDao {
    int create(User user) throws DaoException;

    User update(User oldValue, User newValue) throws DaoException;

    Optional<User> findByLogin(String login) throws DaoException;

    Optional<User> findById(int id) throws DaoException;

    boolean updateUserRoles(String login, ServiceAction serviceAction, Role role) throws DaoException;
}
