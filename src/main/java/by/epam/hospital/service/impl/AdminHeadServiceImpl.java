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
import by.epam.hospital.service.ServiceAction;
import by.epam.hospital.service.ServiceException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class AdminHeadServiceImpl implements AdminHeadService {
    private final UserDao userDao = new UserDaoImpl();
    private final DepartmentDao departmentDao = new DepartmentDaoImpl();
    private final DepartmentStaffDao departmentStaffDao = new DepartmentStaffDaoImpl();

    @Override
    public ArrayList<Role> findUserRoles(String login) throws ServiceException {
        ArrayList<Role> roles = new ArrayList<>();
        try {
            Optional<User> user = userDao.findByLogin(login);
            if (user.isPresent()) {
                roles = user.get().getRoles();
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return roles;
    }

    @Override
    public void performUserRolesAction(String login, ServiceAction serviceAction, Role role) throws ServiceException {
        try {
            if (userDao.findByLogin(login).isPresent()) {
                userDao.updateUserRoles(login, serviceAction, role);
            }
        } catch (DaoException e) {
            throw new ServiceException("Can not update table users_roles");
        }
    }

    @Override
    public boolean appointDepartmentHead(Department department, String login) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> userFromDb = userDao.findByLogin(login);
            if (userFromDb.isPresent() &&
                    userFromDb.get().getRoles().contains(Role.DOCTOR)) {
                Optional<User> previous = departmentDao.findHeadDepartment(department);
                if (!userFromDb.equals(previous)) {
                    if (previous.isPresent()) {
                        performUserRolesAction(previous.get().getLogin(), ServiceAction.REMOVE, Role.DEPARTMENT_HEAD);
                    }
                    departmentDao.updateDepartmentHead(department, login);
                    performUserRolesAction(login, ServiceAction.ADD, Role.DEPARTMENT_HEAD);
                    result = true;
                }
            }
        } catch (DaoException e) {
            throw new ServiceException("Can update head of department");
        }
        return result;
    }

    @Override
    public boolean performDepartmentStaffAction(Department department, ServiceAction serviceAction, String login)
            throws ServiceException {
        boolean result = false;
        try {
            if ((departmentDao.findDepartment(login) == null && serviceAction.equals(ServiceAction.ADD)) ||
                    serviceAction.equals(ServiceAction.REMOVE)) {
                Optional<User> userFromDb = userDao.findByLogin(login);
                if (userFromDb.isPresent() && !userFromDb.get().getRoles().contains(Role.DEPARTMENT_HEAD)) {
                    departmentStaffDao.updateStaffDepartment(department, serviceAction, login);
                    result = true;
                }
            }
            if (departmentDao.findDepartment(login) != null && serviceAction.equals(ServiceAction.ADD)) {
                Department previous = findDepartmentByUsername(login);
                Optional<User> userFromDb = userDao.findByLogin(login);
                if (userFromDb.isPresent() && !userFromDb.get().getRoles().contains(Role.DEPARTMENT_HEAD)) {
                    departmentStaffDao.updateStaffDepartment(previous, ServiceAction.REMOVE, login);
                    departmentStaffDao.updateStaffDepartment(department, serviceAction, login);
                    result = true;
                }
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

    @Override
    public Map<Department, String> findDepartmentsHeads() throws ServiceException {
        Map<Department, String> departmentHeadMap;
        try {
            departmentHeadMap = departmentDao.findDepartmentsHeads();
        } catch (DaoException e) {
            throw new ServiceException("Can not find departments heads", e);
        }
        return departmentHeadMap;
    }
}
