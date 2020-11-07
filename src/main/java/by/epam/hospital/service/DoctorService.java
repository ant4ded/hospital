package by.epam.hospital.service;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.User;

import java.sql.Date;
import java.util.Optional;

public interface DoctorService {
    Optional<User> findPatientByRegistrationData(String firstName, String surname, String lastName, Date birthday)
            throws ServiceException;

    boolean diagnoseDisease(String icdCode, String reason, String doctorLogin,
                            String patientLogin, CardType cardType) throws ServiceException;
}
