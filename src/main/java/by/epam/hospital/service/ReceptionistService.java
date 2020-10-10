package by.epam.hospital.service;

import by.epam.hospital.entity.User;

public interface ReceptionistService {
    boolean registerClient(User user) throws ServiceException;
}
