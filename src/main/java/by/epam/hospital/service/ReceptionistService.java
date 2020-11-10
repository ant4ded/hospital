package by.epam.hospital.service;

import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;

import java.util.Optional;

public interface ReceptionistService {
    boolean registerClient(User user) throws ServiceException;

    Optional<User> findUserCredentials(UserDetails userDetails) throws ServiceException;
}
