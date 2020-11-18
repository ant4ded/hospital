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

/**
 * {@code DiagnosisDaoImpl} implementation of {@link DiagnosisDao}.
 * Implements all required methods for work with the {@link Diagnosis} database entity.
 * <p>
 * All methods get connection from {@code ConnectionPool}
 * and it is object of type {@code ProxyConnection}. It is a wrapper of really
 * {@code Connection}, which different only in methods {@code close}
 * and {@code reallyClose}.
 *
 * @see ConnectionPool
 * @see by.epam.hospital.connection.ProxyConnection
 * @see Connection
 */

public class DiagnosisDaoImpl implements DiagnosisDao {
    /**
     * Sql {@code String} object for call stored procedure {@code CreateAmbulatoryDiagnosis}.
     * Written for the MySQL dialect.
     */
    private static final String SP_CREATE_AMBULATORY_DIAGNOSIS =
            "CALL CreateAmbulatoryDiagnosis(?,?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure {@code CreateStationaryDiagnosis}.
     * Written for the MySQL dialect.
     */
    private static final String SP_CREATE_STATIONARY_DIAGNOSIS =
            "CALL CreateStationaryDiagnosis(?,?,?,?,?)";
    /**
     * Sql {@code String} object for creating {@code Diagnosis}
     * entity in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE_DIAGNOSIS = """
            INSERT INTO diagnoses (icd_id, doctor_id, diagnosis_date, reason) VALUES (?, ?, ?, ?)""";
    /**
     * Sql {@code String} object for find {@code Diagnosis}
     * entity by {@code Diagnosis.id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_ID = """
            SELECT id, icd_id, doctor_id, diagnosis_date, reason
            FROM diagnoses
            WHERE id = ?""";
    /**
     * Sql {@code String} object for find {@code Diagnosis}
     * entity by {@code Therapy.id} of therapy in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_THERAPY_ID = """
            SELECT diagnoses.id, icd_id, diagnoses.doctor_id, diagnosis_date, reason
            FROM diagnoses
            INNER JOIN therapy_diagnoses td on diagnoses.id = td.diagnosis_id
            INNER JOIN therapy t on td.therapy_id = t.id
            WHERE t.id = ?""";
    /**
     * Sql {@code String} object for add {@code Diagnosis}
     * entity in therapy_diagnoses table in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE_THERAPY_DIAGNOSES = """
            INSERT INTO therapy_diagnoses (therapy_id, diagnosis_id) VALUES (?, ?)""";

    /**
     * {@link IcdDao} data access object.
     */
    private final IcdDao icdDao = new IcdDaoImpl();
    /**
     * {@link UserDao} data access object.
     */
    private final UserDao userDao = new UserDaoImpl();

    /**
     * Create entity {@code Diagnosis} for ambulatory
     * {@code Therapy} entity in database.
     *
     * @param diagnosis    an a {@code Diagnosis} entity.
     * @param patientLogin {@code String} value of patient
     *                     {@code User.login} field.
     * @return auto-generated {@code Diagnosis.id} field.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see ConnectionException
     */
    @Override
    public int createAmbulatoryDiagnosis(Diagnosis diagnosis, String patientLogin) throws DaoException {
        int diagnosisId;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_AMBULATORY_DIAGNOSIS);
            statement.setString(1, patientLogin);
            statement.setString(2, diagnosis.getDoctor().getLogin());
            statement.setString(3, diagnosis.getIcd().getCode());
            statement.setDate(4, diagnosis.getDiagnosisDate());
            statement.setString(5, diagnosis.getReason());

            resultSet = statement.executeQuery();
            diagnosisId = resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateAmbulatoryDiagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return diagnosisId;
    }

    /**
     * Create entity {@code Diagnosis} for stationary
     * {@code Therapy} entity in database.
     *
     * @param diagnosis    an a {@code Diagnosis} entity.
     * @param patientLogin {@code String} value of patient
     *                     {@code User.login} field.
     * @return auto-generated {@code Diagnosis.id} field.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see ConnectionException
     */
    @Override
    public int createStationaryDiagnosis(Diagnosis diagnosis, String patientLogin) throws DaoException {
        int diagnosisId;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_STATIONARY_DIAGNOSIS);
            statement.setString(1, patientLogin);
            statement.setString(2, diagnosis.getDoctor().getLogin());
            statement.setString(3, diagnosis.getIcd().getCode());
            statement.setDate(4, diagnosis.getDiagnosisDate());
            statement.setString(5, diagnosis.getReason());

            resultSet = statement.executeQuery();
            diagnosisId = resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateStationaryDiagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return diagnosisId;
    }

    /**
     * Create entity {@code Diagnosis} in database using {@code PreparedStatement}
     * with parameter {@code Statement.RETURN_GENERATED_KEYS}.
     *
     * @param diagnosis    an a {@code Diagnosis} entity.
     * @param patientLogin {@code String} value of patient
     *                     {@code User.login} field.
     * @param therapyId    {@code int} value of {@code Therapy.id}.
     * @return auto-generated {@code Diagnosis.id} field.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     */
    @Override
    public int create(Diagnosis diagnosis, String patientLogin, int therapyId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        int diagnosisId = 0;
        if (diagnosis.getDoctor().getId() == 0) {
            diagnosis.setDoctor(userDao.findByLogin(diagnosis.getDoctor().getLogin()).orElseThrow(DaoException::new));
        }
        if (diagnosis.getIcd().getId() == 0) {
            diagnosis.setIcd(icdDao.findByCode(diagnosis.getIcd().getCode()).orElseThrow(DaoException::new));
        }
        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(SQL_CREATE_DIAGNOSIS, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, diagnosis.getIcd().getId());
            statement.setInt(2, diagnosis.getDoctor().getId());
            statement.setDate(3, diagnosis.getDiagnosisDate());
            statement.setString(4, diagnosis.getReason());

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    diagnosisId = generatedKeys.getInt(1);
                    generatedKeys.close();
                    statement.close();
                    addDiagnosisToTherapy(connection, therapyId, diagnosisId);
                }
            }
            connection.setAutoCommit(true);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Creating diagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, generatedKeys);
        }
        return diagnosisId;
    }

    /**
     * Find all {@code Diagnosis} entity by {@code Therapy.id} field
     * using {@code PreparedStatement}.
     *
     * @param id {@code int} value of {@code Therapy.id} field.
     * @return {@code List<Diagnosis>} being a
     * {@code ArrayList<Diagnosis>} object if it present
     * or an empty {@code List} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see ArrayList
     * @see List
     */
    @Override
    public List<Diagnosis> findByTherapyId(int id) throws DaoException {
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

    /**
     * Find {@code Diagnosis} entity by {@code Diagnosis.id} field
     * using {@code PreparedStatement}.
     *
     * @param id {@code int} value of {@code Therapy.id} field.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<Diagnosis> findById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<Diagnosis> optionalDiagnosis = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                Diagnosis diagnosis = new Diagnosis();
                setDiagnosis(diagnosis, resultSet);
                optionalDiagnosis = Optional.of(diagnosis);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find diagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalDiagnosis;
    }

    /**
     * Set {@code Diagnosis} fields.
     *
     * @param diagnosis empty {@code Diagnosis} entity.
     * @param resultSet {@code ResultSet} object with result
     *                  of execute sql string.
     * @throws SQLException when db send error.
     * @throws DaoException when {@code IcdDao} or {@code UserDao}
     *                      throws exception.
     * @see ResultSet
     * @see SQLException
     */
    private void setDiagnosis(Diagnosis diagnosis, ResultSet resultSet) throws SQLException, DaoException {
        diagnosis.setId(resultSet.getInt(DiagnosesFieldName.ID));
        diagnosis.setDiagnosisDate(resultSet.getDate(DiagnosesFieldName.DIAGNOSIS_DATE));
        diagnosis.setReason(resultSet.getString(DiagnosesFieldName.REASON));
        diagnosis.setIcd(icdDao.findById(resultSet.getInt(DiagnosesFieldName.ICD_ID))
                .orElseThrow(DaoException::new));
        diagnosis.setDoctor(userDao.findByIdWithUserDetails(resultSet.getInt(DiagnosesFieldName.DOCTOR_ID))
                .orElseThrow(DaoException::new));
    }

    /**
     * Add {@code Diagnosis} to {@code Therapy} table using {@code PreparedStatement}.
     *
     * @param connection  {@code Connection} object for executing sql string.
     * @param therapyId   {@code int} value of {@code Therapy.id} field.
     * @param diagnosisId {@code int} value of {@code Diagnosis.id} field.
     * @throws SQLException throws when adding {@code Diagnosis} to
     *                      {@code Therapy} table throws
     *                      {@code SQLException}.
     * @see PreparedStatement
     * @see Connection
     * @see SQLException
     */
    private void addDiagnosisToTherapy(Connection connection, int therapyId, int diagnosisId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_CREATE_THERAPY_DIAGNOSES);
        statement.setInt(1, therapyId);
        statement.setInt(2, diagnosisId);
        statement.execute();
        statement.close();
    }
}
