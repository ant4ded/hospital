package by.epam.hospital.dao;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.Therapy;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * A {@code TherapyDao} data access objects works with the database and
 * can be used for work with the {@link Therapy} database entity.
 */

public interface TherapyDao {
    /**
     * Create ambulatory entity {@code Therapy} and
     * entity {@code Diagnosis} for this ambulatory {@code Therapy}.
     *
     * @param therapy entity {@code Therapy} that wil be created.
     * @param diagnosis entity {@code Diagnosis} that wil be created.
     * @return {@code id} created entity {@code Therapy}.
     * @throws DaoException if a database access error occurs.
     */
    int createAmbulatoryTherapyWithDiagnosis(Therapy therapy, Diagnosis diagnosis) throws DaoException;

    /**
     * Create stationary entity {@code Therapy} and
     * entity {@code Diagnosis} for this stationary {@code Therapy}.
     *
     * @param therapy entity {@code Therapy} that wil be created.
     * @param diagnosis entity {@code Diagnosis} that wil be created.
     * @return {@code id} created entity {@code Therapy}.
     * @throws DaoException if a database access error occurs.
     */
    int createStationaryTherapyWithDiagnosis(Therapy therapy, Diagnosis diagnosis) throws DaoException;

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
     * @see CardType
     */
    Optional<Therapy> findCurrentPatientStationaryTherapy(String doctorLogin, String patientLogin)
            throws DaoException;

    /**
     * Set {@code Therapy.endTherapy} field to entity {@code Therapy}
     * table by doctor {@code User.login} and patient {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     */
    boolean setEndTherapy(String doctorLogin, String patientLogin, Date date, CardType cardType)
            throws DaoException;

    /**
     * Set {@code Therapy.finalDiagnosis} field to entity {@code Therapy} in
     * stationary card table by doctor {@code User.login} and patient {@code User.login} in data base.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return {@code true} if success and {@code false} if not.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     */
    boolean setFinalDiagnosisToTherapy(String doctorLogin, String patientLogin, CardType cardType)
            throws DaoException;

    /**
     * Find {@code Therapy} entity by {@code id} in database.
     *
     * @param id       {@code int} value of {@code User.id} field.
     * @param cardType element of enum {@code CardType}
     *                 table is selected based on this element.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     * @see CardType
     */
    Optional<Therapy> findById(int id, CardType cardType) throws DaoException;

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
     */
    List<Therapy> findPatientTherapies(String patientLogin, CardType cardType) throws DaoException;

    /**
     * Find open doctor {@code Therapy} entities by {@code User.login} in database.
     *
     * @param doctorLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     * @see List
     */
    List<Therapy> findOpenDoctorTherapies(String doctorLogin, CardType cardType) throws DaoException;
}
