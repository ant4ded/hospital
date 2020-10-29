package by.epam.hospital.service;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Diagnosis;
import by.epam.hospital.entity.User;

import java.sql.Date;
import java.util.Optional;

public interface DoctorService {
    Optional<User> findByRegistrationData(String firstName, String surname, String lastName, Date birthday)
            throws ServiceException;

    boolean diagnoseDisease(Diagnosis diagnosis, String patientLogin, CardType cardType)
            throws ServiceException;
}
