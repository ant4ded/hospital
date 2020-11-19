package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersFieldName;

import java.sql.CallableStatement;
import java.sql.Connection;
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
     * Sql {@code String} object for call stored procedure {@code FindDepartmentHeadByDepartmentId}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_DEPARTMENT_HEAD_BY_DEPARTMENT_ID =
            "CALL FindDepartmentHeadByDepartmentId(?)";
    /**
     * Sql {@code String} object for call stored procedure {@code UpdateDepartmentHeadByDepartmentId}.
     * Written for the MySQL dialect.
     */
    private static final String SP_UPDATE_DEPARTMENT_HEAD_BY_DEPARTMENT_ID =
            "CALL UpdateDepartmentHeadByDepartmentId(?,?)";
    /**
     * Sql {@code String} object for call stored procedure {@code FindDepartmentByUserLogin}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_DEPARTMENT_BY_USER_LOGIN =
            "CALL FindDepartmentByUserLogin(?)";

    /**
     * {@link UserDao} data access object.
     */
    private final UserDao userDao = new UserDaoImpl();

    /**
     * Find head of department {@code User} entity by {@code Department.id} field.
     *
     * @param department {@code Department} value.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<User> findHeadDepartment(Department department) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_DEPARTMENT_HEAD_BY_DEPARTMENT_ID);
            statement.setInt(1, department.id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(UsersFieldName.ID));
                user.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                user.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                user.setRoles(userDao.findUserRoles(user.getLogin()));
                optionalUser = Optional.of(user);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find department head failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalUser;
    }

    /**
     * Update departmentHead in table departments.
     *
     * @param department element of enum {@code Department}.
     * @param login      {@code String} value of {@code User.login}.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see ConnectionException
     */
    @Override
    public boolean updateDepartmentHead(Department department, String login) throws DaoException {
        boolean result;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_UPDATE_DEPARTMENT_HEAD_BY_DEPARTMENT_ID);
            statement.setInt(1, department.id);
            statement.setString(2, login);

            resultSet = statement.executeQuery();
            result = resultSet.next() && resultSet.getInt(1) != 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Update department failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    /**
     * Find {@code Department} entity by {@code User.login} field.
     *
     * @param login {@code String} value of {@code User.login}.
     * @return {@code Optional<Diagnosis>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<Department> findDepartment(String login) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<Department> optionalDepartment;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_DEPARTMENT_BY_USER_LOGIN);
            statement.setString(1, login);

            resultSet = statement.executeQuery();
            optionalDepartment = resultSet.next() ?
                    Optional.of(Department.valueOf(resultSet.getString(1))) :
                    Optional.empty();
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
