package by.epam.hospital.dao;

import by.epam.hospital.entity.Medicament;

import java.util.List;
import java.util.Optional;

public interface MedicamentDao {
    int create(Medicament medicament) throws DaoException;

    Optional<Medicament> findById(int id) throws DaoException;

    Optional<Medicament> findByName(String name) throws DaoException;

    Optional<Medicament> updateEnabledStatus(int id, boolean isEnabled) throws DaoException;

    List<Medicament> findAllByNamePartPaging(String namePart, int page) throws DaoException;
}
