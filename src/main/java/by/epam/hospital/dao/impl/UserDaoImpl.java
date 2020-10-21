package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.RolesFieldName;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ServiceAction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CREATE_USER = "INSERT INTO users (login, password) VALUES (?, ?)";
    private static final String SQL_CREATE_USER_ROLES = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";

    private static final String SQL_FIND_BY_LOGIN = "SELECT id, password  FROM users WHERE login = ?";
    private static final String SQL_FIND_BY_ID = "SELECT login, password  FROM users WHERE id = ?";
    private static final String SQL_FIND_USER_ROLES = "SELECT title FROM hospital.roles " +
            "INNER JOIN users_roles ON roles.id = users_roles.role_id " +
            "INNER JOIN users ON users_roles.user_id = users.id WHERE users.id = ?";

    private static final String SQL_UPDATE = "UPDATE users SET login = ?, password = ? WHERE id = ?";
    private static final String SQL_UPDATE_USER_ROLE = "INSERT INTO users_roles (user_id, role_id) " +
            "SELECT users.id, roles.id FROM users, roles WHERE login = ? AND title = ?";

    private static final String SQL_DELETE_USER_ROLE = "DELETE users_roles FROM users_roles " +
            "INNER JOIN users u ON u.id = users_roles.user_id " +
            "INNER JOIN roles r ON r.id = users_roles.role_id " +
            "WHERE u.login = ? AND r.title = ?";

    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();

    private void setUserRoles(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_USER_ROLES);
            statement.setInt(1, user.getId());
            statement.execute();
            resultSet = statement.getResultSet();
            ArrayList<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                Role role = Role.valueOf(resultSet.getString(RolesFieldName.TITLE));
                roles.add(role);
            }
            user.setRoles(roles);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
    }

    @Override
    public void create(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.CLIENT);
        UserDetails userDetails = user.getUserDetails();
        try {
            connection = ConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(SQL_CREATE_USER);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.execute();
            user = find(user.getLogin()).orElseThrow(DaoException::new);
            userDetails.setUserId(user.getId());
            user.setUserDetails(userDetails);
            user.setRoles(roles);
            statement.close();

            statement = connection.prepareStatement(SQL_CREATE_USER_ROLES);
            statement.setInt(1, user.getId());
            statement.setInt(2, Role.CLIENT.id);
            statement.execute();
            statement.close();

            userDetailsDao.create(userDetails);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not add row to users table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    @Override
    public void update(User oldValue, User newValue) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        User userFromDb;
        try {
            connection = ConnectionPool.getInstance().getConnection();
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
            ConnectionPool.closeConnection(connection, statement);
        }
    }

    @Override
    public Optional<User> find(String login) throws DaoException {
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

                ConnectionPool.closeConnection(connection, statement, resultSet);
                setUserRoles(userFromDb);
                userFromDb.setUserDetails(userDetailsDao.find(userFromDb.getId()).orElse(new UserDetails()));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users table", e);
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

                ConnectionPool.closeConnection(connection, statement, resultSet);
                setUserRoles(userFromDb);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(userFromDb);
    }

    @Override
    public Optional<User> findByRegistrationData(String firstName, String surname, String lastName, Date birthday) throws DaoException {
        User userFromDb = null;
        Optional<UserDetails> optionalUserDetails =
                userDetailsDao.findByRegistrationData(firstName, surname, lastName, birthday);
        if (optionalUserDetails.isPresent()) {
            userFromDb = findById(optionalUserDetails.get().getUserId()).orElseThrow(DaoException::new);
            userFromDb.setUserDetails(optionalUserDetails.get());
        }
        return Optional.ofNullable(userFromDb);
    }

    @Override
    public void updateUserRoles(String login, ServiceAction serviceAction, Role role) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();

            if (!serviceAction.equals(ServiceAction.ADD) && !serviceAction.equals(ServiceAction.REMOVE)) {
                throw new DaoException("Invalid parameter value. Parameter - " + ParameterName.ACTION);
            }

            statement = connection.prepareStatement(serviceAction.equals(ServiceAction.REMOVE) ?
                    SQL_DELETE_USER_ROLE : SQL_UPDATE_USER_ROLE);

            statement.setString(1, login);
            statement.setString(2, role.name());

            statement.execute();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not update row on users_details table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
    }
}
