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
     * Sql {@code String} object for call stored procedure {@code UpdateUserDetailsNonPassportData}.
     * Written for the MySQL dialect.
     */
    private static final String SP_UPDATE_USER_DETAILS_NON_PASSPORT_DATA =
            "CALL UpdateUserDetailsNonPassportData(?,?,?)";

    /**
     * Update entity {@code UserDetails} in database.
     *
     * @param newValue new value of {@code UserDetails} entity,
     *                 that update {@code oldValue}.
     * @param login    {@code String} value of {@code User.login}.
     * @return {@code newValue} if it was updated or
     * {@code oldValue} if it wasn't of {@code UserDetails} entity.
     * @throws DaoException if a database access error occurs or if
     *                      {@link ConnectionPool} throws
     *                      {@link ConnectionException}.
     */
    @Override
    public Optional<UserDetails> update(UserDetails newValue, String login) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<UserDetails> optionalUserDetails = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_UPDATE_USER_DETAILS_NON_PASSPORT_DATA);
            statement.setString(1, login);
            statement.setString(2, newValue.getAddress());
            statement.setString(3, newValue.getPhone());

            resultSet = statement.executeQuery();
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
            throw new DaoException("Updating user details failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalUserDetails;
    }
}
