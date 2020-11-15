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

/**
 * {@code UserDaoImpl} implementation of {@link UserDao}.
 * Implements all required methods for work with the {@link User} database entity
 * and included in {@code User} object element of enum {@link Role}.
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

public class UserDaoImpl implements UserDao {
    /**
     * Sql {@code String} object for creating {@code User} entity in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE_USER = """
            INSERT INTO users (login, password) VALUES (?, ?)""";
    /**
     * Sql {@code String} object for find {@code User}
     * by {@code login} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_LOGIN = """
            SELECT id, password
            FROM users
            WHERE login = ?""";
    /**
     * Sql {@code String} object for find {@code User}
     * by {@code id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_ID = """
            SELECT login, password
            FROM users
            WHERE id = ?""";
    /**
     * Sql {@code String} object for find {@code User} object's
     * elements of enum {@code Role} by {@code userId} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_USER_ROLES = """
            SELECT title
            FROM hospital.roles
            INNER JOIN users_roles ON roles.id = users_roles.role_id
            INNER JOIN users ON users_roles.user_id = users.id
            WHERE users.id = ?""";
    /**
     * Sql {@code String} object for updating {@code User} entity
     * by {@code id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_UPDATE = """
            UPDATE users
            SET login = ?, password = ?
            WHERE id = ?""";
    /**
     * Sql {@code String} object for creating {@code User} object's
     * element of enum {@code Role} by {@code User.login}
     * and {@code Role.title} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_CREATE_USER_ROLE = """
            INSERT INTO users_roles (user_id, role_id)
                SELECT users.id, roles.id
                FROM users, roles
                WHERE login = ? AND title = ?""";
    /**
     * Sql {@code String} object for deleting {@code User} object's
     * element of enum {@code Role} by {@code User.login}
     * and {@code Role.title} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_DELETE_USER_ROLE = """
            DELETE users_roles
            FROM users_roles
            INNER JOIN users u ON u.id = users_roles.user_id
            INNER JOIN roles r ON r.id = users_roles.role_id
            WHERE u.login = ? AND r.title = ?""";

    private static final String SP_CREATE_CLIENT_WITH_USER_DETAILS =
            "CALL CreateClientWithUserDetails(?,?,?,?,?,?,?,?,?,?,?)";

    @Override
    public int createClientWithUserDetails(User user) throws DaoException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        int userId;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            callableStatement = connection.prepareCall(SP_CREATE_CLIENT_WITH_USER_DETAILS);

            callableStatement.setString(1, user.getLogin());
            callableStatement.setString(2, user.getPassword());
            callableStatement.setString(3, user.getUserDetails().getPassportId());
            callableStatement.setString(4, user.getUserDetails().getGender().name());
            callableStatement.setString(5, user.getUserDetails().getFirstName());
            callableStatement.setString(6, user.getUserDetails().getSurname());
            callableStatement.setString(7, user.getUserDetails().getLastName());
            callableStatement.setDate(8, user.getUserDetails().getBirthday());
            callableStatement.setString(9, user.getUserDetails().getAddress());
            callableStatement.setString(10, user.getUserDetails().getPhone());

            callableStatement.registerOutParameter(11, Types.INTEGER);

            callableStatement.execute();
            userId = callableStatement.getInt(11);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateClientWithUserDetails failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, callableStatement);
        }
        return userId;
    }

    /**
     * Create entity {@code User} in database using {@code PreparedStatement}
     * with parameter {@code Statement.RETURN_GENERATED_KEYS}.
     *
     * @param user an a {@code User} entity.
     * @return auto-generated {@code User.id} field.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     */
    @Override
    public int create(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        int userId = 0;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                    generatedKeys.close();
                    statement.close();

                    statement = connection.prepareStatement(SQL_CREATE_USER_ROLE);
                    statement.setString(1, user.getLogin());
                    statement.setString(2, Role.CLIENT.name());
                    affectedRows = statement.executeUpdate();
                    if (affectedRows == 0) {
                        connection.rollback();
                    }
                }
            }
            connection.setAutoCommit(true);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Creating user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return userId;
    }

    /**
     * Update entity {@code User} in database
     * using {@code PreparedStatement}.
     *
     * @param oldValue {@code User} entity that need to be updated.
     * @param newValue new value for {@code User} entity.
     * @return {@code newValue} if it was updated or
     * {@code oldValue} if it wasn't of {@code User} entity.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     */
    @Override
    public User update(User oldValue, User newValue) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        User userFromDb = findByLogin(oldValue.getLogin()).orElseThrow(DaoException::new);
        User updatedUser = new User();
        updatedUser.setId(newValue.getId());
        updatedUser.setLogin(newValue.getLogin());
        updatedUser.setPassword(newValue.getPassword());
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, updatedUser.getLogin());
            statement.setString(2, updatedUser.getPassword());
            statement.setInt(3, updatedUser.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                updatedUser = userFromDb;
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return updatedUser;
    }

    /**
     * Find {@code User} entity by {@code User.login} field
     * using {@code PreparedStatement}.
     *
     * @param login {@code String} value of {@code User.login} field.
     * @return {@code Optional<User>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<User> findByLogin(String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_LOGIN);
            statement.setString(1, login);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                User userFromDb = new User();
                userFromDb.setId(resultSet.getInt(UsersFieldName.ID));
                userFromDb.setLogin(login);
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                userFromDb.setRoles(findUserRoles(userFromDb.getId()));
                optionalUser = Optional.of(userFromDb);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalUser;
    }

    /**
     * Find {@code User} entity by {@code User.id} field
     * using {@code PreparedStatement}.
     *
     * @param id {@code int} value of {@code User.id} field.
     * @return {@code Optional<User>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<User> findById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                User userFromDb = new User();
                userFromDb.setId(id);
                userFromDb.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                userFromDb.setRoles(findUserRoles(userFromDb.getId()));
                optionalUser = Optional.of(userFromDb);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalUser;
    }

    /**
     * Update roles {@code User} entity by {@code login} field
     * using {@code PreparedStatement}.
     *
     * @param login         {@code String} value of {@code User.login} field.
     * @param serviceAction enumeration element of {@link ServiceAction}.
     * @param role          enumeration element of {@link Role}.
     * @return {@code true} if it was successful and {@code false} if it wasn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     */
    @Override
    public boolean updateUserRoles(String login, ServiceAction serviceAction, Role role) throws DaoException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(serviceAction == ServiceAction.ADD ?
                    SQL_CREATE_USER_ROLE :
                    SQL_DELETE_USER_ROLE);
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

    /**
     * Update roles {@code User} entity by {@code User.login} field
     * using {@code PreparedStatement}.
     *
     * @param id {@code int} value of {@code User.login} field.
     * @return {@code List<Role>} being a {@code ArrayList<Role>}
     * object if it present  or an empty {@code List} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see List
     * @see ArrayList
     * @see ConnectionException
     */
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
