package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DiagnosisDao;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.table.TherapyFieldName;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@code TherapyDaoImpl} implementation of {@link TherapyDao}.
 * Implements all required methods for work with the {@link Therapy} database entity.
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

public class TherapyDaoImpl implements TherapyDao {
    /**
     * Sql {@code String} object for call stored procedure {@code CreateAmbulatoryTherapyWithDiagnosis}.
     * Written for the MySQL dialect.
     */
    private static final String SP_CREATE_AMBULATORY_THERAPY_WITH_DIAGNOSIS =
            "CALL CreateAmbulatoryTherapyWithDiagnosis(?,?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure {@code CreateStationaryTherapyWithDiagnosis}.
     * Written for the MySQL dialect.
     */
    private static final String SP_CREATE_STATIONARY_THERAPY_WITH_DIAGNOSIS =
            "CALL CreateStationaryTherapyWithDiagnosis(?,?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure {@code FindCurrentPatientAmbulatoryTherapy}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_CURRENT_PATIENT_AMBULATORY_THERAPY =
            "CALL FindCurrentPatientAmbulatoryTherapy(?,?)";
    /**
     * Sql {@code String} object for call stored procedure {@code FindCurrentPatientStationaryTherapy}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_CURRENT_PATIENT_STATIONARY_THERAPY =
            "CALL FindCurrentPatientStationaryTherapy(?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code SetAmbulatoryTherapyEndDateByDoctorAndPatient}.
     * Written for the MySQL dialect.
     */
    private static final String SP_SET_AMBULATORY_THERAPY_END_DATE_BY_DOCTOR_AND_PATIENT =
            "CALL SetAmbulatoryTherapyEndDateByDoctorAndPatient(?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code SetStationaryTherapyEndDateByDoctorAndPatient}.
     * Written for the MySQL dialect.
     */
    private static final String SP_SET_STATIONARY_THERAPY_END_DATE_BY_DOCTOR_AND_PATIENT =
            "CALL SetStationaryTherapyEndDateByDoctorAndPatient(?,?,?)";
    /**
     * Sql {@code String} object for set {@code Therapy.finalDiagnosis}
     * field to entity {@code Therapy} in
     * ambulatory card table by doctor {@code User.login}
     * and patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_SET_FINAL_DIAGNOSIS_AMBULATORY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN = """
            UPDATE therapy
            SET final_diagnosis_id = (
                SELECT fdid FROM (
                  	SELECT d.id fdid
                    FROM therapy
                    INNER JOIN ambulatory_cards ac ON ac.therapy_id = therapy.id
                    INNER JOIN users patients ON patients.id = ac.patient_id
                    INNER JOIN users doctors ON doctors.id = therapy.doctor_id
                    INNER JOIN therapy_diagnoses td ON td.therapy_id = therapy.id
                    INNER JOIN diagnoses d ON d.id = td.diagnosis_id
                    WHERE doctors.login = ? AND patients.login = ?
                    ORDER BY d.id DESC
                    LIMIT 1)
                as c)
            WHERE doctor_id = (
                SELECT did FROM (
                  	SELECT doctors.id did
                  	FROM ambulatory_cards
                  	INNER JOIN therapy t ON ambulatory_cards.therapy_id = t.id
                  	INNER JOIN users doctors ON t.doctor_id = doctors.id
                  	INNER JOIN users patients ON ambulatory_cards.patient_id = patients.id
                  	WHERE doctors.login = ? AND patients.login = ?)
                as c)""";
    /**
     * Sql {@code String} object for set {@code Therapy.finalDiagnosis}
     * field to entity {@code Therapy} in
     * stationary card table by doctor {@code User.login}
     * and patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_SET_FINAL_DIAGNOSIS_STATIONARY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN = """
            UPDATE therapy
            SET final_diagnosis_id = (
                SELECT fdid FROM (
                  	SELECT d.id fdid
                    FROM therapy
                    INNER JOIN stationary_cards sc ON sc.therapy_id = therapy.id
                    INNER JOIN users patients ON patients.id = sc.patient_id
                    INNER JOIN users doctors ON doctors.id = therapy.doctor_id
                    INNER JOIN therapy_diagnoses td ON td.therapy_id = therapy.id
                    INNER JOIN diagnoses d ON d.id = td.diagnosis_id
                    WHERE doctors.login = ? AND patients.login = ?
                    ORDER BY d.id DESC
                    LIMIT 1)
                as c)
            WHERE doctor_id = (
                SELECT did FROM (
                  	SELECT doctors.id did
                  	FROM stationary_cards
                  	INNER JOIN therapy t ON stationary_cards.therapy_id = t.id
                  	INNER JOIN users doctors ON t.doctor_id = doctors.id
                  	INNER JOIN users patients ON stationary_cards.patient_id = patients.id
                  	WHERE doctors.login = ? AND patients.login = ?)
                as c)""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity in
     * ambulatory card table by patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_AMBULATORY_THERAPY_BY_PATIENT_LOGIN = """
            SELECT therapy.id, doctor_id, end_therapy, final_diagnosis_id
            FROM therapy
            INNER JOIN ambulatory_cards ac on therapy.id = ac.therapy_id
            INNER JOIN users patients on ac.patient_id = patients.id
            WHERE patient_id = (
                SELECT patients.id 
                WHERE patients.login = ?)""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity in
     * stationary card table by patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_STATIONARY_THERAPY_BY_PATIENT_LOGIN = """
            SELECT t.id, doctor_id, end_therapy, final_diagnosis_id
             FROM stationary_cards
             INNER JOIN therapy t on stationary_cards.therapy_id = t.id
             INNER JOIN users doctors on t.doctor_id = doctors.id
             WHERE doctor_id = (
                 SELECT doctors.id 
                 WHERE doctors.login = ?)
                 AND end_therapy IS NULL""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity
     * where {@code Therapy.endTherapy} not present in
     * ambulatory card table by doctor {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_OPEN_AMBULATORY_THERAPY_BY_DOCTOR_LOGIN = """
            SELECT t.id, doctor_id, end_therapy, final_diagnosis_id
            FROM ambulatory_cards
            INNER JOIN therapy t on ambulatory_cards.therapy_id = t.id
            INNER JOIN users doctors on t.doctor_id = doctors.id
            WHERE doctor_id = (
                SELECT doctors.id
                WHERE doctors.login = ?)
                AND end_therapy IS NULL""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity
     * where {@code Therapy.endTherapy} not present in
     * stationary card table by doctor {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_OPEN_STATIONARY_THERAPY_BY_DOCTOR_LOGIN = """
            SELECT therapy.id, doctor_id, end_therapy, final_diagnosis_id
            FROM therapy
            INNER JOIN users doctors on therapy.doctor_id = doctors.id
            INNER JOIN stationary_cards sc on therapy.id = sc.therapy_id
            INNER JOIN users patients on sc.patient_id = patients.id
            WHERE patient_id = (
                SELECT patients.id
                WHERE patients.login = ?)""";

    /**
     * {@code UserDao} data access object.
     */
    private final UserDao userDao = new UserDaoImpl();
    /**
     * {@code DiagnosisDao} data access object.
     */
    private final DiagnosisDao diagnosisDao = new DiagnosisDaoImpl();

    /**
     * Create ambulatory entity {@code Therapy} and
     * entity {@code Diagnosis} for this ambulatory {@code Therapy}.
     *
     * @param therapy   entity {@code Therapy} that wil be created.
     * @param diagnosis entity {@code Diagnosis} that wil be created.
     * @return {@code id} created entity {@code Therapy}.
     * @throws DaoException if a database access error occurs.
     */
    public int createAmbulatoryTherapyWithDiagnosis(Therapy therapy, Diagnosis diagnosis) throws DaoException {
        int therapyId;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_AMBULATORY_THERAPY_WITH_DIAGNOSIS);
            statement.setString(1, therapy.getPatient().getLogin());
            statement.setString(2, therapy.getDoctor().getLogin());
            statement.setString(3, diagnosis.getIcd().getCode());
            statement.setDate(4, diagnosis.getDiagnosisDate());
            statement.setString(5, diagnosis.getReason());

            resultSet = statement.executeQuery();
            therapyId = resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateStationaryTherapyWithDiagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return therapyId;
    }

    /**
     * Create stationary entity {@code Therapy} and
     * entity {@code Diagnosis} for this stationary {@code Therapy}.
     *
     * @param therapy   entity {@code Therapy} that wil be created.
     * @param diagnosis entity {@code Diagnosis} that wil be created.
     * @return {@code id} created entity {@code Therapy}.
     * @throws DaoException if a database access error occurs.
     */
    public int createStationaryTherapyWithDiagnosis(Therapy therapy, Diagnosis diagnosis) throws DaoException {
        int therapyId;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_STATIONARY_THERAPY_WITH_DIAGNOSIS);
            statement.setString(1, therapy.getPatient().getLogin());
            statement.setString(2, therapy.getDoctor().getLogin());
            statement.setString(3, diagnosis.getIcd().getCode());
            statement.setDate(4, diagnosis.getDiagnosisDate());
            statement.setString(5, diagnosis.getReason());

            resultSet = statement.executeQuery();
            therapyId = resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateStationaryTherapyWithDiagnosis failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return therapyId;
    }

    /**
     * Find current patient entity {@code Therapy} in ambulatory cards database.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     * @see CardType
     */
    @Override
    public Optional<Therapy> findCurrentPatientAmbulatoryTherapy(String doctorLogin, String patientLogin)
            throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Therapy> optionalTherapy = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_CURRENT_PATIENT_AMBULATORY_THERAPY);
            statement.setString(1, doctorLogin);
            statement.setString(2, patientLogin);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findByLoginWithUserDetails(doctorLogin)
                        .orElseThrow(DaoException::new));
                therapy.setPatient(userDao.findByLoginWithUserDetails(patientLogin)
                        .orElseThrow(DaoException::new));
                therapy.setCardType(CardType.AMBULATORY);
                therapy.setEndTherapy(resultSet.getDate(TherapyFieldName.END_THERAPY));
                therapy.setFinalDiagnosis(diagnosisDao.findById(resultSet.getInt(TherapyFieldName.FINAL_DIAGNOSIS_ID))
                        .orElse(null));
                therapy.setDiagnoses(diagnosisDao.findByTherapyId(therapy.getId()));
                optionalTherapy = Optional.of(therapy);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindCurrentPatientTherapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalTherapy;
    }

    /**
     * Find current patient entity {@code Therapy} in stationary cards database.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     * @see CardType
     */
    @Override
    public Optional<Therapy> findCurrentPatientStationaryTherapy(String doctorLogin, String patientLogin)
            throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Therapy> optionalTherapy = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_CURRENT_PATIENT_STATIONARY_THERAPY);
            statement.setString(1, doctorLogin);
            statement.setString(2, patientLogin);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findByLoginWithUserDetails(doctorLogin)
                        .orElseThrow(DaoException::new));
                therapy.setPatient(userDao.findByLoginWithUserDetails(patientLogin)
                        .orElseThrow(DaoException::new));
                therapy.setCardType(CardType.STATIONARY);
                therapy.setEndTherapy(resultSet.getDate(TherapyFieldName.END_THERAPY));
                therapy.setFinalDiagnosis(diagnosisDao.findById(resultSet.getInt(TherapyFieldName.FINAL_DIAGNOSIS_ID))
                        .orElse(null));
                therapy.setDiagnoses(diagnosisDao.findByTherapyId(therapy.getId()));
                optionalTherapy = Optional.of(therapy);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindCurrentPatientTherapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalTherapy;
    }

    /**
     * Set {@code Therapy.endTherapy} field to ambulatory entity {@code Therapy}
     * table by doctor {@code User.login} and patient {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    @Override
    public boolean setAmbulatoryTherapyEndDate(String doctorLogin, String patientLogin, Date date)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_SET_AMBULATORY_THERAPY_END_DATE_BY_DOCTOR_AND_PATIENT);
            statement.setString(1, patientLogin);
            statement.setString(2, doctorLogin);
            statement.setDate(3, date);

            resultSet = statement.executeQuery();
            result = resultSet.next() && resultSet.getInt(1) != 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Close therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    /**
     * Set {@code Therapy.endTherapy} field to stationary entity {@code Therapy}
     * table by doctor {@code User.login} and patient {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    @Override
    public boolean setStationaryTherapyEndDate(String doctorLogin, String patientLogin, Date date)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_SET_STATIONARY_THERAPY_END_DATE_BY_DOCTOR_AND_PATIENT);
            statement.setString(1, patientLogin);
            statement.setString(2, doctorLogin);
            statement.setDate(3, date);

            resultSet = statement.executeQuery();
            result = resultSet.next() && resultSet.getInt(1) != 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Close therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    /**
     * Set {@code Therapy.finalDiagnosis} field to entity {@code Therapy} in
     * stationary card table by doctor {@code User.login} and patient
     * {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     */
    @Override
    public boolean setFinalDiagnosisToTherapy(String doctorLogin, String patientLogin, CardType cardType)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = false;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType == CardType.AMBULATORY ?
                    SQL_SET_FINAL_DIAGNOSIS_AMBULATORY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN :
                    SQL_SET_FINAL_DIAGNOSIS_STATIONARY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN);
            statement.setString(1, doctorLogin);
            statement.setString(2, patientLogin);
            statement.setString(3, doctorLogin);
            statement.setString(4, patientLogin);

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                result = true;
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Close therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return result;
    }

    /**
     * Find patient {@code Therapy} entities by {@code User.login} in database.
     *
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     * @see List
     * @see ArrayList
     */
    @Override
    public List<Therapy> findPatientTherapies(String patientLogin, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Therapy> therapies = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType == CardType.AMBULATORY ?
                    SQL_FIND_AMBULATORY_THERAPY_BY_PATIENT_LOGIN :
                    SQL_FIND_STATIONARY_THERAPY_BY_PATIENT_LOGIN);
            statement.setString(1, patientLogin);
            statement.execute();

            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setCardType(cardType);
                therapy.setPatient(userDao.findByLoginWithUserDetails(patientLogin).orElseThrow(DaoException::new));
                therapy.setEndTherapy(resultSet.getDate(TherapyFieldName.END_THERAPY));
                therapy.setFinalDiagnosis(diagnosisDao.findById(resultSet.getInt(TherapyFieldName.FINAL_DIAGNOSIS_ID))
                        .orElse(null));
                therapy.setDiagnoses(diagnosisDao.findByTherapyId(therapy.getId()));
                therapies.add(therapy);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindPatientTherapies failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return therapies;
    }

    /**
     * Find open doctor {@code Therapy} entities by {@code User.login} in database.
     *
     * @param doctorLogin {@code String} value of {@code User.login} field.
     * @param cardType    element of enum {@code CardType}
     *                    table is selected based on this element.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     * @see List
     * @see ArrayList
     */
    @Override
    public List<Therapy> findOpenDoctorTherapies(String doctorLogin, CardType cardType) throws DaoException {
        List<Therapy> therapies = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType == CardType.AMBULATORY ?
                    SQL_FIND_OPEN_AMBULATORY_THERAPY_BY_DOCTOR_LOGIN :
                    SQL_FIND_OPEN_STATIONARY_THERAPY_BY_DOCTOR_LOGIN);
            statement.setString(1, doctorLogin);
            statement.execute();

            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setCardType(cardType);
                therapy.setPatient(userDao.findByLoginWithUserDetails(doctorLogin).orElseThrow(DaoException::new));
                therapy.setEndTherapy(resultSet.getDate(TherapyFieldName.END_THERAPY));
                therapy.setFinalDiagnosis(diagnosisDao.findById(resultSet.getInt(TherapyFieldName.FINAL_DIAGNOSIS_ID))
                        .orElse(null));
                therapy.setDiagnoses(diagnosisDao.findByTherapyId(therapy.getId()));
                therapies.add(therapy);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("FindOpenDoctorTherapies failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return therapies;
    }
}
