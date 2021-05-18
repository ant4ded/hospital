package by.epam.hospital.dao;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.UserDetails;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * A {@code TherapyDao} data access objects works with the database and
 * can be used for work with the {@link Therapy} database entity.
 */

public interface TherapyDao {
    int createTherapyWithDiagnosis(Therapy therapy, Diagnosis diagnosis, CardType cardType) throws DaoException;

    Optional<Therapy> findCurrentPatientTherapy(String doctorLogin, String patientLogin, CardType cardType) throws DaoException;

    List<Therapy> findOpenDoctorTherapies(String doctorLogin, CardType cardType) throws DaoException;

    boolean setTherapyEndDate(String doctorLogin, String patientLogin, Date date, CardType cardType) throws DaoException;

    boolean setFinalDiagnosisToTherapy(String doctorLogin, String patientLogin, CardType cardType) throws DaoException;

    List<Therapy> findPatientTherapies(UserDetails patientUserDetails, CardType cardType) throws DaoException;
}
