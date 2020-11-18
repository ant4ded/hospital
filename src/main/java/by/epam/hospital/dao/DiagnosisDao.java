package by.epam.hospital.dao;

import by.epam.hospital.entity.Diagnosis;

import java.util.List;
import java.util.Optional;

/**
 * A {@code DiagnosisDao} data access objects works with the database and
 * can be used for work with the {@link Diagnosis} database entity.
 */

public interface DiagnosisDao {
    /**
     * Create entity {@code Diagnosis} for ambulatory
     * {@code Therapy} entity in database.
     *
     * @param diagnosis    an a {@code Diagnosis} entity.
     * @param patientLogin {@code String} value of patient
     *                     {@code User.login} field.
     * @return auto-generated {@code Diagnosis.id} field.
     * @throws DaoException if a database access error occurs.
     */
    int createAmbulatoryDiagnosis(Diagnosis diagnosis, String patientLogin) throws DaoException;

    /**
     * Create entity {@code Diagnosis} for stationary
     * {@code Therapy} entity in database.
     *
     * @param diagnosis    an a {@code Diagnosis} entity.
     * @param patientLogin {@code String} value of patient
     *                     {@code User.login} field.
     * @return auto-generated {@code Diagnosis.id} field.
     * @throws DaoException if a database access error occurs.
     */
    int createStationaryDiagnosis(Diagnosis diagnosis, String patientLogin) throws DaoException;

    /**
     * Create entity {@code Diagnosis} in database.
     *
     * @param diagnosis    an a {@code Diagnosis} entity.
     * @param patientLogin {@code String} value of patient
     *                     {@code User.login} field.
     * @param therapyId    {@code int} value of {@code Therapy.id}.
     * @return auto-generated {@code Diagnosis.id} field.
     * @throws DaoException if a database access error occurs.
     */
    int create(Diagnosis diagnosis, String patientLogin, int therapyId) throws DaoException;

    /**
     * Find all {@code Diagnosis} entity by {@code Therapy.id} field.
     *
     * @param id {@code int} value of {@code Therapy.id} field.
     * @return {@code List<Diagnosis>} if it present
     * or an empty {@code List} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see List
     */
    List<Diagnosis> findByTherapyId(int id) throws DaoException;

    /**
     * Find {@code Diagnosis} entity by {@code Diagnosis.id} field.
     *
     * @param id {@code int} value of {@code Therapy.id} field.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<Diagnosis> findById(int id) throws DaoException;
}
