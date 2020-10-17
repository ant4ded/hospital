package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.RoleDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.RolesFieldName;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.util.Action;

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
    private static final String SQL_FIND_BY_ID = "SELECT login, password  FROM users WHERE id = ?";
    private static final String SQL_FIND_USER_ROLES = "SELECT title FROM hospital.roles " +
            "INNER JOIN users_roles ON roles.id = users_roles.role_id " +
            "INNER JOIN users ON users_roles.user_id = users.id WHERE users.id = ?";
    private static final String SQL_UPDATE = "UPDATE users SET login = ?, password = ? WHERE id = ?";
    private static final String SQL_DELETE_USER_ROLE = "DELETE users_roles FROM users_roles " +
            "INNER JOIN users u ON u.id = users_roles.user_id " +
            "INNER JOIN roles r ON r.id = users_roles.role_id " +
            "WHERE u.login = ? AND r.title = ?";
    private static final String SQL_UPDATE_USER_ROLE = "INSERT INTO users_roles (user_id, role_id) " +
            "SELECT users.id, roles.id FROM users, roles WHERE login = ? AND title = ?";

    private final RoleDao roleDao = new RoleDaoImpl();

    private void setUserRoles(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USER_ROLES);
            statement.setInt(1, user.getId());
            statement.execute();
            resultSet = statement.getResultSet();
            Map<String, Role> map = new HashMap<>();
            while (resultSet.next()) {
                Role role = Role.valueOf(resultSet.getString(RolesFieldName.TITLE));
                map.put(role.name(), role);
            }
            user.setRoles(map);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement, resultSet);
        }
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
            User userFromDb = find(user.getLogin()).orElseThrow(DaoException::new);
            statement.close();

            statement = connection.prepareStatement(SQL_CREATE_USER_ROLES);
            statement.setInt(1, userFromDb.getId());
            statement.setInt(2, Role.CLIENT.ID);
            statement.execute();
            statement.close();
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
            userFromDb = find(oldValue.getLogin()).orElseThrow(DaoException::new);

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
    public Optional<User> find(String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User userFromDb = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_FIND);

            statement.setString(1, login);

            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                userFromDb = new User();
                userFromDb.setId(resultSet.getInt(UsersFieldName.ID));
                userFromDb.setLogin(login);
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));

                ConnectionUtil.closeConnection(connection, statement, resultSet);
                setUserRoles(userFromDb);
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

    @Override
    public Optional<User> findById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User userFromDb = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);

            statement.setInt(1, id);

            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                userFromDb = new User();
                userFromDb.setId(id);
                userFromDb.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));

                ConnectionUtil.closeConnection(connection, statement, resultSet);
                setUserRoles(userFromDb);
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

    @Override
    public void updateUserRoles(String login, Action action, Role role) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();

            if (!action.equals(Action.ADD) && !action.equals(Action.REMOVE)) {
                throw new DaoException("Invalid parameter value. Parameter - " + ParameterName.ACTION);
            }

            statement = connection.prepareStatement(action.equals(Action.REMOVE) ?
                    SQL_DELETE_USER_ROLE : SQL_UPDATE_USER_ROLE);

            statement.setString(1, login);
            statement.setString(2, role.name());

            statement.execute();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not update row on users_details table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }
}
