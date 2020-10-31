package by.epam.hospital.dao;

import by.epam.hospital.entity.UserDetails;

import java.sql.Date;
import java.util.Optional;

public interface UserDetailsDao {
    boolean create(UserDetails userDetails) throws DaoException;

    UserDetails update(UserDetails oldValue, UserDetails newValue) throws DaoException;

    Optional<UserDetails> findByUserId(int id) throws DaoException;

    Optional<UserDetails> findByRegistrationData(String firstName, String surname, String lastName, Date birthday)
            throws DaoException;
}
