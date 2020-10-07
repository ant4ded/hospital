package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.RolesColumnName;
import by.epam.hospital.entity.table.UsersColumnName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CREATE_USER = "INSERT INTO users (login, password) VALUES (?, ?)";
    private static final String SQL_CREATE_USER_ROLES = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
    private static final String SQL_FIND = "SELECT id, password  FROM users WHERE login = ?";
    private static final String SQL_FIND_ROLES = "SELECT title FROM hospital.roles " +
            "INNER JOIN users_roles ON roles.id = users_roles.role_id " +
            "INNER JOIN users ON users_roles.user_id = users.id WHERE users.id = ?";
    private static final String SQL_FIND_ROLE_ID = "SELECT id FROM roles WHERE title = ?";
    private static final String SQL_UPDATE = "UPDATE users SET login = ?, password = ? WHERE id = ?";

    private int findRoleId(Role role) throws DaoException {
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

    @Override
    public void create(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();

            statement = connection.prepareStatement(SQL_CREATE_USER);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.execute();
            User userFromDb = find(user).orElseThrow(DaoException::new);
            statement.close();

            for (Role role : user.getRoles().values()) {
                statement = connection.prepareStatement(SQL_CREATE_USER_ROLES);
                statement.setInt(1, userFromDb.getId());
                statement.setInt(2, findRoleId(role));
                statement.execute();
                statement.close();
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not add row to users table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }

    @Override
    public void update(User oldValue, User newValue) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        User userFromDb;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);
            userFromDb = find(oldValue).orElseThrow(DaoException::new);

            statement.setString(1, newValue.getLogin());
            statement.setString(2, newValue.getPassword());
            statement.setInt(3, userFromDb.getId());
            statement.execute();

        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not update row on users_details table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }

    @Override
    public Optional<User> find(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User userFromDb = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_FIND);

            statement.setString(1, user.getLogin());

            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                userFromDb = new User();
                userFromDb.setId(resultSet.getInt(UsersColumnName.ID));
                userFromDb.setPassword(resultSet.getString(UsersColumnName.PASSWORD));
                userFromDb.setLogin(user.getLogin());

                ConnectionUtil.closeConnection(connection, statement, resultSet);
                connection = DataSourceFactory.createMysqlDataSource().getConnection();
                statement = connection.prepareStatement(SQL_FIND_ROLES);
                statement.setInt(1, userFromDb.getId());
                statement.execute();
                resultSet = statement.getResultSet();
                Map<String, Role> map = new HashMap<>();
                while (resultSet.next()) {
                    Role role = Role.valueOf(resultSet.getString(RolesColumnName.TITLE));
                    map.put(role.name(), role);
                }
                userFromDb.setRoles(map);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(userFromDb);
    }
}
