package by.epam.hospital.dao;

import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.util.Action;

import java.util.Optional;

public interface UserDao {
    void create(User user) throws DaoException;

    void update(User oldValue, User newValue) throws DaoException;

    Optional<User> find(String login) throws DaoException;

    Optional<User> findById(int id) throws DaoException;

    void updateUserRoles(String login, Action action, Role role) throws DaoException;
}
