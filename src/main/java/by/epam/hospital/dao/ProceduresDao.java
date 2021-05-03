package by.epam.hospital.dao;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.PageResult;
import by.epam.hospital.entity.Procedure;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProceduresDao {
    int create(Procedure procedure) throws DaoException;

    Optional<Procedure> findByName(String name) throws DaoException;

    Optional<Procedure> findById(int id) throws DaoException;

    Optional<Procedure> updateCost(int id, int cost) throws DaoException;

    Optional<Procedure> updateEnabledStatus(int id, boolean isEnabled) throws DaoException;

    PageResult<Procedure> findAllByNamePartPaging(String namePart, int page) throws DaoException;

    boolean assignProcedureToDiagnosis(String procedureName, LocalDateTime assignDateTime, String description,
                                       String doctorLogin, String patientLogin, CardType cardType) throws DaoException;
}
