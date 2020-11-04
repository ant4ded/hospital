package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@code DepartmentDaoImpl} implementation of {@link DepartmentDao}.
 * Implements all required methods for work with the
 * {@link Department} and {@link User} database entity.
 * <p>
 * All methods get connection from {@code ConnectionPool}
 * and it is object of type {@code ProxyConnection}. It is a wrapper of really
 * {@code Connection}, which different only in methods {@code close}
 * and {@code reallyClose}.
 *
 * @see ConnectionPool
 * @see by.epam.hospital.connection.ProxyConnection
 * @see Connection
 */

public class DepartmentDaoImpl implements DepartmentDao {
    /**
     * Sql {@code String} object for find department_head_id in
     * departments table entity by {@code Department.id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_DEPARTMENT_HEAD = """
            SELECT department_head_id
            FROM departments
            WHERE id = ?""";
    /**
     * Sql {@code String} object for update department_head_id in
     * departments table entity by {@code Department.id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_UPDATE_DEPARTMENT_HEAD = """
            UPDATE departments
            SET department_head_id = ?
            WHERE id = ?""";
    /**
     * Sql {@code String} object for find title in
     * departments table entity by {@code Department.id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_DEPARTMENT_BY_USERNAME = """
            SELECT title
            FROM departments
            INNER JOIN departments_staff ds on departments.id = ds.department_id
            INNER JOIN users u on ds.doctor_id = u.id
            WHERE u.login = ?""";

    /**
     * {@link UserDao} data access object.
     */
    private final UserDao userDao = new UserDaoImpl();

    /**
     * Find head of department {@code User} entity by {@code Department.id} field
     * in database using {@code PreparedStatement}.
     *
     * @param department {@code Department} value.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<User> findHeadDepartment(Department department) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> departmentHead;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_DEPARTMENT_HEAD);
            statement.setInt(1, department.id);
            statement.execute();

            resultSet = statement.getResultSet();
            departmentHead = resultSet.next() ? userDao.findById(resultSet.getInt(1)) : Optional.empty();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find department head failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return departmentHead;
    }

    /**
     * Update departmentHead in table departments
     * in database using {@code PreparedStatement}.
     *
     * @param department element of enum {@code Department}.
     * @param login      {@code String} value of {@code User.login}.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     */
    @Override
    public boolean updateDepartmentHead(Department department, String login) throws DaoException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement statement = null;
        Optional<User> user = userDao.findByLogin(login);
        try {
            if (user.isPresent()) {
                connection = ConnectionPool.getInstance().getConnection();
                statement = connection.prepareStatement(SQL_UPDATE_DEPARTMENT_HEAD);
                statement.setInt(1, user.get().getId());
                statement.setInt(2, department.id);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 1) {
                    result = true;
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Update department failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return result;
    }

    /**
     * Find {@code Department} entity by {@code User.login} field
     * in database using {@code PreparedStatement}.
     *
     * @param login {@code String} value of {@code User.login}.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<Department> findDepartment(String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<Department> optionalDepartment;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_DEPARTMENT_BY_USERNAME);
            statement.setString(1, login);
            statement.execute();
            resultSet = statement.getResultSet();
            optionalDepartment = resultSet.next() ?
                    Optional.of(Department.valueOf(resultSet.getString(1))) : Optional.empty();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find department failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalDepartment;
    }

    /**
     * Find department heads.
     *
     * @return {@code Map<Department, String>} being a
     * {@code HashMap<Department, String>} object if it present
     * or an empty {@code Map} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Map
     * @see HashMap
     */
    @Override
    public Map<Department, String> findDepartmentsHeads() throws DaoException {
        Map<Department, String> departmentHeadMap = new HashMap<>();
        departmentHeadMap.put(Department.INFECTIOUS,
                findHeadDepartment(Department.INFECTIOUS).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.CARDIOLOGY,
                findHeadDepartment(Department.CARDIOLOGY).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.NEUROLOGY,
                findHeadDepartment(Department.NEUROLOGY).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.OTORHINOLARYNGOLOGY,
                findHeadDepartment(Department.OTORHINOLARYNGOLOGY).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.PEDIATRIC,
                findHeadDepartment(Department.PEDIATRIC).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.THERAPEUTIC,
                findHeadDepartment(Department.THERAPEUTIC).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.UROLOGY,
                findHeadDepartment(Department.UROLOGY).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.TRAUMATOLOGY,
                findHeadDepartment(Department.TRAUMATOLOGY).orElseThrow(DaoException::new).getLogin());
        departmentHeadMap.put(Department.SURGERY,
                findHeadDepartment(Department.SURGERY).orElseThrow(DaoException::new).getLogin());
        return departmentHeadMap;
    }
}
