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
     * Sql {@code String} object for find {@code UserDetails}
     * entity by {@code userId} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_USER_ID = """
            SELECT passport_id, user_id, gender, first_name, surname, last_name, birthday, address, phone
            FROM hospital.users_details
            WHERE user_id = ?""";

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
}
