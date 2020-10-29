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

public class DepartmentDaoImpl implements DepartmentDao {
    private static final String SQL_FIND_DEPARTMENT_HEAD =
            "SELECT department_head_id FROM departments WHERE id = ?";
    private static final String SQL_UPDATE_DEPARTMENT_HEAD =
            "UPDATE departments SET department_head_id = ? WHERE id = ?";
    private static final String SQL_FIND_DEPARTMENT_BY_USERNAME =
            "SELECT title FROM departments " +
                    "INNER JOIN departments_staff ds on departments.id = ds.department_id " +
                    "INNER JOIN users u on ds.doctor_id = u.id " +
                    "WHERE u.login = ?";

    private final UserDao userDao = new UserDaoImpl();

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
            if (!resultSet.next()) {
                throw new DaoException("Find department failed.");
            }
            departmentHead = userDao.findById(resultSet.getInt(1));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find department head failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return departmentHead;
    }

    @Override
    public void updateDepartmentHead(Department department, String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        Optional<User> user = userDao.findByLogin(login);
        try {
            if (user.isEmpty()) {
                throw new DaoException("Can not find user on users table.");
            }
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE_DEPARTMENT_HEAD);
            statement.setInt(1, user.get().getId());
            statement.setInt(2, department.id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Update department failed, no rows affected.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Update department failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    @Override
    public Department findDepartment(String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Department department = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_DEPARTMENT_BY_USERNAME);
            statement.setString(1, login);
            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                department = Department.valueOf(resultSet.getString(1));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find department failed.");
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return department;
    }

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
