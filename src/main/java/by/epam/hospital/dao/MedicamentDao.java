package by.epam.hospital.dao;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Medicament;
import by.epam.hospital.entity.PageResult;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MedicamentDao {
    int create(Medicament medicament) throws DaoException;

    Optional<Medicament> findById(int id) throws DaoException;

    Optional<Medicament> findByName(String name) throws DaoException;

    Optional<Medicament> updateEnabledStatus(int id, boolean isEnabled) throws DaoException;

    PageResult<Medicament> findAllByNamePartPaging(String namePart, int page) throws DaoException;

    boolean assignMedicamentToDiagnosis(String name, LocalDateTime assignDateTime, String description, String doctorLogin,
                                        String patientLogin, CardType cardType) throws DaoException;
}
