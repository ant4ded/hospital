package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ServiceAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DepartmentStaffDaoImpl implements DepartmentStaffDao {
    private static final String SQL_DELETE_DEPARTMENTS_STAFF_ROW =
            "DELETE departments_staff FROM departments_staff " +
                    "INNER JOIN users u ON u.id = departments_staff.doctor_id " +
                    "WHERE u.login = ?";
    private static final String SQL_ADD_DEPARTMENTS_STAFF_ROW =
            "INSERT INTO departments_staff (department_id, doctor_id) " +
                    "SELECT departments.id, users.id FROM departments, users WHERE login = ? AND departments.id = ?";
    private static final String SQL_FIND_DEPARTMENT_STAFF =
            "SELECT users.id, users.login, users.password FROM users " +
                    "INNER JOIN departments_staff ds on users.id = ds.doctor_id " +
                    "WHERE department_id = ?";

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public boolean updateStaffDepartment(Department department, ServiceAction serviceAction, String login)
            throws DaoException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(serviceAction.equals(ServiceAction.ADD) ?
                    SQL_ADD_DEPARTMENTS_STAFF_ROW : SQL_DELETE_DEPARTMENTS_STAFF_ROW);
            statement.setString(1, login);
            if (serviceAction.equals(ServiceAction.ADD)) {
                statement.setInt(2, department.id);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 1) {
                result = true;
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating department staff failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return result;
    }

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
