package by.epam.hospital.dao;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;
import by.epam.hospital.service.ServiceAction;

import java.util.Map;

/**
 * A {@code DepartmentStaffDao} data access objects works with the
 * database and can be used for work with the {@link Department}
 * and {@link User} database entity.
 */

public interface DepartmentStaffDao {
    /**
     * Abstract update table department_staff.
     *
     * @param department    element of enum {@code Department}.
     * @param serviceAction element of enum {@link ServiceAction}
     *                      action is selected based on this element.
     * @param login         {@code String} value of {@code User.login}.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean updateStaffDepartment(Department department, ServiceAction serviceAction, String login) throws DaoException;

    /**
     * Find department staff.
     *
     * @param department element of enum {@code Department}.
     * @return {@code Map<String, User>} if it present
     * or an empty {@code Map} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Map
     */
    Map<String, User> findDepartmentStaff(Department department) throws DaoException;
}
