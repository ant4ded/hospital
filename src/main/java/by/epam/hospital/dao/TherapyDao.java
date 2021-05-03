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

    /**
     * Find current patient entity {@code Therapy} in ambulatory cards database.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<Therapy> findCurrentPatientAmbulatoryTherapy(String doctorLogin, String patientLogin)
            throws DaoException;

    /**
     * Find current patient entity {@code Therapy} in stationary cards database.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<Therapy> findCurrentPatientStationaryTherapy(String doctorLogin, String patientLogin)
            throws DaoException;

    /**
     * Set {@code Therapy.endTherapy} field to ambulatory entity {@code Therapy}
     * table by doctor {@code User.login} and patient {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean setAmbulatoryTherapyEndDate(String doctorLogin, String patientLogin, Date date) throws DaoException;

    /**
     * Set {@code Therapy.endTherapy} field to stationary entity {@code Therapy}
     * table by doctor {@code User.login} and patient {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean setStationaryTherapyEndDate(String doctorLogin, String patientLogin, Date date) throws DaoException;

    /**
     * Set {@code Therapy.finalDiagnosis} field to ambulatory entity
     * {@code Therapy} in by doctor {@code User.login} and patient
     * {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean setFinalDiagnosisToAmbulatoryTherapy(String doctorLogin, String patientLogin) throws DaoException;

    /**
     * Set {@code Therapy.finalDiagnosis} field to stationary entity
     * {@code Therapy} in by doctor {@code User.login} and patient
     * {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean setFinalDiagnosisToStationaryTherapy(String doctorLogin, String patientLogin) throws DaoException;

    /**
     * Find ambulatory patient {@code Therapy} entities by {@code User.login} in database.
     *
     * @param patientUserDetails {@code UserDetails} object for find.
     * @return {@code List<Therapy>} if it present
     * or an empty {@code List<Therapy>} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see List
     */
    List<Therapy> findAmbulatoryPatientTherapies(UserDetails patientUserDetails) throws DaoException;

    /**
     * Find stationary patient {@code Therapy} entities by {@code User.login} in database.
     *
     * @param patientUserDetails {@code UserDetails} object for find.
     * @return {@code List<Therapy>} if it present
     * or an empty {@code List<Therapy>} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see List
     */
    List<Therapy> findStationaryPatientTherapies(UserDetails patientUserDetails) throws DaoException;

    /**
     * Find open ambulatory doctor {@code Therapy}
     * entities by {@code User.login} in database.
     *
     * @param doctorLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see List
     */
    List<Therapy> findOpenDoctorAmbulatoryTherapies(String doctorLogin) throws DaoException;

    /**
     * Find open stationary doctor {@code Therapy}
     * entities by {@code User.login} in database.
     *
     * @param doctorLogin {@code String} value of {@code User.login} field.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see List
     */
    List<Therapy> findOpenDoctorStationaryTherapies(String doctorLogin) throws DaoException;
}
