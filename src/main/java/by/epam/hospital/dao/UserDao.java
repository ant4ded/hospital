package by.epam.hospital.dao;

import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;

import java.util.Optional;

public interface UserDao {
    void create(User user) throws DaoException;

    void update(User oldValue, User newValue) throws DaoException;

    Optional<User> find(String login) throws DaoException;

    Optional<User> findById(int id) throws DaoException;

    void updateUserRoles(String login, String action, Role role) throws DaoException;
}
