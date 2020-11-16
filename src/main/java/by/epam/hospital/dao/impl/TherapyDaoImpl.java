package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.*;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.table.TherapyFieldName;
import by.epam.hospital.entity.table.UsersFieldName;

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
     * Sql {@code String} object for creating
     * {@code Therapy} entity in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE_THERAPY = """
            INSERT INTO therapy (doctor_id) VALUES (?)""";
    /**
     * Sql {@code String} object for adding {@code Therapy} entity to
     * ambulatory card table in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE_AMBULATORY_THERAPY = """
            INSERT INTO ambulatory_cards (patient_id, therapy_id) VALUES (?, ?)""";
    /**
     * Sql {@code String} object for adding {@code Therapy} entity to
     * stationary card table in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE_STATIONARY_THERAPY = """
            INSERT INTO stationary_cards (patient_id, therapy_id) VALUES (?, ?)""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity in
     * ambulatory card table by {@code Therapy.id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_AMBULATORY_THERAPY_BY_THERAPY_ID = """
            SELECT doctor_id, end_therapy, final_diagnosis_id, patient_id, patients.id
            FROM therapy
            INNER JOIN ambulatory_cards ac on therapy.id = ac.therapy_id
            INNER JOIN users patients on ac.patient_id = patients.id
            WHERE therapy.id = ?""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity in
     * stationary card table by {@code Therapy.id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_STATIONARY_THERAPY_BY_THERAPY_ID = """
            SELECT doctor_id, end_therapy, final_diagnosis_id, patient_id
            FROM therapy
            INNER JOIN stationary_cards sc on therapy.id = sc.therapy_id
            INNER JOIN users patients on sc.patient_id = patients.id
            WHERE therapy.id = ?""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity in
     * ambulatory card table by doctor {@code User.login}
     * and patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_AMBULATORY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN = """
            SELECT therapy.id, doctor_id, end_therapy, final_diagnosis_id
            FROM therapy
            INNER JOIN users doctors on therapy.doctor_id = doctors.id
            INNER JOIN ambulatory_cards ac on therapy.id = ac.therapy_id
            INNER JOIN users patients on ac.patient_id = patients.id
            WHERE doctor_id = (
                SELECT doctors.id 
                WHERE doctors.login = ?)
            AND patient_id = (
                SELECT patients.id 
                WHERE patients.login = ?)""";
    /**
     * Sql {@code String} object for find {@code Therapy} entity in
     * stationary card table by doctor {@code User.login}
     * and patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_STATIONARY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN = """
            SELECT therapy.id, doctor_id, end_therapy, final_diagnosis_id
            FROM therapy
            INNER JOIN users doctors on therapy.doctor_id = doctors.id
            INNER JOIN stationary_cards sc on therapy.id = sc.therapy_id
            INNER JOIN users patients on sc.patient_id = patients.id
            WHERE doctor_id = (
                SELECT doctors.id
                WHERE doctors.login = ?)
            AND patient_id = (
                SELECT patients.id
                WHERE patients.login = ?)""";
    /**
     * Sql {@code String} object for set {@code Therapy.endTherapy}
     * field to entity {@code Therapy} in
     * ambulatory card table by doctor {@code User.login}
     * and patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_SET_END_AMBULATORY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN = """
            UPDATE therapy
            SET end_therapy = ?
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
     * Sql {@code String} object for set {@code Therapy.endTherapy}
     * field to entity {@code Therapy} in
     * stationary card table by doctor {@code User.login}
     * and patient {@code User.login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_SET_END_STATIONARY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN = """
            UPDATE therapy
            SET end_therapy = ?
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
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();
    /**
     * {@code DiagnosisDao} data access object.
     */
    private final DiagnosisDao diagnosisDao = new DiagnosisDaoImpl();

    /**
     * Create entity {@code Therapy} in database using {@code PreparedStatement}
     * with parameter {@code Statement.RETURN_GENERATED_KEYS}.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return auto-generated {@code Therapy.id} field.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see CardType
     */
    @Override
    public int create(String doctorLogin, String patientLogin, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        int therapyId = 0;
        int doctorId = userDao.findByLogin(doctorLogin).orElseThrow(DaoException::new).getId();
        int patientId = userDao.findByLogin(patientLogin).orElseThrow(DaoException::new).getId();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(SQL_CREATE_THERAPY, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, doctorId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    therapyId = generatedKeys.getInt(1);
                    generatedKeys.close();
                    statement.close();

                    statement = connection.prepareStatement(cardType == CardType.AMBULATORY ?
                            SQL_CREATE_AMBULATORY_THERAPY :
                            SQL_CREATE_STATIONARY_THERAPY);
                    statement.setInt(1, patientId);
                    statement.setInt(2, therapyId);

                    affectedRows = statement.executeUpdate();
                    if (affectedRows == 0) {
                        connection.rollback();
                    }
                }
            }
            connection.setAutoCommit(true);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Creating therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return therapyId;
    }

    /**
     * Set {@code Therapy.endTherapy} field to entity {@code Therapy}
     * table by doctor {@code User.login} and patient {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return auto-generated {@code Therapy.id} field.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     */
    @Override
    public boolean setEndTherapy(String doctorLogin, String patientLogin, Date date, CardType cardType)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = false;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType == CardType.AMBULATORY ?
                    SQL_SET_END_AMBULATORY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN :
                    SQL_SET_END_STATIONARY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN);
            statement.setDate(1, date);
            statement.setString(2, doctorLogin);
            statement.setString(3, patientLogin);

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
     * Find current patient entity {@code Therapy} in database
     * using {@code PreparedStatement}.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see CardType
     */
    @Override
    public Optional<Therapy> findCurrentPatientTherapy(String doctorLogin, String patientLogin, CardType cardType)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<Therapy> optionalTherapy = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType == CardType.AMBULATORY ?
                    SQL_FIND_AMBULATORY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN :
                    SQL_FIND_STATIONARY_THERAPY_BY_DOCTOR_AND_PATIENT_LOGIN);
            statement.setString(1, doctorLogin);
            statement.setString(2, patientLogin);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findByLogin(doctorLogin).orElseThrow(DaoException::new));
                therapy.setPatient(userDao.findByLoginWithUserDetails(patientLogin).orElseThrow(DaoException::new));
                therapy.setCardType(cardType);
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
     * Find {@code Therapy} entity by {@code id} in database
     * using {@code PreparedStatement}.
     *
     * @param id       {@code int} value of {@code User.id} field.
     * @param cardType element of enum {@code CardType}
     *                 table is selected based on this element.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see CardType
     */
    @Override
    public Optional<Therapy> findById(int id, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<Therapy> optionalTherapy = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType == CardType.AMBULATORY ?
                    SQL_FIND_AMBULATORY_THERAPY_BY_THERAPY_ID :
                    SQL_FIND_STATIONARY_THERAPY_BY_THERAPY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(id);
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setEndTherapy(resultSet.getDate(TherapyFieldName.END_THERAPY));
                therapy.setFinalDiagnosis(diagnosisDao.findById(resultSet.getInt(TherapyFieldName.FINAL_DIAGNOSIS_ID))
                        .orElse(null));
                therapy.setDiagnoses(diagnosisDao.findByTherapyId(id));
                therapy.setPatient(userDao.findByIdWithUserDetails(resultSet.getInt(UsersFieldName.ID))
                        .orElseThrow(DaoException::new));
                therapy.setCardType(cardType);
                optionalTherapy = Optional.of(therapy);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("findById therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalTherapy;
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
