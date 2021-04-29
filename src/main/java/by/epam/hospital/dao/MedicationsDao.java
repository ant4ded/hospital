package by.epam.hospital.dao;

import by.epam.hospital.entity.Medication;

import java.util.List;
import java.util.Optional;

public interface MedicationsDao {
    int create(Medication medication) throws DaoException;

    Optional<Medication> findById(int id) throws DaoException;

    Optional<Medication> findByName(String name) throws DaoException;

    Optional<Medication> updateEnabledStatus(int id, boolean isEnabled) throws DaoException;

    List<Medication> findAllByNamePartPaging(String namePart, int from, int to) throws DaoException;
}
