package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentDaoImpl;
import by.epam.hospital.dao.impl.DepartmentStaffDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.util.Action;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdminHeadServiceImpl implements AdminHeadService {
    private final UserDao userDao = new UserDaoImpl();
    private final DepartmentDao departmentDao = new DepartmentDaoImpl();
    private final DepartmentStaffDao departmentStaffDao = new DepartmentStaffDaoImpl();

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
    public boolean performUserRolesAction(String login, Action action, Role role) throws ServiceException {
        boolean result = false;
        try {
            if (userDao.find(login).isPresent()) {
                userDao.updateUserRoles(login, action, role);
                result = true;
            }
        } catch (DaoException e) {
            throw new ServiceException("Can not update table users_roles");
        }
        return result;
    }

    @Override
    public boolean appointDepartmentHead(Department department, String login) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> userFromDb = userDao.find(login);
            if (userFromDb.isPresent() &&
                    userFromDb.get().getRoles().containsValue(Role.DOCTOR)) {
                Optional<User> previous = departmentDao.findHeadDepartment(department);
                if (!userFromDb.equals(previous)) {
                    if (previous.isPresent()) {
                        performUserRolesAction(previous.get().getLogin(), Action.REMOVE, Role.DEPARTMENT_HEAD);
                    }
                    departmentDao.updateDepartmentHead(department, login);
                    result = true;
                }
            }
        } catch (DaoException e) {
            throw new ServiceException("Can update head of department");
        }
        return result;
    }

    @Override
    public boolean performDepartmentStaffAction(Department department, Action action, String login) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> userFromDb = userDao.find(login);
            if (userFromDb.isPresent() && !userFromDb.get().getRoles().containsValue(Role.DEPARTMENT_HEAD)) {
                departmentStaffDao.updateStaffDepartment(department, action, login);
                result = true;
            }
        } catch (DaoException e) {
            throw new ServiceException("Can update head of department");
        }
        return result;
    }

    @Override
    public Department findDepartmentByUsername(String login) throws ServiceException {
        Department department;
        try {
            department = departmentDao.findDepartment(login);
        } catch (DaoException e) {
            throw new ServiceException("Can not find department", e);
        }
        return department;
    }
}
