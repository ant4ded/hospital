package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdminHeadServiceImpl implements AdminHeadService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public Map<String, Role> findUserRoles(String login) throws ServiceException {
        Map<String, Role> roles = new HashMap<>();
        try {
            Optional<User> user = userDao.find(login);
            if (user.isPresent()) {
                roles = user.get().getRoles();
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return roles;
    }

    @Override
    public boolean updateUserRoles(String login, String action, Role role) throws ServiceException {
        boolean result = false;
        try {
            if (userDao.find(login).isPresent()) {
                userDao.updateUserRole(login, action, role);
                result = true;
            }
        } catch (DaoException e) {
            throw new ServiceException("Can not update table users_roles");
        }
        return result;
    }
}
