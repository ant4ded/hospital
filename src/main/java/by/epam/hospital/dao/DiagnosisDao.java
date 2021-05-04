package by.epam.hospital.dao;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.MedicamentAssignment;
import by.epam.hospital.entity.ProcedureAssignment;

import java.util.List;
import java.util.Optional;

/**
 * A {@code DiagnosisDao} data access objects works with the database and
 * can be used for work with the {@link Diagnosis} database entity.
 */

public interface DiagnosisDao {

    int createDiagnosis(Diagnosis diagnosis, String patientLogin, CardType cardType) throws DaoException;

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

    boolean assignProcedureToLastDiagnosis(ProcedureAssignment assignment, String doctorLogin,
                                           String patientLogin, CardType cardType) throws DaoException;

    boolean assignMedicamentToLastDiagnosis(MedicamentAssignment assignment, String doctorLogin,
                                            String patientLogin, CardType cardType) throws DaoException;
}
