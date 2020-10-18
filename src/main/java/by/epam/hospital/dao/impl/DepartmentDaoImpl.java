package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.DepartmentsFieldName;
import by.epam.hospital.service.util.Action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {
    private static final String SQL_FIND_DEPARTMENT_HEAD = "SELECT department_head_id FROM departments WHERE id = ?";
    private static final String SQL_UPDATE_DEPARTMENT_HEAD = "UPDATE departments SET department_head_id = ? WHERE id = ?";

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public Optional<User> findHeadDepartment(Department department) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> departmentHead;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_FIND_DEPARTMENT_HEAD);

            statement.setInt(1, department.ID);

            statement.execute();
            resultSet = statement.getResultSet();
            if (!resultSet.next()) {
                throw new DaoException("Can not find " + DepartmentsFieldName.DEPARTMENT_HEAD_ID + " on departments table");
            }
            departmentHead = userDao.findById(resultSet.getInt(1));
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find " + DepartmentsFieldName.DEPARTMENT_HEAD_ID + " on departments table");
        } finally {
            ConnectionUtil.closeConnection(connection, statement, resultSet);
        }
        return departmentHead;
    }

    @Override
    public void updateDepartmentHead(Department department, String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        Optional<User> user = userDao.find(login);
        try {
            if (user.isEmpty()) {
                throw new DaoException("Can not find user on users table");
            }
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE_DEPARTMENT_HEAD);

            statement.setInt(1, user.get().getId());
            statement.setInt(2, department.ID);

            statement.execute();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not update " + DepartmentsFieldName.DEPARTMENT_HEAD_ID + " on departments table");
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }
}
