package epam.hospital.util;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class Cleaner {
    private static final String SQL_DELETE_USER_ROLES =
            "DELETE FROM users_roles WHERE user_id = ?";
    private static final String SQL_DELETE_USER =
            "DELETE FROM users WHERE id = ?";
    private static final String SQL_DELETE_USER_DETAILS =
            "DELETE FROM users_details WHERE passport_id = ?";

    private final UserDao userDao = new UserDaoImpl();
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();

    public void delete(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDetails userDetails = user.getUserDetails();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_DELETE_USER_ROLES);

            user = userDao.find(user.getLogin()).orElseThrow(DaoException::new);
            user.setUserDetails(userDetails);
            statement.setInt(1, user.getId());
            statement.execute();
            statement.close();

            Optional<UserDetails> optionalUserDetails = userDetailsDao.find(user.getId());
            if (optionalUserDetails.isPresent()) {
                delete(optionalUserDetails.get());
            }

            statement = connection.prepareStatement(SQL_DELETE_USER);
            statement.setInt(1, user.getId());
            statement.execute();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    private void delete(UserDetails userDetails) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDetails userDetailsFromDb;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_DELETE_USER_DETAILS);

            userDetailsFromDb = userDetailsDao.find(userDetails.getUserId()).orElseThrow(DaoException::new);
            statement.setString(1, userDetailsFromDb.getPassportId());

            if (statement.executeUpdate() < 0) {
                throw new DaoException("Can not delete row on users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users_details table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }
}
