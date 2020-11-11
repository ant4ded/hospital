package by.epam.hospital.service.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
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
    private final UserDao userDao;
    private final DepartmentDao departmentDao;
    private final DepartmentStaffDao departmentStaffDao;

    public AdminHeadServiceImpl(UserDao userDao, DepartmentDao departmentDao, DepartmentStaffDao departmentStaffDao) {
        this.userDao = userDao;
        this.departmentDao = departmentDao;
        this.departmentStaffDao = departmentStaffDao;
    }

    @Override
    public ArrayList<Role> findUserRoles(String login) throws ServiceException {
        ArrayList<Role> roles = new ArrayList<>();
        try {
            Optional<User> user = userDao.findByLogin(login);
            if (user.isPresent()) {
                roles = user.get().getRoles();
            }
        } catch (DaoException e) {
            throw new ServiceException("FindUserRoles failed.", e);
        }
        return roles;
    }

    @Override
    public boolean performUserRolesAction(String login, ServiceAction serviceAction, Role role)
            throws ServiceException {
        boolean result = false;
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            if (optionalUser.isPresent() && ((optionalUser.get().getRoles().contains(role)
                    && serviceAction == ServiceAction.REMOVE) ||
                    !optionalUser.get().getRoles().contains(role) && serviceAction == ServiceAction.ADD)) {
                result = userDao.updateUserRoles(login, serviceAction, role);
            }
        } catch (DaoException e) {
            throw new ServiceException("PerformUserRoles failed.", e);
        }
        return result;
    }

    @Override
    public boolean appointDepartmentHead(Department department, String login) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> newHead = userDao.findByLogin(login); 
            Optional<User> previousHead = departmentDao.findHeadDepartment(department); 
            Optional<Department> departmentOfNewHead = findDepartmentByUsername(login);  
            boolean isNonEqualsAndNewHeadIsDoctor = newHead.isPresent() && !newHead.equals(previousHead) &&
                    newHead.get().getRoles().contains(Role.DOCTOR);
            boolean isDepartmentNewHeadEqualsThisDepartment = departmentOfNewHead.isPresent() &&
                    departmentOfNewHead.get().equals(department);
            if (isNonEqualsAndNewHeadIsDoctor && isDepartmentNewHeadEqualsThisDepartment) {
                if (previousHead.isPresent()) {
                    performUserRolesAction(previousHead.get().getLogin(), ServiceAction.REMOVE, Role.DEPARTMENT_HEAD);  
                }
                departmentDao.updateDepartmentHead(department, login); 
                result = performUserRolesAction(login, ServiceAction.ADD, Role.DEPARTMENT_HEAD);  
            }
        } catch (DaoException e) {
            throw new ServiceException("AppointDepartmentHead failed.", e);
        }
        return result;
    }

    @Override
    public boolean performDepartmentStaffAction(Department department, ServiceAction serviceAction,
                                                String login, Role role) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            boolean isNotDepartmentHead =
                    optionalUser.isPresent() && !optionalUser.get().getRoles().contains(Role.DEPARTMENT_HEAD);
            boolean isUserFutureDoctorOrMedicalAssistant =
                    role.equals(Role.MEDICAL_ASSISTANT) || role.equals(Role.DOCTOR);
            Optional<Department> previousDepartment = departmentDao.findDepartment(login);
            boolean isUserHaveDepartmentAndActionAdd = previousDepartment.isPresent() &&
                    serviceAction == ServiceAction.ADD;
            if (isNotDepartmentHead && isUserFutureDoctorOrMedicalAssistant) {
                performUserRolesAction(login, serviceAction, role);
                if (isUserHaveDepartmentAndActionAdd) {
                    departmentStaffDao.updateStaffDepartment(previousDepartment.get(), ServiceAction.REMOVE, login);
                }
                result = departmentStaffDao.updateStaffDepartment(department, serviceAction, login);
            }
        } catch (DaoException e) {
            throw new ServiceException("PerformDepartmentStaff failed.", e);
        }
        return result;
    }

    @Override
    public Optional<Department> findDepartmentByUsername(String login) throws ServiceException {
        Optional<Department> optionalDepartment = Optional.empty();
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            boolean isUserMedicalWorker = optionalUser.isPresent() &&
                    (optionalUser.get().getRoles().contains(Role.DOCTOR) ||
                    optionalUser.get().getRoles().contains(Role.MEDICAL_ASSISTANT));
            if (isUserMedicalWorker) {
                optionalDepartment = departmentDao.findDepartment(login);
            }
        } catch (DaoException e) {
            throw new ServiceException("FindDepartmentByUsername failed.", e);
        }
        return optionalDepartment;
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
}
