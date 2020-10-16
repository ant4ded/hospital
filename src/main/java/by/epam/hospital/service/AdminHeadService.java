package by.epam.hospital.service;

import by.epam.hospital.entity.Role;

import java.util.Map;

public interface AdminHeadService {
    Map<String, Role> findUserRoles(String login) throws ServiceException;

    boolean updateUserRoles(String login, String action, Role role) throws ServiceException;
}
