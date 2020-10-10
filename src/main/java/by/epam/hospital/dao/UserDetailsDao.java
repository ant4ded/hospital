package by.epam.hospital.dao;

import by.epam.hospital.entity.UserDetails;

import java.util.Optional;

public interface UserDetailsDao {
    void create(UserDetails userDetails) throws DaoException;

    void update(UserDetails oldValue, UserDetails newValue) throws DaoException;

    Optional<UserDetails> find(UserDetails userDetails) throws DaoException;
}
