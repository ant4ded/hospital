package by.epam.hospital.dao;

import by.epam.hospital.entity.UserDetails;

public interface UserDetailsDao {
    void create(UserDetails userDetails) throws DaoException;

    void update(UserDetails oldValue, UserDetails newValue) throws DaoException;

    void delete(UserDetails userDetails) throws DaoException;

    UserDetails find(UserDetails userDetails) throws DaoException;
}
