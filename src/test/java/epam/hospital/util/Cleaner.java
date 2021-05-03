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
    private static final String SP_DELETE_PROCEDURE = "CALL DeleteProcedure(?)";
    private static final String SP_DELETE_MEDICAMENT = "CALL DeleteMedicament(?)";
    private static final String SP_DELETE_THERAPY_WITH_ALL_FK = "CALL DeleteTherapyWithAllFK(?,?,?)";

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

    public void deleteTherapyWithDiagnosis(Therapy therapy, CardType cardType) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_DELETE_THERAPY_WITH_ALL_FK);
            statement.setString(1, therapy.getPatient().getLogin());
            statement.setString(2, therapy.getDoctor().getLogin());
            statement.setString(3, cardType.name());
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

    public void delete(Medicament medicament) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_DELETE_MEDICAMENT);
            statement.setString(1, medicament.getName());
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
