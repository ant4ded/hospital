package by.epam.hospital.service;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Medicament;
import by.epam.hospital.entity.Procedure;
import by.epam.hospital.entity.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminHeadService {
    ArrayList<Role> findUserRoles(String login) throws ServiceException;

    boolean updateUserRoles(String login, ServiceAction serviceAction, Role role) throws ServiceException;

    boolean appointDepartmentHead(Department department, String login) throws ServiceException;

    boolean updateDepartmentStaff(Department department, ServiceAction serviceAction,
                                  String login, Role appointedRole) throws ServiceException;

    Optional<Department> findDepartmentByUsername(String login) throws ServiceException;

    Map<Department, String> findDepartmentsHeads() throws ServiceException;

    boolean createProcedureOrMedicament(Object o, Class<?> type) throws ServiceException;

    boolean updateEnabledStatusOnProcedureOrMedicament(Object o, boolean isEnabled, Class<?> type) throws ServiceException;

    boolean updateProcedureCost(Procedure procedure, int cost) throws ServiceException;

    List<Procedure> findAllProceduresByNamePartPaging(String namePart, int page) throws ServiceException;

    List<Medicament> findAllMedicationsByNamePartPaging(String namePart, int page) throws ServiceException;
}
