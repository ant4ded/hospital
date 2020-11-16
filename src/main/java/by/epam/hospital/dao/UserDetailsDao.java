package by.epam.hospital.dao;

import by.epam.hospital.entity.UserDetails;

import java.util.Optional;

/**
 * A {@code UserDetailsDao} data access objects works with the database and
 * can be used for work with the {@link UserDetails} database entity.
 */

public interface UserDetailsDao {
    /**
     * Update entity {@code UserDetails} in database.
     *
     * @param newValue new value of {@code UserDetails} entity.
     * @param login   {@code String} value of {@code User.login}.
     * @return {@code newValue} if it was updated or {@code oldValue} if it wasn't
     * of {@code UserDetails} entity.
     * @throws DaoException if a database access error occurs.
     */
    Optional<UserDetails> update(UserDetails newValue, String login) throws DaoException;
}
