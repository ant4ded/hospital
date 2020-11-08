package epam.hospital.util;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class Cleaner {
    private static final String SQL_DELETE_USER_ROLES = """
            DELETE FROM users_roles
            WHERE user_id = ?""";
    private static final String SQL_DELETE_USER = """
            DELETE FROM users
            WHERE id = ?""";
    private static final String SQL_DELETE_USER_DETAILS = """
            DELETE FROM users_details
            WHERE passport_id = ?""";
    private static final String SQL_DELETE_DIAGNOSIS = """
            DELETE FROM diagnoses
            WHERE id = ?""";
    private static final String SQL_DELETE_THERAPY = """
            DELETE FROM therapy
            WHERE id = ?""";
    private static final String SQL_DELETE_AMBULATORY_ROW = """
            DELETE FROM ambulatory_cards
            WHERE therapy_id = ?""";
    private static final String SQL_DELETE_STATIONARY_ROW = """
            DELETE FROM stationary_cards
            WHERE therapy_id = ?""";
    private static final String SQL_DELETE_THERAPY_DIAGNOSIS_ROW = """
            DELETE FROM therapy_diagnoses
            WHERE therapy_id = ?""";

    private final UserDao userDao = new UserDaoImpl();
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();

    public void delete(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDetails userDetails = user.getUserDetails();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_DELETE_USER_ROLES);

            user = userDao.findByLogin(user.getLogin()).orElseThrow(DaoException::new);
            user.setUserDetails(userDetails);
            statement.setInt(1, user.getId());
            statement.execute();
            statement.close();

            Optional<UserDetails> optionalUserDetails = userDetailsDao.findByUserId(user.getId());
            if (optionalUserDetails.isPresent()) {
                delete(optionalUserDetails.get());
            }

            statement = connection.prepareStatement(SQL_DELETE_USER);
            statement.setInt(1, user.getId());
            statement.execute();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users table.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    public void delete(Diagnosis diagnosis) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_DELETE_DIAGNOSIS);
            statement.setInt(1, diagnosis.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new DaoException("Affected rows != 1.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on diagnoses table.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    public void delete(Therapy therapy, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType.equals(CardType.AMBULATORY) ?
                    SQL_DELETE_AMBULATORY_ROW :
                    SQL_DELETE_STATIONARY_ROW);
            statement.setInt(1, therapy.getId());
            statement.execute();
            statement.close();

            statement = connection.prepareStatement(SQL_DELETE_THERAPY_DIAGNOSIS_ROW);
            statement.setInt(1, therapy.getId());
            statement.execute();
            statement.close();

            statement = connection.prepareStatement(SQL_DELETE_THERAPY);
            statement.setInt(1, therapy.getId());
            statement.execute();

            for (int i = 0; i < therapy.getDiagnoses().size(); i++) {
                delete(therapy.getDiagnoses().get(i));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on therapy table.", e);
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

            userDetailsFromDb = userDetailsDao.findByUserId(userDetails.getUserId()).orElseThrow(DaoException::new);
            statement.setString(1, userDetailsFromDb.getPassportId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new DaoException("Affected rows != 1.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users_details table.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }
}
