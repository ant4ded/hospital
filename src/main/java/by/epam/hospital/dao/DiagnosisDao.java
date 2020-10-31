package by.epam.hospital.dao;

import by.epam.hospital.entity.Diagnosis;

import java.util.List;
import java.util.Optional;

public interface DiagnosisDao {
    int create(Diagnosis diagnosis, String patientLogin, int therapyId) throws DaoException;

    List<Diagnosis> findAllByTherapyId(int id) throws DaoException;

    Optional<Diagnosis> findById(int id) throws DaoException;
}
