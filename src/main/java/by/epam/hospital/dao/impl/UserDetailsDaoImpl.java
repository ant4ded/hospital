package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;

import java.sql.*;
import java.util.Optional;

/**
 * {@code UserDetailsDaoImpl} implementation of {@link UserDetailsDao}.
 * Implements all required methods for work with the {@link UserDetails} database entity.
 * <p>
 * All methods get connection from {@code ConnectionPool}
 * and it is object of type {@code ProxyConnection}. It is a wrapper of really
 * {@code Connection}, which different only in methods {@code close}
 * and {@code reallyClose}.
 *
 * @see ConnectionPool
 * @see by.epam.hospital.connection.ProxyConnection
 * @see Connection
 */

public class UserDetailsDaoImpl implements UserDetailsDao {
    /**
     * Sql {@code String} object for creating
     * {@code UserDetails} entity in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE = """
            INSERT INTO hospital.users_details (passport_id, user_id, gender, first_name, surname, last_name,
                birthday, address , phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)""";
    /**
     * Sql {@code String} object for find {@code UserDetails}
     * entity by {@code userId} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_USER_ID = """
            SELECT passport_id, user_id, gender, first_name, surname, last_name, birthday, address, phone
            FROM hospital.users_details
            WHERE user_id = ?""";
    /**
     * Sql {@code String} object for find {@code UserDetails} entity by {@code firstName},
     * {@code surname}, {@code lastName} and {@code birthday} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_REGISTRATION_DATA = """
            SELECT passport_id, user_id, gender, address, phone
            FROM users_details
            WHERE first_name = ? AND surname = ? AND last_name = ? AND birthday = ?""";
    /**
     * Sql {@code String} object for update {@code UserDetails}
     * entity by {@code userId} field.
     * Written for the MySQL dialect.
     */
    private static final String SQL_UPDATE = """
            UPDATE users_details
            SET address = ?, phone = ?
            WHERE user_id = ?""";

    /**
     * Create entity {@code UserDetails} in database
     * using {@code PreparedStatement}.
     *
     * @param userDetails an a {@code UserDetails} entity,
     *                    that must have nonzero {@code userId}.
     * @return {@code true} if affected one row or {@code false} if not.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     */
    @Override
    public boolean create(UserDetails userDetails) throws DaoException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_CREATE);
            statement.setString(1, userDetails.getPassportId());
            statement.setInt(2, userDetails.getUserId());
            statement.setString(3, userDetails.getGender().name());
            statement.setString(4, userDetails.getFirstName());
            statement.setString(5, userDetails.getSurname());
            statement.setString(6, userDetails.getLastName());
            statement.setDate(7, userDetails.getBirthday());
            statement.setString(8, userDetails.getAddress());
            statement.setString(9, userDetails.getPhone());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 1) {
                result = true;
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Creating user details failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return result;
    }

    /**
     * Update entity {@code UserDetails} in database
     * using {@code PreparedStatement}.
     *
     * @param newValue new value for {@code UserDetails} entity,
     *                 that update {@code oldValue}.
     * @param userId   {@code int} value for {@code User.id}.
     * @return {@code newValue} if it was updated or
     * {@code oldValue} if it wasn't of {@code UserDetails} entity.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     */
    @Override
    public Optional<UserDetails> update(UserDetails newValue, int userId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        Optional<UserDetails> optionalUserDetails = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, newValue.getAddress());
            statement.setString(2, newValue.getPhone());
            statement.setInt(3, userId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                optionalUserDetails = findByUserId(userId);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating user details failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return optionalUserDetails;
    }

    /**
     * Find {@code UserDetails} entity by {@code UserDetails.userId} field
     * using {@code PreparedStatement}.
     *
     * @param id {@code int} value of {@code User.id} field.
     * @return {@code Optional<UserDetails>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<UserDetails> findByUserId(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<UserDetails> optionalUserDetails = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_USER_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                UserDetails userDetails = new UserDetails();
                userDetails.setPassportId(resultSet.getString(UsersDetailsFieldName.PASSPORT_ID));
                userDetails.setUserId(resultSet.getInt(UsersDetailsFieldName.USER_ID));
                userDetails.setGender(UserDetails.Gender
                        .valueOf(resultSet.getString(UsersDetailsFieldName.GENDER)));
                userDetails.setFirstName(resultSet.getString(UsersDetailsFieldName.FIRST_NAME));
                userDetails.setSurname(resultSet.getString(UsersDetailsFieldName.SURNAME));
                userDetails.setLastName(resultSet.getString(UsersDetailsFieldName.LAST_NAME));
                userDetails.setBirthday(resultSet.getDate(UsersDetailsFieldName.BIRTHDAY));
                userDetails.setAddress(resultSet.getString(UsersDetailsFieldName.ADDRESS));
                userDetails.setPhone(resultSet.getString(UsersDetailsFieldName.PHONE));
                optionalUserDetails = Optional.of(userDetails);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find user details failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalUserDetails;
    }

    /**
     * Find {@code UserDetails} entity by
     * {@code UserDetails.firstName}, {@code UserDetails.surname},
     * {@code UserDetails.lastName} and {@code UserDetails.birthday} fields
     * using {@code PreparedStatement}.
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
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<UserDetails> findByRegistrationData
    (String firstName, String surname, String lastName, Date birthday) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<UserDetails> optionalUserDetails = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_REGISTRATION_DATA);
            statement.setString(1, firstName);
            statement.setString(2, surname);
            statement.setString(3, lastName);
            statement.setDate(4, birthday);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                UserDetails userDetails = new UserDetails();
                userDetails.setPassportId(resultSet.getString(UsersDetailsFieldName.PASSPORT_ID));
                userDetails.setUserId(resultSet.getInt(UsersDetailsFieldName.USER_ID));
                userDetails.setGender(UserDetails.Gender
                        .valueOf(resultSet.getString(UsersDetailsFieldName.GENDER)));
                userDetails.setFirstName(firstName);
                userDetails.setSurname(surname);
                userDetails.setLastName(lastName);
                userDetails.setBirthday(birthday);
                userDetails.setAddress(resultSet.getString(UsersDetailsFieldName.ADDRESS));
                userDetails.setPhone(resultSet.getString(UsersDetailsFieldName.PHONE));
                optionalUserDetails = Optional.of(userDetails);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find user details failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalUserDetails;
    }
}
