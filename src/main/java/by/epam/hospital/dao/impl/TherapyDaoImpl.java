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
import by.epam.hospital.entity.UserDetails;
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
    private static final String SP_CREATE_THERAPY_WITH_DIAGNOSIS =
            "CALL CreateTherapyWithDiagnosis(?,?,?,?,?,?)";
    private static final String SP_FIND_CURRENT_PATIENT_THERAPY =
            "CALL FindCurrentPatientTherapy(?,?,?)";
    private static final String SP_FIND_OPEN_THERAPY_BY_DOCTOR_LOGIN =
            "CALL FindOpenDoctorTherapies(?,?)";
    private static final String SP_SET_THERAPY_END_DATE =
            "CALL SetTherapyEndDate(?,?,?,?)";
    private static final String SP_SET_FINAL_DIAGNOSIS_TO_THERAPY =
            "CALL SetFinalDiagnosisToTherapy(?,?,?)";
    private static final String SP_FIND_PATIENT_THERAPIES =
            "CALL FindPatientTherapies(?,?,?,?,?)";

    /**
     * {@code UserDao} data access object.
     */
    private final UserDao userDao = new UserDaoImpl();
    /**
     * {@code DiagnosisDao} data access object.
     */
    private final DiagnosisDao diagnosisDao = new DiagnosisDaoImpl();

    @Override
    public int createTherapyWithDiagnosis(Therapy therapy, Diagnosis diagnosis, CardType cardType) throws DaoException {
        int therapyId;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_THERAPY_WITH_DIAGNOSIS);
            statement.setString(1, therapy.getPatient().getLogin());
            statement.setString(2, therapy.getDoctor().getLogin());
            statement.setString(3, diagnosis.getIcd().getCode());
            statement.setDate(4, diagnosis.getDiagnosisDate());
            statement.setString(5, diagnosis.getReason());
            statement.setString(6, cardType.name());

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

    @Override
    public Optional<Therapy> findCurrentPatientTherapy(String doctorLogin, String patientLogin, CardType cardType) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Therapy> optionalTherapy = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_CURRENT_PATIENT_THERAPY);
            statement.setString(1, doctorLogin);
            statement.setString(2, patientLogin);
            statement.setString(3, cardType.name());

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

    @Override
    public List<Therapy> findOpenDoctorTherapies(String doctorLogin, CardType cardType) throws DaoException {
        String patientId = "patient_id";
        List<Therapy> therapies = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_OPEN_THERAPY_BY_DOCTOR_LOGIN);
            statement.setString(1, doctorLogin);
            statement.setString(2, cardType.name());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Therapy therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setCardType(CardType.AMBULATORY);
                therapy.setPatient(userDao
                        .findByIdWithUserDetails(resultSet.getInt(patientId))
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
            throw new DaoException("FindOpenDoctorTherapies failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return therapies;
    }

    @Override
    public boolean setTherapyEndDate(String doctorLogin, String patientLogin, Date date, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_SET_THERAPY_END_DATE);
            statement.setString(1, patientLogin);
            statement.setString(2, doctorLogin);
            statement.setDate(3, date);
            statement.setString(4, cardType.name());

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

    @Override
    public boolean setFinalDiagnosisToTherapy(String doctorLogin, String patientLogin, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_SET_FINAL_DIAGNOSIS_TO_THERAPY);
            statement.setString(1, patientLogin);
            statement.setString(2, doctorLogin);
            statement.setString(3, cardType.name());

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

    @Override
    public List<Therapy> findPatientTherapies(UserDetails patientUserDetails, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Therapy> therapies = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SP_FIND_PATIENT_THERAPIES);
            statement.setString(1, patientUserDetails.getFirstName());
            statement.setString(2, patientUserDetails.getSurname());
            statement.setString(3, patientUserDetails.getLastName());
            statement.setDate(4, patientUserDetails.getBirthday());
            statement.setString(5, cardType.name());

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
}
