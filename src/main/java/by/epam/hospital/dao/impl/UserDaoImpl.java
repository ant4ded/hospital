package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.RoleDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.table.RolesFieldName;
import by.epam.hospital.entity.table.UsersFieldName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CREATE_USER = "INSERT INTO users (login, password) VALUES (?, ?)";
    private static final String SQL_CREATE_USER_ROLES = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
    private static final String SQL_FIND = "SELECT id, password  FROM users WHERE login = ?";
    private static final String SQL_FIND_ROLES = "SELECT title FROM hospital.roles " +
            "INNER JOIN users_roles ON roles.id = users_roles.role_id " +
            "INNER JOIN users ON users_roles.user_id = users.id WHERE users.id = ?";
    private static final String SQL_UPDATE = "UPDATE users SET login = ?, password = ? WHERE id = ?";

    private final RoleDao roleDao = new RoleDaoImpl();

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
            statement.setInt(2, roleDao.findRoleId(Role.CLIENT));
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
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                userFromDb.setLogin(login);

                ConnectionUtil.closeConnection(connection, statement, resultSet);
                connection = DataSourceFactory.createMysqlDataSource().getConnection();
                statement = connection.prepareStatement(SQL_FIND_ROLES);
                statement.setInt(1, userFromDb.getId());
                statement.execute();
                resultSet = statement.getResultSet();
                Map<String, Role> map = new HashMap<>();
                while (resultSet.next()) {
                    Role role = Role.valueOf(resultSet.getString(RolesFieldName.TITLE));
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
