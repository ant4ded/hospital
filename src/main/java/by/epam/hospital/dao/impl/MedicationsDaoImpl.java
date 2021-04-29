package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.MedicationsDao;
import by.epam.hospital.entity.Medication;
import by.epam.hospital.entity.table.MedicationsFieldName;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MedicationsDaoImpl implements MedicationsDao {
    private static final String SP_CREATE_MEDICATION = "CALL CreateMedication(?)";
    private static final String SP_FIND_MEDICATION_BY_ID = "CALL FindMedicationById(?)";
    private static final String SP_FIND_MEDICATION_BY_NAME = "CALL FindMedicationByName(?)";
    private static final String SP_UPDATE_MEDICATION_ENABLED_STATUS = "CALL UpdateMedicationEnabledStatus(?,?)";
    private static final String SP_FIND_ALL_MEDICATIONS_BY_NAME_PAGING = "CALL FindAllMedicationsByNamePaging(?,?,?)";

    @Override
    public int create(Medication medication) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        int userId;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_MEDICATION);

            statement.setString(1, medication.getName());

            resultSet = statement.executeQuery();
            userId = resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateMedication failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return userId;
    }

    @Override
    public Optional<Medication> findById(int id) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Medication> optionalMedication;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_MEDICATION_BY_ID);
            statement.setString(1, String.valueOf(id));

            resultSet = statement.executeQuery();
            optionalMedication = Optional.ofNullable(getMedication(resultSet));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindMedicationById failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalMedication;
    }

    @Override
    public Optional<Medication> findByName(String name) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Medication> optionalMedication;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_MEDICATION_BY_NAME);
            statement.setString(1, name);

            resultSet = statement.executeQuery();
            optionalMedication = Optional.ofNullable(getMedication(resultSet));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindMedicationByName failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalMedication;
    }

    @Override
    public Optional<Medication> updateEnabledStatus(int id, boolean isEnabled) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Medication> optionalMedication;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_UPDATE_MEDICATION_ENABLED_STATUS);

            statement.setString(1, String.valueOf(id));
            statement.setBoolean(2, isEnabled);

            resultSet = statement.executeQuery();
            optionalMedication = Optional.ofNullable(getMedication(resultSet));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("UpdateMedicationEnabledStatus failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalMedication;
    }

    @Override
    public List<Medication> findAllByNamePartPaging(String namePart, int from, int to) throws DaoException {
        List<Medication> procedures = new LinkedList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_ALL_MEDICATIONS_BY_NAME_PAGING);
            statement.setString(1, namePart);
            statement.setInt(2, from);
            statement.setInt(3, to);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Medication medication = new Medication();
                medication.setId(resultSet.getInt(MedicationsFieldName.ID));
                medication.setName(resultSet.getString(MedicationsFieldName.NAME));
                medication.setEnabled(resultSet.getBoolean(MedicationsFieldName.IS_ENABLED));
                procedures.add(medication);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindAllMedicationsByNamePaging failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return procedures;
    }

    private Medication getMedication(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Medication medication = new Medication();
            medication.setId(resultSet.getInt(MedicationsFieldName.ID));
            medication.setName(resultSet.getString(MedicationsFieldName.NAME));
            medication.setEnabled(resultSet.getBoolean(MedicationsFieldName.IS_ENABLED));
            return medication;
        }
        return null;
    }
}
