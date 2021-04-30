package by.epam.hospital.dao;

import by.epam.hospital.entity.Procedure;

import java.util.List;
import java.util.Optional;

public interface ProceduresDao {
    int create(Procedure procedure) throws DaoException;

    Optional<Procedure> findByName(String name) throws DaoException;

    Optional<Procedure> findById(int id) throws DaoException;

    Optional<Procedure> updateCost(int id, int cost) throws DaoException;

    Optional<Procedure> updateEnabledStatus(int id, boolean isEnabled) throws DaoException;

    List<Procedure> findAllByNamePartPaging(String namePart, int page) throws DaoException;
}
