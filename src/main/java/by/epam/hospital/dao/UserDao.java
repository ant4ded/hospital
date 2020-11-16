package by.epam.hospital.dao;

import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;

import java.util.Optional;

/**
 * A {@code UserDao} data access objects works with the database and
 * can be used for work with the {@link User} database entity.
 */

public interface UserDao {
    /**
     * Create entity {@link User} with entity {@link by.epam.hospital.entity.UserDetails}
     * and with {@link Role#CLIENT} in database.
     *
     * @param user an a {@code User} entity.
     * @return auto-generated {@code User.id} field or zero if not success.
     * @throws DaoException if a database access error occurs.
     */
    int createClientWithUserDetails(User user) throws DaoException;

    /**
     * Update entity {@code User} in database.
     *
     * @param login    {@code String} value of {@code User.login}
     *                 for find entity that need to be updated.
     * @param newValue new value for {@code User} entity.
     * @return {@code newValue} if it was updated or
     * {@code oldValue} if it wasn't of {@code User} entity.
     * @throws DaoException if a database access error occurs.
     */
    Optional<User> updateLoginAndPassword(String login, User newValue) throws DaoException;

    /**
     * Find {@code User} entity by {@code User.login} field.
     *
     * @param login {@code String} value of {@code User.login} field.
     * @return {@code Optional<User>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<User> findByLogin(String login) throws DaoException;

    /**
     * Find {@code User} entity by {@code User.login} field
     * with {@link by.epam.hospital.entity.UserDetails}.
     *
     * @param login {@code String} value of {@code User.login} field.
     * @return {@code Optional<User>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<User> findByLoginWithUserDetails(String login) throws DaoException;

    /**
     * Find {@code User} entity by {@code User.id} field.
     *
     * @param id {@code int} value of {@code User.id} field.
     * @return {@code Optional<User>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<User> findById(int id) throws DaoException;

    /**
     * Add {@link Role} to entity {@link User}
     *
     * @param login {@code String} value of {@code User.login} field.
     * @param role  enumeration element of {@link Role}.
     * @return {@code true} if it was successful and {@code false} if it wasn't.
     * @throws DaoException if a database access error occurs.
     */
    boolean addUserRole(String login, Role role) throws DaoException;

    /**
     * Delete {@link Role} from entity {@link User}
     *
     * @param login {@code String} value of {@code User.login} field.
     * @param role  enumeration element of {@link Role}.
     * @return {@code true} if it was successful and {@code false} if it wasn't.
     * @throws DaoException if a database access error occurs.
     */
    boolean deleteUserRole(String login, Role role) throws DaoException;
}
