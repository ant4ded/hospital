package by.epam.hospital.service;

import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Optional<User> findPatientByUserDetails(UserDetails userDetails) throws ServiceException;

    Optional<Therapy> findCurrentPatientTherapy(String doctorLogin, String patientLogin, CardType cardType)
            throws ServiceException;

    boolean diagnoseDisease(String icdCode, String reason, String doctorLogin,
                            String patientLogin, CardType cardType) throws ServiceException;

    List<Therapy> findAllPatientTherapies(String patientLogin, CardType cardType) throws ServiceException;
}
