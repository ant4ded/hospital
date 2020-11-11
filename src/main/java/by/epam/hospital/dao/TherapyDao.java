package by.epam.hospital.dao;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;

import java.util.List;
import java.util.Optional;

/**
 * A {@code TherapyDao} data access objects works with the database and
 * can be used for work with the {@link Therapy} database entity.
 */

public interface TherapyDao {
    /**
     * Create entity {@code Therapy} in database.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return auto-generated {@code Therapy.id} field.
     * @throws DaoException if a database access error occurs.
     * @see CardType
     */
    int create(String doctorLogin, String patientLogin, CardType cardType) throws DaoException;

    /**
     * Find current patient entity {@code Therapy} in database.
     *
     * @param doctorLogin  {@code String} value of {@code User.login} field.
     * @param patientLogin {@code String} value of {@code User.login} field.
     * @param cardType     element of enum {@code CardType}
     *                     table is selected based on this element.
     * @return {@code Optional<Therapy>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     * @see CardType
     */
    Optional<Therapy> findCurrentPatientTherapy(String doctorLogin, String patientLogin, CardType cardType)
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

    List<Therapy> findPatientTherapies(String patientLogin, CardType cardType) throws DaoException;
}
