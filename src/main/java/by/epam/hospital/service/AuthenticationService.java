package by.epam.hospital.service;

import by.epam.hospital.entity.Role;

public interface AuthenticationService {
    boolean isHasRole(String login, Role role) throws ServiceException;
}
