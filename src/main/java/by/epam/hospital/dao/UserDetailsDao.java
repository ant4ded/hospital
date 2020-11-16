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
     * @param newValue new value for {@code UserDetails} entity.
     * @param userId   {@code int} value for {@code User.id}.
     * @return {@code newValue} if it was updated or {@code oldValue} if it wasn't
     * of {@code UserDetails} entity.
     * @throws DaoException if a database access error occurs.
     */
    Optional<UserDetails> update(UserDetails newValue, int userId) throws DaoException;

    /**
     * Find {@code UserDetails} entity by {@code UserDetails.userId} field.
     *
     * @param id {@code int} value of {@code User.id} field.
     * @return {@code Optional<UserDetails>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<UserDetails> findByUserId(int id) throws DaoException;
}
