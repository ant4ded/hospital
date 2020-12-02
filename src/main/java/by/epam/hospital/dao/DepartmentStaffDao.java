package by.epam.hospital.dao;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;

import java.util.Map;

/**
 * A {@code DepartmentStaffDao} data access objects works with the
 * database and can be used for work with the {@link Department}
 * and {@link User} database entity.
 */

public interface DepartmentStaffDao {
    /**
     * This method add {@link Role#DOCTOR} or {@link Role#MEDICAL_ASSISTANT}
     * to entity {@code User} in database and add this entity to {@code Department}.
     *
     * @param department element of {@code Department}. In this department
     *                   {@code User} will be added.
     * @param login      {@code String} object of {@code User.login}.
     * @param role       element of {@code Role}. This role wil be added to {@code User}.
     * @return {@code true} if it was successful or false if wasn't.
     * @throws DaoException if a database access error occurs.
     */
    boolean makeMedicalWorkerAndAddToDepartment(Department department, String login, Role role)
            throws DaoException;

    /**
     * Abstract update table department_staff.
     *
     * @param department element of enum {@code Department}.
     * @param login      {@code String} value of {@code User.login}.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean updateDepartmentByLogin(Department department, String login)
            throws DaoException;

    /**
     * Delete doctor or medical assistant from department.
     *
     * @param login {@code String} object of {@code User.login}.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs or if
     *                      {@link ConnectionPool} throws
     *                      {@link ConnectionException}.
     */
    boolean deleteMedicalWorkerFromDepartment(String login) throws DaoException;

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
