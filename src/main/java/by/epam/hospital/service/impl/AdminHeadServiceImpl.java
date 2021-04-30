package by.epam.hospital.service.impl;

import by.epam.hospital.dao.*;
import by.epam.hospital.entity.*;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ServiceAction;
import by.epam.hospital.service.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminHeadServiceImpl implements AdminHeadService {
    private static final int PAGING_SIZE = 10;

    private final UserDao userDao;
    private final DepartmentDao departmentDao;
    private final DepartmentStaffDao departmentStaffDao;
    private final ProceduresDao procedureDao;
    private final MedicamentDao medicamentDao;

    public AdminHeadServiceImpl(UserDao userDao,
                                DepartmentDao departmentDao,
                                DepartmentStaffDao departmentStaffDao,
                                ProceduresDao procedureDao,
                                MedicamentDao medicamentDao) {
        this.userDao = userDao;
        this.departmentDao = departmentDao;
        this.departmentStaffDao = departmentStaffDao;
        this.procedureDao = procedureDao;
        this.medicamentDao = medicamentDao;
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
    public boolean updateUserRoles(String login, ServiceAction serviceAction, Role role)
            throws ServiceException {
        boolean result = false;
        try {
            if (role != Role.CLIENT) {
                Optional<User> optionalUser = userDao.findByLogin(login);
                boolean isActionDeleteAndUserContainsRole = optionalUser.isPresent() &&
                        optionalUser.get().getRoles().contains(role) &&
                        serviceAction == ServiceAction.DELETE;
                boolean isActionAddAndUserNotContainsRole = optionalUser.isPresent() &&
                        !optionalUser.get().getRoles().contains(role) &&
                        serviceAction == ServiceAction.ADD;
                if (isActionDeleteAndUserContainsRole) {
                    result = userDao.deleteUserRole(login, role);
                }
                if (isActionAddAndUserNotContainsRole) {
                    result = userDao.addUserRole(login, role);
                }
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
                result = departmentDao.updateDepartmentHead(department, login);
            }
        } catch (DaoException e) {
            throw new ServiceException("AppointDepartmentHead failed.", e);
        }
        return result;
    }

    @Override
    public boolean updateDepartmentStaff(Department department, ServiceAction serviceAction,
                                         String login, Role role) throws ServiceException {
        boolean result = false;
        try {
            Optional<User> optionalUser = userDao.findByLogin(login);
            Optional<Department> previousDepartment = departmentDao.findDepartment(login);
            boolean isNotDepartmentHead = optionalUser.isPresent() &&
                    !optionalUser.get().getRoles().contains(Role.DEPARTMENT_HEAD);
            boolean isUserFutureDoctorOrMedicalAssistant = role.equals(Role.MEDICAL_ASSISTANT) ||
                    role.equals(Role.DOCTOR);
            boolean isUserDoesNotHaveDepartment = previousDepartment.isEmpty();
            if (isNotDepartmentHead && isUserFutureDoctorOrMedicalAssistant) {
                if (serviceAction == ServiceAction.ADD) {
                    result = isUserDoesNotHaveDepartment ?
                            departmentStaffDao.makeMedicalWorkerAndAddToDepartment(department, login, role) :
                            departmentStaffDao.updateDepartmentByLogin(department, login);
                } else {
                    result = departmentStaffDao.deleteMedicalWorkerFromDepartment(login);
                }
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

    @Override
    public boolean createProcedureOrMedicament(Object o, Class<?> type) throws ServiceException {
        int id = 0;
        try {
            id = type == Procedure.class ? procedureDao.create((Procedure) o) : id;
            id = type == Medicament.class ? medicamentDao.create((Medicament) o) : id;
        } catch (DaoException e) {
            throw new ServiceException("Can not create " + type.getSimpleName() + ".", e);
        }
        return id > 0;
    }

    @Override
    public boolean updateEnabledStatusOnProcedureOrMedicament(Object o, boolean isEnabled, Class<?> type)
            throws ServiceException {
        AtomicInteger idEntityForUpdate = new AtomicInteger();
        boolean result = false;
        try {
            if (type == Procedure.class) {
                Optional<Procedure> optionalFromDb = procedureDao.findByName(((Procedure) o).getName());
                optionalFromDb.ifPresent(fromDb -> idEntityForUpdate.set(fromDb.getId()));
                Optional<Procedure> optionalUpdated = procedureDao.updateEnabledStatus(idEntityForUpdate.get(), isEnabled);

                if (optionalUpdated.isPresent() && optionalFromDb.isPresent()) {
                    result = optionalFromDb.get().isEnabled() != optionalUpdated.get().isEnabled();
                }
            }
            if (type == Medicament.class) {
                Optional<Medicament> optionalFromDb = medicamentDao.findByName(((Medicament) o).getName());
                optionalFromDb.ifPresent(fromDb -> idEntityForUpdate.set(fromDb.getId()));
                Optional<Medicament> optionalUpdated = medicamentDao.updateEnabledStatus(idEntityForUpdate.get(), isEnabled);

                if (optionalUpdated.isPresent() && optionalFromDb.isPresent()) {
                    result = optionalFromDb.get().isEnabled() != optionalUpdated.get().isEnabled();
                }
            }
        } catch (DaoException e) {
            throw new ServiceException("Can not update enabled status on " + type.getSimpleName() + ".", e);
        }
        return result;
    }

    @Override
    public boolean updateProcedureCost(Procedure procedure, int cost) throws ServiceException {
        AtomicInteger id = new AtomicInteger();
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            procedureDao.findByName(procedure.getName()).ifPresent(procedureFromDb -> id.set(procedureFromDb.getId()));
            procedureDao.updateCost(id.get(), cost)
                    .ifPresent(updatedProcedure -> result.set(updatedProcedure.getCost() == cost));
        } catch (DaoException e) {
            throw new ServiceException("Can not update procedure cost.", e);
        }
        return result.get();
    }

    @Override
    public List<Procedure> findAllProceduresByNamePartPaging(String namePart, int page) throws ServiceException {
        if (page < 1) {
            throw new ServiceException("Can not find procedures, page less than one.");
        }

        List<Procedure> procedures;
        try {
            procedures = procedureDao.findAllByNamePartPaging(namePart, page);
        } catch (DaoException e) {
            throw new ServiceException("Can not find procedures, something wrong.", e);
        }
        return procedures;
    }

    @Override
    public List<Medicament> findAllMedicationsByNamePartPaging(String namePart, int page) throws ServiceException {
        if (page < 1) {
            throw new ServiceException("Can not find procedures, page less than one.");
        }

        List<Medicament> medications;
        try {
            medications = medicamentDao.findAllByNamePartPaging(namePart, page);
        } catch (DaoException e) {
            throw new ServiceException("Can not find medications, something wrong.", e);
        }
        return medications;
    }
}
