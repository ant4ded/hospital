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
        ArrayList<Role> roles;
        try {
            Optional<User> user = userDao.findByLogin(login);
            if (user.isEmpty()) {
                throw new ServiceException("FindUserRoles failed. User not existing.");
            }
            roles = user.get().getRoles();
        } catch (DaoException e) {
            throw new ServiceException("FindUserRoles failed.", e);
        }
        return roles;
    }

    @Override
    public void performUserRolesAction(String login, ServiceAction serviceAction, Role role) throws ServiceException {
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isEmpty()) {
                throw new ServiceException("PerformUserRoles failed. User not existing.");
            }
            if ((optionalUser.get().getRoles().contains(role) && serviceAction.equals(ServiceAction.REMOVE)) ||
                    !optionalUser.get().getRoles().contains(role) && serviceAction.equals(ServiceAction.ADD) ) {
                userDao.updateUserRoles(login, serviceAction, role);
            }
        } catch (DaoException e) {
            throw new ServiceException("PerformUserRoles failed.", e);
        }
    }

    @Override
    public void appointDepartmentHead(Department department, String login) throws ServiceException {
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isEmpty()) {
                throw new ServiceException("AppointDepartmentHead failed. User not existing.");
            }
            if (!optionalUser.get().getRoles().contains(Role.DOCTOR)) {
                throw new ServiceException("AppointDepartmentHead failed. User is not a doctor.");
            }
            Optional<User> previous = departmentDao.findHeadDepartment(department);
            if (!optionalUser.equals(previous)) {
                if (previous.isPresent()) {
                    performUserRolesAction(previous.get().getLogin(), ServiceAction.REMOVE, Role.DEPARTMENT_HEAD);
                }
                departmentDao.updateDepartmentHead(department, login);
                performUserRolesAction(login, ServiceAction.ADD, Role.DEPARTMENT_HEAD);
            }
        } catch (DaoException e) {
            throw new ServiceException("AppointDepartmentHead failed.", e);
        }
    }

    @Override
    public void performDepartmentStaffAction(Department department, ServiceAction serviceAction,
                                             String login, Role role) throws ServiceException {
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isEmpty()) {
                throw new ServiceException("PerformDepartmentStaff failed. User not existing.");
            }
            if (optionalUser.get().getRoles().contains(Role.DEPARTMENT_HEAD)) {
                throw new ServiceException("PerformDepartmentStaff failed. " +
                        "Try to appoint the head of another department as the head of a department.");
            }
            if (!role.equals(Role.MEDICAL_ASSISTANT) && !role.equals(Role.DOCTOR)) {
                throw new ServiceException("PerformDepartmentStaff failed. " +
                        "Role is not a role doctor or medical assistant.");
            }
            performUserRolesAction(login, serviceAction, role);
            if ((departmentDao.findDepartment(login) != null || !serviceAction.equals(ServiceAction.ADD)) &&
                    !serviceAction.equals(ServiceAction.REMOVE)) {
                Department previous = findDepartmentByUsername(login);
                departmentStaffDao.updateStaffDepartment(previous, ServiceAction.REMOVE, login);
            }
            departmentStaffDao.updateStaffDepartment(department, serviceAction, login);
        } catch (DaoException e) {
            throw new ServiceException("PerformDepartmentStaff failed.", e);
        }
    }

    @Override
    public Department findDepartmentByUsername(String login) throws ServiceException {
        Department department;
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isEmpty()) {
                throw new ServiceException("FindDepartmentByUsername failed. User not existing.");
            }
            if (optionalUser.get().getRoles().contains(Role.DOCTOR) ||
                    optionalUser.get().getRoles().contains(Role.MEDICAL_ASSISTANT)) {
                department = departmentDao.findDepartment(login);
            } else {
                throw new ServiceException("FindDepartmentByUsername failed. User is not a doctor.");
            }
        } catch (DaoException e) {
            throw new ServiceException("FindDepartmentByUsername failed.", e);
        }
        return department;
    }

    @Override
    public Map<Department, String> findDepartmentsHeads() throws ServiceException {
        Map<Department, String> departmentHeadMap;
        try {
            departmentHeadMap = departmentDao.findDepartmentsHeads();
        } catch (DaoException e) {
            throw new ServiceException("Can not find departments heads.", e);
        }
        return departmentHeadMap;
    }

    @Override
    public void roleControl(String login, Department department, ServiceAction serviceAction, Role role)
            throws ServiceException {
        if (role != Role.DOCTOR && role != Role.MEDICAL_ASSISTANT && role != Role.DEPARTMENT_HEAD) {
            performUserRolesAction(login, serviceAction, role);
        }
        if (role == Role.DEPARTMENT_HEAD) {
            appointDepartmentHead(department, login);
        }
        if (role == Role.DOCTOR || role == Role.MEDICAL_ASSISTANT) {
            performDepartmentStaffAction(department, serviceAction, login, role);
//            performUserRolesAction(login, serviceAction, role);
        }
    }
}
