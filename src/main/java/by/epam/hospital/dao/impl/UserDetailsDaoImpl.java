package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;

import java.sql.*;
import java.util.Optional;

public class UserDetailsDaoImpl implements UserDetailsDao {
    private static final String SQL_CREATE =
            "INSERT INTO hospital.users_details " +
            "(passport_id, user_id, gender, first_name, surname, last_name, birthday, address , phone) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_USER_ID =
            "SELECT passport_id, user_id, gender, first_name, surname, last_name, birthday, address, phone " +
            "FROM hospital.users_details WHERE user_id = ?";
    private static final String SQL_FIND_BY_REGISTRATION_DATA =
            "SELECT passport_id, user_id, gender, address, phone FROM users_details " +
            "WHERE first_name = ? AND surname = ? AND last_name = ? AND birthday = ?";
    private static final String SQL_UPDATE =
            "UPDATE users_details " +
            "SET gender = ?, first_name = ?, surname = ?, last_name = ?, birthday = ?, address = ?, phone = ? " +
            "WHERE passport_id = ? AND user_id = ?";

    @Override
    public void create(UserDetails userDetails) throws DaoException {
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

            if (statement.executeUpdate() < 0) {
                throw new DaoException("Can not add row to users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not add row to users_details table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    @Override
    public void update(UserDetails oldValue, UserDetails newValue) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDetails userDetailsFromDb;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);
            userDetailsFromDb = find(oldValue.getUserId()).orElseThrow(DaoException::new);
            newValue.setUserId(userDetailsFromDb.getUserId());

            statement.setString(1, newValue.getGender().name());
            statement.setString(2, newValue.getFirstName());
            statement.setString(3, newValue.getSurname());
            statement.setString(4, newValue.getLastName());
            statement.setDate(5, newValue.getBirthday());
            statement.setString(6, newValue.getAddress());
            statement.setString(7, newValue.getPhone());
            statement.setString(8, userDetailsFromDb.getPassportId());
            statement.setInt(9, newValue.getUserId());

            if (statement.executeUpdate() < 0) {
                throw new DaoException("Can not update row on users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not update row on users_details table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    @Override
    public Optional<UserDetails> find(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        UserDetails userDetailsFromDb = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_USER_ID);

            statement.setInt(1, id);

            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                userDetailsFromDb = new UserDetails();
                userDetailsFromDb.setPassportId(resultSet.getString(UsersDetailsFieldName.PASSPORT_ID));
                userDetailsFromDb.setUserId(resultSet.getInt(UsersDetailsFieldName.USER_ID));
                userDetailsFromDb.setGender(UserDetails.Gender
                        .valueOf(resultSet.getString(UsersDetailsFieldName.GENDER)));
                userDetailsFromDb.setFirstName(resultSet.getString(UsersDetailsFieldName.FIRST_NAME));
                userDetailsFromDb.setSurname(resultSet.getString(UsersDetailsFieldName.SURNAME));
                userDetailsFromDb.setLastName(resultSet.getString(UsersDetailsFieldName.LAST_NAME));
                userDetailsFromDb.setBirthday(resultSet.getDate(UsersDetailsFieldName.BIRTHDAY));
                userDetailsFromDb.setAddress(resultSet.getString(UsersDetailsFieldName.ADDRESS));
                userDetailsFromDb.setPhone(resultSet.getString(UsersDetailsFieldName.PHONE));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users_details table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(userDetailsFromDb);
    }

    @Override
    public Optional<UserDetails> findByRegistrationData
            (String firstName, String surname, String lastName, Date birthday) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        UserDetails userDetailsFromDb = null;
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
                userDetailsFromDb = new UserDetails();
                userDetailsFromDb.setPassportId(resultSet.getString(UsersDetailsFieldName.PASSPORT_ID));
                userDetailsFromDb.setUserId(resultSet.getInt(UsersDetailsFieldName.USER_ID));
                userDetailsFromDb.setGender(UserDetails.Gender
                        .valueOf(resultSet.getString(UsersDetailsFieldName.GENDER)));

                userDetailsFromDb.setFirstName(firstName);
                userDetailsFromDb.setSurname(surname);
                userDetailsFromDb.setLastName(lastName);
                userDetailsFromDb.setBirthday(birthday);

                userDetailsFromDb.setAddress(resultSet.getString(UsersDetailsFieldName.ADDRESS));
                userDetailsFromDb.setPhone(resultSet.getString(UsersDetailsFieldName.PHONE));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users_details table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(userDetailsFromDb);
    }
}
