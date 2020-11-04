package by.epam.hospital.dao;

import by.epam.hospital.entity.UserDetails;

import java.sql.Date;
import java.util.Optional;

/**
 * A {@code UserDetailsDao} data access objects works with the database and
 * can be used for work with the {@link UserDetails} database entity.
 */

public interface UserDetailsDao {
    /**
     * Create entity {@code UserDetails} in database.
     *
     * @param userDetails an a {@code UserDetails} entity.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean create(UserDetails userDetails) throws DaoException;

    /**
     * Update entity {@code UserDetails} in database.
     *
     * @param oldValue {@code UserDetails} entity that need to be updated.
     * @param newValue new value for {@code UserDetails} entity.
     * @return {@code newValue} if it was updated or {@code oldValue} if it wasn't
     * of {@code UserDetails} entity.
     * @throws DaoException if a database access error occurs.
     */
    UserDetails update(UserDetails oldValue, UserDetails newValue) throws DaoException;

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

    /**
     * Find {@code UserDetails} entity by
     * {@code UserDetails.firstName}, {@code UserDetails.surname},
     * {@code UserDetails.lastName} and {@code UserDetails.birthday} fields.
     *
     * @param firstName {@code String} passport data value of
     *                  {@code UserDetails}.
     * @param surname   {@code String} passport data value of
     *                  {@code UserDetails}.
     * @param lastName  {@code String} passport data value of
     *                  {@code UserDetails}.
     * @param birthday  {@code Date} passport data value
     *                  {@code UserDetails}.
     * @return {@code Optional<UserDetails>} if it exist
     * or an empty {@code Optional} if it is not present.
     * @throws DaoException if a database access error occurs.
     * @see Date
     * @see Optional
     */
    Optional<UserDetails> findByRegistrationData(String firstName, String surname, String lastName, Date birthday)
            throws DaoException;
}
