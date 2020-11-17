package epam.hospital.util;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.entity.*;

import java.sql.*;

public class Cleaner {
    private static final String SP_DELETE_USER_WITH_USER_ROLES_AND_USER_DETAILS =
            "CALL DeleteUserWithUserRolesAndUserDetails(?)";
    private static final String SP_DELETE_USER_FROM_DEPARTMENT =
            "CALL DeleteMedicalWorkerFromDepartment(?)";
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

    public void deleteUserFromDepartment(User user) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_DELETE_USER_FROM_DEPARTMENT);
            statement.setString(1, user.getLogin());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int affectedRows = resultSet.getInt(1);
                if (affectedRows == 0) {
                    throw new DaoException("Delete failed.");
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users table.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
    }

    public void delete(User user) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_DELETE_USER_WITH_USER_ROLES_AND_USER_DETAILS);
            statement.setString(1, user.getLogin());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int affectedRows = resultSet.getInt(1);
                if (affectedRows == 0) {
                    throw new DaoException("Delete failed.");
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users table.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
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
}
