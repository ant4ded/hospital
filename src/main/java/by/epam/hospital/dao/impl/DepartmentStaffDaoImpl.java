package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersFieldName;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code DepartmentStaffDaoImpl} implementation of {@link DepartmentStaffDao}.
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

public class DepartmentStaffDaoImpl implements DepartmentStaffDao {
    /**
     * Sql {@code String} object for call stored procedure {@code MakeMedicalWorkerAndAddToDepartment}.
     * Written for the MySQL dialect.
     */
    private static final String SP_MAKE_MEDICAL_WORKER_AND_ADD_TO_DEPARTMENT =
            "CALL MakeMedicalWorkerAndAddToDepartment(?,?,?)";
    /**
     * Sql {@code String} object for call stored procedure {@code MakeMedicalWorkerAndAddToDepartment}.
     * Written for the MySQL dialect.
     */
    private static final String SP_UPDATE_DEPARTMENT_BY_LOGIN =
            "CALL UpdateDepartmentByLogin(?,?)";
    /**
     * Sql {@code String} object for find row in department_staff table
     * entity by {@code Department.id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_DEPARTMENT_STAFF = """
            SELECT users.id, users.login, users.password
            FROM users
            INNER JOIN departments_staff ds on users.id = ds.doctor_id
            WHERE department_id = ?""";

    /**
     * {@link UserDao} data access object.
     */
    private final UserDao userDao = new UserDaoImpl();

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
    @Override
    public boolean makeMedicalWorkerAndAddToDepartment(Department department, String login, Role role)
            throws DaoException {
        boolean result;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_MAKE_MEDICAL_WORKER_AND_ADD_TO_DEPARTMENT);
            statement.setInt(1, department.id);
            statement.setString(2, login);
            statement.setString(3, role.name());

            resultSet = statement.executeQuery();
            result = resultSet.next() && resultSet.getInt(1) != 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating department staff failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    /**
     * Abstract update table department_staff
     * in database using {@code PreparedStatement}.
     *
     * @param department element of enum {@code Department}.
     * @return {@code true} if it was successful or {@code false} if not.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see ConnectionException
     */
    @Override
    public boolean updateDepartmentByLogin(Department department, String login)
            throws DaoException {
        boolean result;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_UPDATE_DEPARTMENT_BY_LOGIN);
            statement.setInt(1, department.id);
            statement.setString(2, login);

            resultSet = statement.executeQuery();
            result = resultSet.next() && resultSet.getInt(1) != 0;
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating department staff failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    /**
     * Find department staff in database using {@code PreparedStatement}.
     *
     * @param department element of enum {@code Department}.
     * @return {@code Map<String, User>} being a
     * {@code HashMap<String, User>} object if it present
     * or an empty {@code Map} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see Map
     * @see HashMap
     * @see ConnectionException
     */
    @Override
    public Map<String, User> findDepartmentStaff(Department department) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<String, User> userMap = new HashMap<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_DEPARTMENT_STAFF);
            statement.setInt(1, department.id);
            statement.execute();

            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(UsersFieldName.ID));
                user.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                user.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                user.setRoles(userDao.findByLogin(user.getLogin()).orElseThrow(DaoException::new).getRoles());
                userMap.put(user.getLogin(), user);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find department staff failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return userMap;
    }
}
