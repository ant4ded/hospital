package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.RolesFieldName;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ServiceAction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CREATE_USER =
            "INSERT INTO users (login, password) VALUES (?, ?)";
    private static final String SQL_CREATE_USER_ROLES =
            "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
    private static final String SQL_FIND_BY_LOGIN =
            "SELECT id, password  FROM users WHERE login = ?";
    private static final String SQL_FIND_BY_ID =
            "SELECT login, password  FROM users WHERE id = ?";
    private static final String SQL_FIND_USER_ROLES =
            "SELECT title FROM hospital.roles " +
                    "INNER JOIN users_roles ON roles.id = users_roles.role_id " +
                    "INNER JOIN users ON users_roles.user_id = users.id WHERE users.id = ?";
    private static final String SQL_UPDATE =
            "UPDATE users SET login = ?, password = ? WHERE id = ?";
    private static final String SQL_UPDATE_USER_ROLE =
            "INSERT INTO users_roles (user_id, role_id) " +
                    "SELECT users.id, roles.id FROM users, roles WHERE login = ? AND title = ?";
    private static final String SQL_DELETE_USER_ROLE =
            "DELETE users_roles FROM users_roles " +
                    "INNER JOIN users u ON u.id = users_roles.user_id " +
                    "INNER JOIN roles r ON r.id = users_roles.role_id " +
                    "WHERE u.login = ? AND r.title = ?";

    @Override
    public int create(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Creating user failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            } else {
                throw new DaoException("Creating user failed, no id obtained.");
            }
            generatedKeys.close();
            statement.close();

            statement = connection.prepareStatement(SQL_CREATE_USER_ROLES);
            statement.setInt(1, user.getId());
            statement.setInt(2, Role.CLIENT.id);
            affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Creating user roles failed, no rows affected.");
            }
            connection.setAutoCommit(true);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Creating user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return user.getId();
    }

    @Override
    public User update(User oldValue, User newValue) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        User userFromDb = findByLogin(oldValue.getLogin()).orElseThrow(DaoException::new);
        userFromDb.setLogin(newValue.getLogin());
        userFromDb.setPassword(newValue.getPassword());
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, userFromDb.getLogin());
            statement.setString(2, userFromDb.getPassword());
            statement.setInt(3, userFromDb.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Updating user failed, no rows affected.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return userFromDb;
    }

    @Override
    public Optional<User> findByLogin(String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User userFromDb = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_LOGIN);
            statement.setString(1, login);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                userFromDb = new User();
                userFromDb.setId(resultSet.getInt(UsersFieldName.ID));
                userFromDb.setLogin(login);
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                userFromDb.setRoles(findUserRoles(userFromDb.getId()));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
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
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                userFromDb = new User();
                userFromDb.setId(id);
                userFromDb.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                findUserRoles(userFromDb.getId());
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(userFromDb);
    }

    @Override
    public boolean updateUserRoles(String login, ServiceAction serviceAction, Role role) throws DaoException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(serviceAction.equals(ServiceAction.REMOVE) ?
                    SQL_DELETE_USER_ROLE : SQL_UPDATE_USER_ROLE);
            statement.setString(1, login);
            statement.setString(2, role.name());

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                result = true;
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating user roles failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return result;
    }

    private List<Role> findUserRoles(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Role> roles = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USER_ROLES);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Role role = Role.valueOf(resultSet.getString(RolesFieldName.TITLE));
                roles.add(role);
            }
            if (roles.isEmpty()) {
                throw new DaoException("Find user roles failed. Empty roles list.");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find user roles failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return roles;
    }
}
