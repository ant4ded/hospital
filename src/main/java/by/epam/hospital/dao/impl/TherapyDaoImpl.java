package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DiagnosisDao;
import by.epam.hospital.dao.TherapyDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.table.TherapyFieldName;
import by.epam.hospital.entity.table.UsersFieldName;

import java.sql.*;
import java.util.Optional;

public class TherapyDaoImpl implements TherapyDao {
    private static final String SQL_CREATE_THERAPY =
            "INSERT INTO therapy (doctor_id) VALUES (?)";
    private static final String SQL_CREATE_AMBULATORY_THERAPY =
            "INSERT INTO ambulatory_cards (patient_id, therapy_id) VALUES (?, ?)";
    private static final String SQL_CREATE_STATIONARY_THERAPY =
            "INSERT INTO stationary_cards (patient_id, therapy_id) VALUES (?, ?)";
    private static final String SQL_FIND_AMBULATORY_BY_ID =
            "SELECT doctor_id, end_therapy, final_diagnosis_id, patient_id, patients.id FROM therapy " +
                    "INNER JOIN ambulatory_cards ac on therapy.id = ac.therapy_id " +
                    "INNER JOIN users patients on ac.patient_id = patients.id  " +
                    "WHERE therapy.id = ?";
    private static final String SQL_FIND_STATIONARY_BY_ID =
            "SELECT doctor_id, end_therapy, final_diagnosis_id, patient_id FROM therapy " +
                    "INNER JOIN stationary_cards sc on therapy.id = sc.therapy_id " +
                    "INNER JOIN users patients on sc.patient_id = patients.id  " +
                    "WHERE therapy.id = ?";
    private static final String SQL_FIND_AMBULATORY_BY_DOCTOR_AND_PATIENT =
            "SELECT therapy.id, doctor_id, end_therapy, final_diagnosis_id FROM therapy " +
                    "INNER JOIN users doctors on therapy.doctor_id = doctors.id " +
                    "INNER JOIN ambulatory_cards ac on therapy.id = ac.therapy_id " +
                    "INNER JOIN users patients on ac.patient_id = patients.id " +
                    "WHERE doctor_id = (SELECT doctors.id WHERE doctors.login = ?) " +
                    "AND patient_id = (SELECT patients.id WHERE patients.login = ?)";
    private static final String SQL_FIND_STATIONARY_BY_DOCTOR_AND_PATIENT =
            "SELECT therapy.id, doctor_id, end_therapy, final_diagnosis_id FROM therapy " +
                    "INNER JOIN users doctors on therapy.doctor_id = doctors.id " +
                    "INNER JOIN stationary_cards sc on therapy.id = sc.therapy_id " +
                    "INNER JOIN users patients on sc.patient_id = patients.id " +
                    "WHERE doctor_id = (SELECT doctors.id WHERE doctors.login = ?) " +
                    "AND patient_id = (SELECT patients.id WHERE patients.login = ?)";

    private final UserDao userDao = new UserDaoImpl();
    private final DiagnosisDao diagnosisDao = new DiagnosisDaoImpl();

    @Override
    public int create(String doctorLogin, String patientLogin, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        int therapyId;
        int doctorId = userDao.findByLogin(doctorLogin).orElseThrow(DaoException::new).getId();
        int patientId = userDao.findByLogin(patientLogin).orElseThrow(DaoException::new).getId();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(SQL_CREATE_THERAPY, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, doctorId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Creating therapy failed, no rows affected.");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                therapyId = generatedKeys.getInt(1);
            } else {
                throw new DaoException("Creating therapy failed, no id obtained.");
            }
            generatedKeys.close();
            statement.close();

            statement = connection.prepareStatement(cardType.equals(CardType.AMBULATORY) ?
                    SQL_CREATE_AMBULATORY_THERAPY : SQL_CREATE_STATIONARY_THERAPY);
            statement.setInt(1, patientId);
            statement.setInt(2, therapyId);

            affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Adding therapy to " + (cardType.equals(CardType.AMBULATORY)
                        ? "ambulatory_cards" : "stationary_cards") + " failed, no rows affected.");
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

    @Override
    public Optional<Therapy> find(String doctorLogin, String patientLogin, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Therapy therapy = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType.equals(CardType.AMBULATORY) ?
                    SQL_FIND_AMBULATORY_BY_DOCTOR_AND_PATIENT : SQL_FIND_STATIONARY_BY_DOCTOR_AND_PATIENT);
            statement.setString(1, doctorLogin);
            statement.setString(2, patientLogin);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                therapy = new Therapy();
                therapy.setId(resultSet.getInt(TherapyFieldName.ID));
                therapy.setDoctor(userDao.findByLogin(doctorLogin).orElseThrow(DaoException::new));
                therapy.setPatient(userDao.findByLogin(patientLogin).orElseThrow(DaoException::new));
                therapy.setCardType(cardType);
                therapy.setEndTherapy(Optional.ofNullable(resultSet.getDate(TherapyFieldName.END_THERAPY)));
                therapy.setFinalDiagnosis(diagnosisDao.findById(resultSet.getInt(TherapyFieldName.FINAL_DIAGNOSIS_ID)));
                therapy.setDiagnoses(diagnosisDao.findAllByTherapyId(therapy.getId()));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(therapy);
    }

    @Override
    public Optional<Therapy> findById(int id, CardType cardType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Therapy therapy = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(cardType.equals(CardType.AMBULATORY) ?
                    SQL_FIND_AMBULATORY_BY_ID : SQL_FIND_STATIONARY_BY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                therapy = new Therapy();
                therapy.setId(id);
                therapy.setDoctor(userDao.findById(resultSet.getInt(TherapyFieldName.DOCTOR_ID))
                        .orElseThrow(DaoException::new));
                therapy.setEndTherapy(Optional.ofNullable(resultSet.getDate(TherapyFieldName.END_THERAPY)));
                therapy.setFinalDiagnosis(diagnosisDao.findById(resultSet.getInt(TherapyFieldName.FINAL_DIAGNOSIS_ID)));
                therapy.setDiagnoses(diagnosisDao.findAllByTherapyId(id));
                therapy.setPatient(userDao.findById(resultSet.getInt(UsersFieldName.ID))
                        .orElseThrow(DaoException::new));
                if (cardType.equals(CardType.AMBULATORY)) {
                    therapy.setCardType(CardType.AMBULATORY);
                } else {
                    therapy.setCardType(CardType.STATIONARY);
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find therapy failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(therapy);
    }
}
