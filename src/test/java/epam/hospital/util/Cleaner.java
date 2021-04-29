package epam.hospital.util;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.entity.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cleaner {
    private static final String SP_DELETE_USER_WITH_USER_ROLES_AND_USER_DETAILS =
            "CALL DeleteUserWithUserRolesAndUserDetails(?)";
    private static final String SP_DELETE_USER_FROM_DEPARTMENT =
            "CALL DeleteMedicalWorkerFromDepartment(?)";
    private static final String SP_DELETE_AMBULATORY_THERAPY_WITH_DIAGNOSIS =
            "CALL DeleteAmbulatoryTherapyWithDiagnosis(?,?)";
    private static final String SP_DELETE_STATIONARY_THERAPY_WITH_DIAGNOSIS =
            "CALL DeleteStationaryTherapyWithDiagnosis(?,?)";
    private static final String SP_DELETE_PROCEDURE = "CALL DeleteProcedure(?)";
    private static final String SP_DELETE_MEDICATION = "CALL DeleteMedication(?)";

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

    // TODO: 28.04.2021 delete with procedures and medications
    public void deleteTherapyWithDiagnosis(Therapy therapy, CardType cardType) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(cardType.equals(CardType.AMBULATORY) ?
                    SP_DELETE_AMBULATORY_THERAPY_WITH_DIAGNOSIS :
                    SP_DELETE_STATIONARY_THERAPY_WITH_DIAGNOSIS);
            statement.setString(1, therapy.getPatient().getLogin());
            statement.setString(2, therapy.getDoctor().getLogin());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new DaoException("DeleteTherapyWithDiagnosis failed.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("DeleteTherapyWithDiagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
    }

    public void delete(Procedure procedure) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_DELETE_PROCEDURE);
            statement.setString(1, procedure.getName());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new DaoException("DeleteTherapyWithDiagnosis failed.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("DeleteProcedure failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
    }

    public void delete(Medication medication) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_DELETE_MEDICATION);
            statement.setString(1, medication.getName());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new DaoException("DeleteTherapyWithDiagnosis failed.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("DeleteProcedure failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
    }
}
