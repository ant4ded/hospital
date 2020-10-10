package by.epam.hospital.service;

import by.epam.hospital.entity.User;

public interface ClientService {
    User authorization(String login, String password) throws ServiceException;

}
