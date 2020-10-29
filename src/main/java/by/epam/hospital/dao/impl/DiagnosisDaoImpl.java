package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DiagnosisDao;
import by.epam.hospital.dao.IcdDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.table.DiagnosesFieldName;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DiagnosisDaoImpl implements DiagnosisDao {
    private static final String SQL_CREATE_DIAGNOSIS =
            "INSERT INTO diagnoses (icd_id, doctor_id, diagnosis_date, reason) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_BY_ID =
            "SELECT id, icd_id, doctor_id, diagnosis_date, reason FROM diagnoses WHERE id = ?";
    private static final String SQL_FIND_BY_THERAPY_ID =
            "SELECT diagnoses.id, icd_id, diagnoses.doctor_id, diagnosis_date, reason FROM diagnoses " +
                    "INNER JOIN therapy_diagnoses td on diagnoses.id = td.diagnosis_id " +
                    "INNER JOIN therapy t on td.therapy_id = t.id " +
                    "WHERE t.id = ?";
    private static final String SQL_CREATE_THERAPY_DIAGNOSES =
            "INSERT INTO therapy_diagnoses (therapy_id, diagnosis_id) " +
                    "VALUES (?, ?)";

    private final IcdDao icdDao = new IcdDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void create(Diagnosis diagnosis, String patientLogin, int therapyId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        if (diagnosis.getDoctor().getId() == 0) {
            diagnosis.setDoctor(userDao.findByLogin(diagnosis.getDoctor().getLogin()).orElseThrow(DaoException::new));
        }
        if (diagnosis.getIcd().getId() == 0) {
            diagnosis.setIcd(icdDao.findByCode(diagnosis.getIcd().getCode()).orElseThrow(DaoException::new));
        }
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_CREATE_DIAGNOSIS, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, diagnosis.getIcd().getId());
            statement.setInt(2, diagnosis.getDoctor().getId());
            statement.setDate(3, diagnosis.getDiagnosisDate());
            statement.setString(4, diagnosis.getReason());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Creating diagnosis failed no rows affected.");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                diagnosis.setId(generatedKeys.getInt(1));
            } else {
                throw new DaoException("Creating diagnosis failed, no id obtained.");
            }

            addDiagnosisToTherapy(therapyId, diagnosis.getId());
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Creating diagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, generatedKeys);
        }
    }

    @Override
    public List<Diagnosis> findAllByTherapyId(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Diagnosis> diagnoses = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_THERAPY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Diagnosis diagnosis = new Diagnosis();
                setDiagnosis(diagnosis, resultSet);
                diagnoses.add(diagnosis);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find diagnoses failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return diagnoses;
    }

    @Override
    public Optional<Diagnosis> findById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Diagnosis diagnosis = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                diagnosis = new Diagnosis();
                setDiagnosis(diagnosis, resultSet);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find diagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(diagnosis);
    }

    private void setDiagnosis(Diagnosis diagnosis, ResultSet resultSet) throws SQLException, DaoException {
        diagnosis.setId(resultSet.getInt(DiagnosesFieldName.ID));
        diagnosis.setDiagnosisDate(resultSet.getDate(DiagnosesFieldName.DIAGNOSIS_DATE));
        diagnosis.setReason(resultSet.getString(DiagnosesFieldName.REASON));
        diagnosis.setIcd(icdDao.findById(resultSet.getInt(DiagnosesFieldName.ICD_ID)));
        diagnosis.setDoctor(userDao.findById(resultSet.getInt(DiagnosesFieldName.DOCTOR_ID))
                .orElseThrow(DaoException::new));
    }

    private void addDiagnosisToTherapy(int therapyId, int diagnosisId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_CREATE_THERAPY_DIAGNOSES);
            statement.setInt(1, therapyId);
            statement.setInt(2, diagnosisId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Adding diagnosis to therapy failed, no rows affected.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Adding diagnosis to therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }
}
