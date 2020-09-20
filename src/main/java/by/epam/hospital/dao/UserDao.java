package by.epam.hospital.dao;

import by.epam.hospital.entity.User;

public interface UserDao {
    void create(User user) throws DaoException;

    void update(User oldValue, User newValue) throws DaoException;

    User find(User user) throws DaoException;
}
