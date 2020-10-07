package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsColumnName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetailsDaoImpl implements UserDetailsDao {
    private static final String SQL_CREATE = "INSERT INTO hospital.users_details " +
            "(passport_id, user_id, gender, first_name, surname, last_name, birthday, address , phone) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM hospital.users_details WHERE passport_id = ?";
    private static final String SQL_FIND = "SELECT passport_id, user_id, gender, " +
            "first_name, surname, last_name, birthday, address, phone " +
            "FROM hospital.users_details WHERE first_name = ? AND surname = ? AND last_name = ? AND birthday = ?";
    private static final String SQL_UPDATE = "UPDATE users_details " +
            "SET gender = ?, first_name = ?, surname = ?, last_name = ?, birthday = ?, address = ?, phone = ? " +
            "WHERE passport_id = ? AND user_id = ?";


    @Override
    public void create(UserDetails userDetails) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
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

            if (statement.executeUpdate() < 0) {
                throw new DaoException("Can not add row to users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not add row to users_details table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }

    @Override
    public void update(UserDetails oldValue, UserDetails newValue) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDetails userDetailsFromDb;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);
            userDetailsFromDb = find(oldValue);

            statement.setString(1, newValue.getGender().name());
            statement.setString(2, newValue.getFirstName());
            statement.setString(3, newValue.getSurname());
            statement.setString(4, newValue.getLastName());
            statement.setDate(5, newValue.getBirthday());
            statement.setString(6, newValue.getAddress());
            statement.setString(7, newValue.getPhone());
            statement.setString(8, userDetailsFromDb.getPassportId());
            statement.setInt(9, userDetailsFromDb.getUserId());

            if (statement.executeUpdate() < 0) {
                throw new DaoException("Can not update row on users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not update row on users_details table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }

    @Override
    public void delete(UserDetails userDetails) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDetails userDetailsFromDb;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            userDetailsFromDb = find(userDetails);
            statement.setString(1, userDetailsFromDb.getPassportId());

            if (statement.executeUpdate() < 0) {
                throw new DaoException("Can not delete row on users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users_details table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }

    @Override
    public UserDetails find(UserDetails userDetails) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        UserDetails userDetailsFromDb;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_FIND);

            statement.setString(1, userDetails.getFirstName());
            statement.setString(2, userDetails.getSurname());
            statement.setString(3, userDetails.getLastName());
            statement.setDate(4, userDetails.getBirthday());

            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                userDetailsFromDb = new UserDetails();
                userDetailsFromDb.setPassportId(resultSet.getString(UsersDetailsColumnName.PASSPORT_ID));
                userDetailsFromDb.setUserId(resultSet.getInt(UsersDetailsColumnName.USER_ID));
                userDetailsFromDb.setGender(UserDetails.Gender
                        .valueOf(resultSet.getString(UsersDetailsColumnName.GENDER)));
                userDetailsFromDb.setFirstName(resultSet.getString(UsersDetailsColumnName.FIRST_NAME));
                userDetailsFromDb.setSurname(resultSet.getString(UsersDetailsColumnName.SURNAME));
                userDetailsFromDb.setLastName(resultSet.getString(UsersDetailsColumnName.LAST_NAME));
                userDetailsFromDb.setBirthday(resultSet.getDate(UsersDetailsColumnName.BIRTHDAY));
                userDetailsFromDb.setAddress(resultSet.getString(UsersDetailsColumnName.ADDRESS));
                userDetailsFromDb.setPhone(resultSet.getString(UsersDetailsColumnName.PHONE));
            } else {
                throw new DaoException("Can not find row on users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users_details table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement, resultSet);
        }
        return userDetailsFromDb;
    }
}
