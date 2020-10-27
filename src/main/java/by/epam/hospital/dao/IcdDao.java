package by.epam.hospital.dao;

import by.epam.hospital.entity.Icd;

import java.util.Optional;

public interface IcdDao {
    Optional<Icd> findByCode(String code) throws DaoException;

    Icd findById(int id) throws DaoException;
}
