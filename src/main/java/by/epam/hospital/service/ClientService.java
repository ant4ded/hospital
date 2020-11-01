package by.epam.hospital.service;

import by.epam.hospital.entity.User;

import java.util.Optional;

public interface ClientService {
    Optional<User> authorization(String login, String password) throws ServiceException;

}
