package by.epam.hospital.dao;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;

import java.util.Optional;

public interface TherapyDao {
    void create(String doctorLogin, String patientLogin, CardType cardType) throws DaoException;

    Optional<Therapy> find(String doctorLogin, String patientLogin, CardType cardType) throws DaoException;

    Optional<Therapy> findById(int id, CardType cardType) throws DaoException;
}
