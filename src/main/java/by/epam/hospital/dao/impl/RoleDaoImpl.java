package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.RoleDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.table.RolesFieldName;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    private static final String SQL_FIND = "SELECT title FROM roles ORDER BY id";
    private static final String SQL_FIND_ROLE_ID = "SELECT id FROM roles WHERE title = ?";

    @Override
    public List<Role> findAll() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Role> roles = new ArrayList<>();
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.createStatement();
            statement.execute(SQL_FIND);

            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString(RolesFieldName.TITLE)));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement, resultSet);
        }
        return roles;
    }

    @Override
    public int findRoleId(Role role) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_FIND_ROLE_ID);

            statement.setString(1, role.name());

            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                throw new DaoException("Can not find id on roles table");
            }

        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find id on roles table");
        } finally {
            ConnectionUtil.closeConnection(connection, statement, resultSet);
        }
        return id;
    }
}
