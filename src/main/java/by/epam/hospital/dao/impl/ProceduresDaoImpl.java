package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.ProceduresDao;
import by.epam.hospital.entity.Procedure;
import by.epam.hospital.entity.table.ProceduresFieldName;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProceduresDaoImpl implements ProceduresDao {
    private static final String SP_CREATE_PROCEDURE = "CALL CreateProcedure(?,?)";
    private static final String SP_FIND_PROCEDURE_BY_NAME = "CALL FindProcedureByName(?)";
    private static final String SP_FIND_PROCEDURE_BY_ID = "CALL FindProcedureById(?)";
    private static final String SP_UPDATE_PROCEDURE = "CALL UpdateProcedureCost(?,?)";
    private static final String SP_UPDATE_PROCEDURE_ENABLED_STATUS = "CALL UpdateProcedureEnabledStatus(?,?)";
    private static final String SP_FIND_ALL_PROCEDURES_BY_NAME_PAGING = "CALL FindAllProceduresByNamePaging(?,?,?)";

    @Override
    public int create(Procedure procedure) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        int userId;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_PROCEDURE);

            statement.setString(1, procedure.getName());
            statement.setString(2, String.valueOf(procedure.getCost()));

            resultSet = statement.executeQuery();
            userId = resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateProcedure failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return userId;
    }

    @Override
    public Optional<Procedure> findByName(String name) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Procedure> optionalProcedure;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_PROCEDURE_BY_NAME);
            statement.setString(1, name);

            resultSet = statement.executeQuery();
            optionalProcedure = Optional.ofNullable(getProcedure(resultSet));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindProcedureByName failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalProcedure;
    }

    @Override
    public Optional<Procedure> findById(int id) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Procedure> optionalProcedure;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_PROCEDURE_BY_ID);
            statement.setString(1, String.valueOf(id));

            resultSet = statement.executeQuery();
            optionalProcedure = Optional.ofNullable(getProcedure(resultSet));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindProcedureById failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalProcedure;
    }

    @Override
    public Optional<Procedure> updateCost(int id, int cost) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Procedure> optionalProcedure;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_UPDATE_PROCEDURE);

            statement.setString(1, String.valueOf(cost));
            statement.setString(2, String.valueOf(id));

            resultSet = statement.executeQuery();
            optionalProcedure = Optional.ofNullable(getProcedure(resultSet));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("UpdateProcedureCost failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalProcedure;
    }

    @Override
    public Optional<Procedure> updateEnabledStatus(int id, boolean isEnabled) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Procedure> optionalProcedure;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_UPDATE_PROCEDURE_ENABLED_STATUS);

            statement.setString(1, String.valueOf(id));
            statement.setBoolean(2, isEnabled);

            resultSet = statement.executeQuery();
            optionalProcedure = Optional.ofNullable(getProcedure(resultSet));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("UpdateProcedureEnabledStatus failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalProcedure;
    }

    @Override
    public List<Procedure> findAllByNamePartPaging(String namePart, int from, int to) throws DaoException {
        List<Procedure> procedures = new LinkedList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_ALL_PROCEDURES_BY_NAME_PAGING);
            statement.setString(1, namePart);
            statement.setInt(2, from);
            statement.setInt(3, to);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Procedure procedure = new Procedure();
                procedure.setId(resultSet.getInt(ProceduresFieldName.ID));
                procedure.setName(resultSet.getString(ProceduresFieldName.NAME));
                procedure.setCost(resultSet.getInt(ProceduresFieldName.COST));
                procedure.setEnabled(resultSet.getBoolean(ProceduresFieldName.IS_ENABLED));
                procedures.add(procedure);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindAllProceduresByNamePaging failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return procedures;
    }

    private Procedure getProcedure(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Procedure procedure = new Procedure();
            procedure.setId(resultSet.getInt(ProceduresFieldName.ID));
            procedure.setName(resultSet.getString(ProceduresFieldName.NAME));
            procedure.setCost(resultSet.getInt(ProceduresFieldName.COST));
            procedure.setEnabled(resultSet.getBoolean(ProceduresFieldName.IS_ENABLED));
            return procedure;
        }
        return null;
    }
}
