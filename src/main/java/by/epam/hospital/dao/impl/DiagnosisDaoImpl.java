package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DiagnosisDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.*;
import by.epam.hospital.entity.table.*;

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
    private static final String SP_CREATE_DIAGNOSIS = "CALL CreateDiagnosis(?,?,?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindDiagnosesByTherapyId}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_DIAGNOSES_BY_THERAPY_ID = "CALL FindDiagnosesByTherapyId(?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindDiagnosisWithIcdById}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_DIAGNOSIS_WITH_ICD_BY_ID = "CALL FindDiagnosisWithIcdById(?)";
    private static final String SP_ASSIGN_PROCEDURE_TO_DIAGNOSIS = "CALL AssignProcedureToDiagnosis(?,?,?,?,?,?)";
    private static final String SP_ASSIGN_MEDICAMENT_TO_DIAGNOSIS = "CALL AssignMedicamentToDiagnosis(?,?,?,?,?,?)";
    private static final String SP_FIND_ALL_ASSIGNMENT_PROCEDURES = "CALL FindAllAssignmentProceduresToDiagnosis(?)";
    private static final String SP_FIND_ALL_ASSIGNMENT_MEDICATIONS = "CALL FindAllAssignmentMedicationsToDiagnosis(?)";

    /**
     * {@link UserDao} data access object.
     */
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public int createDiagnosis(Diagnosis diagnosis, String patientLogin, CardType cardType) throws DaoException {
        int diagnosisId;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_DIAGNOSIS);
            statement.setString(1, patientLogin);
            statement.setString(2, diagnosis.getDoctor().getLogin());
            statement.setString(3, diagnosis.getIcd().getCode());
            statement.setDate(4, diagnosis.getDiagnosisDate());
            statement.setString(5, diagnosis.getReason());
            statement.setString(6, cardType.name());

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
     * Find all {@code Diagnosis} entity by {@code Therapy.id} field.
     *
     * @param id {@code int} value of {@code Therapy.id} field.
     * @return {@code List<Diagnosis>} being a
     * {@code ArrayList<Diagnosis>} object if it present
     * or an empty {@code List} if it isn't.
     * @throws DaoException if a database access error occurs or if
     *                      {@link ConnectionPool} throws
     *                      {@link ConnectionException}.
     * @see ArrayList
     * @see List
     */
    @Override
    public List<Diagnosis> findByTherapyId(int id) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        List<Diagnosis> diagnoses = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_DIAGNOSES_BY_THERAPY_ID);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
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
     * Find {@code Diagnosis} entity by {@code Diagnosis.id} field.
     *
     * @param id {@code int} value of {@code Therapy.id} field.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs or if
     *                      {@link ConnectionPool} throws
     *                      {@link ConnectionException}.
     * @see Optional
     */
    @Override
    public Optional<Diagnosis> findById(int id) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Diagnosis> optionalDiagnosis = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_DIAGNOSIS_WITH_ICD_BY_ID);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
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

    @Override
    public boolean assignProcedureToLastDiagnosis(ProcedureAssignment assignment, String doctorLogin,
                                                  String patientLogin, CardType cardType) throws DaoException {
        boolean result;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_ASSIGN_PROCEDURE_TO_DIAGNOSIS);

            statement.setString(1, assignment.getProcedure().getName());
            statement.setTimestamp(2, Timestamp.valueOf(assignment.getTime()));
            statement.setString(3, assignment.getDescription());
            statement.setString(4, doctorLogin);
            statement.setString(5, patientLogin);
            statement.setString(6, cardType.name());

            resultSet = statement.executeQuery();
            result = resultSet.next() && resultSet.getInt(1) != 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateProcedure failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    @Override
    public boolean assignMedicamentToLastDiagnosis(MedicamentAssignment assignment, String doctorLogin,
                                                   String patientLogin, CardType cardType) throws DaoException {
        boolean result;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_ASSIGN_MEDICAMENT_TO_DIAGNOSIS);

            statement.setString(1, assignment.getMedicament().getName());
            statement.setTimestamp(2, Timestamp.valueOf(assignment.getTime()));
            statement.setString(3, assignment.getDescription());
            statement.setString(4, doctorLogin);
            statement.setString(5, patientLogin);
            statement.setString(6, cardType.name());

            resultSet = statement.executeQuery();
            result = resultSet.next() && resultSet.getInt(1) != 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateProcedure failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    @Override
    public List<ProcedureAssignment> findAllAssignmentProcedures(int diagnosisId) throws DaoException {
        List<ProcedureAssignment> assignments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_ALL_ASSIGNMENT_PROCEDURES);
            statement.setInt(1, diagnosisId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Procedure procedure = new Procedure();
                ProcedureAssignment assignment = new ProcedureAssignment();
                procedure.setId(resultSet.getInt(ProceduresFieldName.ID));
                procedure.setName(resultSet.getString(ProceduresFieldName.NAME));
                procedure.setCost(resultSet.getInt(ProceduresFieldName.COST));
                procedure.setEnabled(resultSet.getBoolean(ProceduresFieldName.IS_ENABLED));
                assignment.setProcedure(procedure);
                assignment.setDescription(resultSet.getString(ProceduresAssignmentFieldName.DESCRIPTION));
                assignment.setTime(resultSet.getTimestamp(ProceduresAssignmentFieldName.DATETIME).toLocalDateTime());
                assignments.add(assignment);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindOpenDoctorTherapies failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return assignments;
    }

    @Override
    public List<MedicamentAssignment> findAllAssignmentMedications(int diagnosisId) throws DaoException {
        List<MedicamentAssignment> assignments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_ALL_ASSIGNMENT_MEDICATIONS);
            statement.setInt(1, diagnosisId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Medicament medicament = new Medicament();
                MedicamentAssignment assignment = new MedicamentAssignment();
                medicament.setId(resultSet.getInt(MedicationsFieldName.ID));
                medicament.setName(resultSet.getString(MedicationsFieldName.NAME));
                medicament.setEnabled(resultSet.getBoolean(MedicationsFieldName.IS_ENABLED));
                assignment.setMedicament(medicament);
                assignment.setDescription(resultSet.getString(MedicationsAssignmentFieldName.DESCRIPTION));
                assignment.setTime(resultSet.getTimestamp(MedicationsAssignmentFieldName.DATETIME).toLocalDateTime());
                assignments.add(assignment);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindOpenDoctorTherapies failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return assignments;
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
        Icd icd = new Icd();
        diagnosis.setId(resultSet.getInt(DiagnosesFieldName.ID));
        diagnosis.setDiagnosisDate(resultSet.getDate(DiagnosesFieldName.DIAGNOSIS_DATE));
        diagnosis.setReason(resultSet.getString(DiagnosesFieldName.REASON));
        diagnosis.setDoctor(userDao.findByIdWithUserDetails(resultSet.getInt(DiagnosesFieldName.DOCTOR_ID))
                .orElseThrow(DaoException::new));
        icd.setCode(resultSet.getString(IcdFieldName.CODE));
        icd.setTitle(resultSet.getString(IcdFieldName.TITLE));
        diagnosis.setIcd(icd);
    }
}
