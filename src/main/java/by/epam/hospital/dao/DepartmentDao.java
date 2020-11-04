package by.epam.hospital.dao;

import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;

import java.util.Map;
import java.util.Optional;

/**
 * A {@code DepartmentStaffDao} data access objects works with the
 * database and can be used for work with the {@link Department}
 * and {@link User} database entity.
 */

public interface DepartmentDao {
    /**
     * Find head of department {@code User} entity by
     * {@code Department.id} field.
     *
     * @param department {@code Department} value.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<User> findHeadDepartment(Department department) throws DaoException;

    /**
     * Update departmentHead in table departments in database.
     *
     * @param department element of enum {@code Department}.
     * @param login      {@code String} value of {@code User.login}.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs.
     */
    boolean updateDepartmentHead(Department department, String login) throws DaoException;

    /**
     * Find {@code Department} entity by {@code User.login} field.
     *
     * @param login {@code String} value of {@code User.login}.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<Department> findDepartment(String login) throws DaoException;

    /**
     * Find department heads.
     *
     * @return {@code Map<Department, String>} if it present
     * or an empty {@code Map} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Map
     */
    Map<Department, String> findDepartmentsHeads() throws DaoException;
}
