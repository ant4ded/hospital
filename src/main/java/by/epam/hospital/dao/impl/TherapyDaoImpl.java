package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DiagnosisDao;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.*;
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
     * Sql {@code String} object for call stored procedure
     * {@code CreateAmbulatoryTherapyWithDiagnosis}.
     * Written for the MySQL dialect.
     */
    private static final String SP_CREATE_AMBULATORY_THERAPY_WITH_DIAGNOSIS =
            "CALL CreateAmbulatoryTherapyWithDiagnosis(?,?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code CreateStationaryTherapyWithDiagnosis}.
     * Written for the MySQL dialect.
     */
    private static final String SP_CREATE_STATIONARY_THERAPY_WITH_DIAGNOSIS =
            "CALL CreateStationaryTherapyWithDiagnosis(?,?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindCurrentPatientAmbulatoryTherapy}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_CURRENT_PATIENT_AMBULATORY_THERAPY =
            "CALL FindCurrentPatientAmbulatoryTherapy(?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindCurrentPatientStationaryTherapy}.
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
     * Sql {@code String} object for call stored procedure
     * {@code SetFinalDiagnosisToAmbulatoryTherapyByDoctorAndPatient}.
     * Written for the MySQL dialect.
     */
    private static final String SP_SET_FINAL_DIAGNOSIS_TO_AMBULATORY_THERAPY =
            "CALL SetFinalDiagnosisToAmbulatoryTherapyByDoctorAndPatient(?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code SetFinalDiagnosisToStationaryTherapyByDoctorAndPatient}.
     * Written for the MySQL dialect.
     */
    private static final String SP_SET_FINAL_DIAGNOSIS_TO_STATIONARY_THERAPY =
            "CALL SetFinalDiagnosisToStationaryTherapyByDoctorAndPatient(?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindAmbulatoryTherapiesByPatient}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_AMBULATORY_THERAPIES_BY_PATIENT_LOGIN =
            "CALL FindAmbulatoryTherapiesByPatient(?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindStationaryTherapiesByPatient}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_STATIONARY_THERAPIES_BY_PATIENT_LOGIN =
            "CALL FindStationaryTherapiesByPatient(?,?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindOpenAmbulatoryTherapiesByDoctor}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_OPEN_AMBULATORY_THERAPY_BY_DOCTOR_LOGIN =
            "CALL FindOpenAmbulatoryTherapiesByDoctor(?)";
    /**
     * Sql {@code String} object for call stored procedure
     * {@code FindOpenStationaryTherapiesByDoctor}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_OPEN_STATIONARY_THERAPY_BY_DOCTOR_LOGIN =
            "CALL FindOpenStationaryTherapiesByDoctor(?)";

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
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
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
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
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
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     * @see Optional
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
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     * @see Optional
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
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
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
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
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
     * Set {@code Therapy.finalDiagnosis} field to ambulatory entity
     * {@code Therapy} in by doctor {@code User.login} and patient
     * {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     */
    @Override
    public boolean setFinalDiagnosisToAmbulatoryTherapy(String doctorLogin, String patientLogin) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_SET_FINAL_DIAGNOSIS_TO_AMBULATORY_THERAPY);
            statement.setString(1, patientLogin);
            statement.setString(2, doctorLogin);

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
     * Set {@code Therapy.finalDiagnosis} field to stationary entity
     * {@code Therapy} in by doctor {@code User.login} and patient
     * {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     */
    @Override
    public boolean setFinalDiagnosisToStationaryTherapy(String doctorLogin, String patientLogin) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_SET_FINAL_DIAGNOSIS_TO_STATIONARY_THERAPY);
            statement.setString(1, patientLogin);
            statement.setString(2, doctorLogin);

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
     * Find ambulatory patient {@code Therapy} entities by {@code User.login} in database.
     *
     * @param patientUserDetails {@code UserDetails} object for find.
     * @return {@code List<Therapy>} if it present
     * or an empty {@code List<Therapy>} if it isn't.
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     * @see List
     */
    @Override
    public List<Therapy> findAmbulatoryPatientTherapies(UserDetails patientUserDetails) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Therapy> therapies = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_AMBULATORY_THERAPIES_BY_PATIENT_LOGIN);
            statement.setString(1, patientUserDetails.getFirstName());
            statement.setString(2, patientUserDetails.getSurname());
            statement.setString(3, patientUserDetails.getLastName());
            statement.setDate(4, patientUserDetails.getBirthday());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setCardType(CardType.AMBULATORY);
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setPatient(userDao.findUserWithUserDetailsByPassportData(patientUserDetails.getFirstName(),
                        patientUserDetails.getSurname(), patientUserDetails.getLastName(),
                        patientUserDetails.getBirthday())
                        .orElseThrow(DaoException::new));
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
     * Find stationary patient {@code Therapy} entities by {@code User.login} in database.
     *
     * @param patientUserDetails {@code UserDetails} object for find.
     * @return {@code List<Therapy>} if it present
     * or an empty {@code List<Therapy>} if it isn't.
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     * @see List
     */
    @Override
    public List<Therapy> findStationaryPatientTherapies(UserDetails patientUserDetails) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Therapy> therapies = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_STATIONARY_THERAPIES_BY_PATIENT_LOGIN);
            statement.setString(1, patientUserDetails.getFirstName());
            statement.setString(2, patientUserDetails.getSurname());
            statement.setString(3, patientUserDetails.getLastName());
            statement.setDate(4, patientUserDetails.getBirthday());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setCardType(CardType.STATIONARY);
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setPatient(userDao.findUserWithUserDetailsByPassportData(patientUserDetails.getFirstName(),
                        patientUserDetails.getSurname(), patientUserDetails.getLastName(),
                        patientUserDetails.getBirthday())
                        .orElseThrow(DaoException::new));
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
     * Find open ambulatory doctor {@code Therapy}
     * entities by {@code User.login} in database.
     *
     * @param doctorLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     * @see List
     */
    @Override
    public List<Therapy> findOpenDoctorAmbulatoryTherapies(String doctorLogin) throws DaoException {
        List<Therapy> therapies = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_OPEN_AMBULATORY_THERAPY_BY_DOCTOR_LOGIN);
            statement.setString(1, doctorLogin);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setCardType(CardType.AMBULATORY);
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

    /**
     * Find open stationary doctor {@code Therapy}
     * entities by {@code User.login} in database.
     *
     * @param doctorLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     * @see List
     */
    @Override
    public List<Therapy> findOpenDoctorStationaryTherapies(String doctorLogin) throws DaoException {
        List<Therapy> therapies = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_OPEN_STATIONARY_THERAPY_BY_DOCTOR_LOGIN);
            statement.setString(1, doctorLogin);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setCardType(CardType.STATIONARY);
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
