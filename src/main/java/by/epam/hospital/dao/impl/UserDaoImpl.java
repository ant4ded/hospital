package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.RolesFieldName;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.entity.table.UsersFieldName;

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
     * Sql {@code String} object for call stored procedure {@code CreateClientWithUserDetails}.
     * Written for the MySQL dialect.
     */
    private static final String SP_CREATE_CLIENT_WITH_USER_DETAILS =
            "CALL CreateClientWithUserDetails(?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * Sql {@code String} object for call stored procedure {@code FindUserByLogin}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_USER_BY_LOGIN = "CALL FindUserByLogin(?)";

    /**
     * Sql {@code String} object for call stored procedure {@code FindUserByLogin}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_USER_WITH_USER_DETAILS_BY_LOGIN = "CALL FindUserWithUserDetailsByLogin(?)";

    /**
     * Sql {@code String} object for call stored procedure {@code FindUserById}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_USER_BY_ID = "CALL FindUserById(?)";

    /**
     * Sql {@code String} object for call stored procedure {@code FindUserRolesByLogin}.
     * Written for the MySQL dialect.
     */
    private static final String SP_FIND_USER_ROLES_BY_LOGIN = "CALL FindUserRolesByLogin(?)";

    /**
     * Sql {@code String} object for call stored procedure {@code AddUserRole}.
     * Written for the MySQL dialect.
     */
    private static final String SP_ADD_USER_ROLE = "CALL AddUserRole(?,?)";

    /**
     * Sql {@code String} object for call stored procedure {@code DeleteUserRole}.
     * Written for the MySQL dialect.
     */
    private static final String SP_DELETE_USER_ROLE = "CALL DeleteUserRole(?,?)";

    /**
     * Sql {@code String} object for call stored procedure {@code UpdateUserLoginAndPassword}.
     * Written for the MySQL dialect.
     */
    private static final String SP_UPDATE_LOGIN_AND_PASSWORD = "CALL UpdateUserLoginAndPassword(?,?,?)";

    /**
     * Create entity {@link User} with entity {@link by.epam.hospital.entity.UserDetails}
     * and with {@link Role#CLIENT} in database.
     *
     * @param user an a {@code User} entity.
     * @return auto-generated {@code User.id} field or zero if not success.
     * @throws DaoException if a database access error occurs or
     *                      if {@link ConnectionPool} throws {@link ConnectionException}.
     */
    @Override
    public int createClientWithUserDetails(User user) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        int userId;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_CREATE_CLIENT_WITH_USER_DETAILS);

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserDetails().getPassportId());
            statement.setString(4, user.getUserDetails().getGender().name());
            statement.setString(5, user.getUserDetails().getFirstName());
            statement.setString(6, user.getUserDetails().getSurname());
            statement.setString(7, user.getUserDetails().getLastName());
            statement.setDate(8, user.getUserDetails().getBirthday());
            statement.setString(9, user.getUserDetails().getAddress());
            statement.setString(10, user.getUserDetails().getPhone());

            statement.registerOutParameter(11, Types.INTEGER);

            statement.execute();
            userId = statement.getInt(11);
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("CreateClientWithUserDetails failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement);
        }
        return userId;
    }

    /**
     * Update entity {@code User} in database.
     *
     * @param login    {@code String} value of {@code User.login}
     *                 for find entity that need to be updated.
     * @param newValue new value for {@code User} entity.
     * @return {@code newValue} if it was updated or
     * {@code oldValue} if it wasn't of {@code User} entity.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see ConnectionException
     */
    @Override
    public Optional<User> updateLoginAndPassword(String login, User newValue) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_UPDATE_LOGIN_AND_PASSWORD);
            statement.setString(1, login);
            statement.setString(2, newValue.getLogin());
            statement.setString(3, newValue.getPassword());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                newValue.setId(resultSet.getInt(UsersFieldName.ID));
                newValue.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                newValue.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                newValue.setRoles(findUserRoles(newValue.getLogin()));
                optionalUser = Optional.of(newValue);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating user failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalUser;
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
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_USER_BY_LOGIN);
            statement.setString(1, login);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User userFromDb = new User();
                userFromDb.setId(resultSet.getInt(UsersFieldName.ID));
                userFromDb.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                userFromDb.setRoles(findUserRoles(userFromDb.getLogin()));
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
     * Find {@code User} entity by {@code User.login} field
     * with {@link by.epam.hospital.entity.UserDetails}.
     *
     * @param login {@code String} value of {@code User.login} field.
     * @return {@code Optional<User>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    @Override
    public Optional<User> findByLoginWithUserDetails(String login) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_USER_WITH_USER_DETAILS_BY_LOGIN);
            statement.setString(1, login);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User userFromDb = new User();
                UserDetails userDetails = new UserDetails();
                userFromDb.setUserDetails(userDetails);
                userFromDb.setUserDetails(userDetails);
                userFromDb.setId(resultSet.getInt(UsersFieldName.ID));
                userFromDb.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));

                userDetails.setPassportId(resultSet.getString(UsersDetailsFieldName.PASSPORT_ID));
                userDetails.setUserId(resultSet.getInt(UsersDetailsFieldName.USER_ID));
                userDetails.setGender(UserDetails.Gender
                        .valueOf(resultSet.getString(UsersDetailsFieldName.GENDER)));
                userDetails.setFirstName(resultSet.getString(UsersDetailsFieldName.FIRST_NAME));
                userDetails.setSurname(resultSet.getString(UsersDetailsFieldName.SURNAME));
                userDetails.setLastName(resultSet.getString(UsersDetailsFieldName.LAST_NAME));
                userDetails.setBirthday(resultSet.getDate(UsersDetailsFieldName.BIRTHDAY));
                userDetails.setAddress(resultSet.getString(UsersDetailsFieldName.ADDRESS));
                userDetails.setPhone(resultSet.getString(UsersDetailsFieldName.PHONE));

                userFromDb.setRoles(findUserRoles(userFromDb.getLogin()));
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
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_USER_BY_ID);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User userFromDb = new User();
                userFromDb.setId(id);
                userFromDb.setLogin(resultSet.getString(UsersFieldName.LOGIN));
                userFromDb.setPassword(resultSet.getString(UsersFieldName.PASSWORD));
                userFromDb.setRoles(findUserRoles(userFromDb.getLogin()));
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
     * Add {@link Role} to entity {@link User}
     *
     * @param login {@code String} value of {@code User.login} field.
     * @param role  enumeration element of {@link Role}.
     * @return {@code true} if it was successful and {@code false} if it wasn't.
     * @throws DaoException if a database access error occurs.
     */
    @Override
    public boolean addUserRole(String login, Role role) throws DaoException {
        boolean result = false;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_ADD_USER_ROLE);
            statement.setString(1, login);
            statement.setString(2, role.name());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int affectedRows = resultSet.getInt(1);
                if (affectedRows != 0) {
                    result = true;
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating user roles failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    /**
     * Delete {@link Role} from entity {@link User}
     *
     * @param login {@code String} value of {@code User.login} field.
     * @param role  enumeration element of {@link Role}.
     * @return {@code true} if it was successful and {@code false} if it wasn't.
     * @throws DaoException if a database access error occurs.
     */
    @Override
    public boolean deleteUserRole(String login, Role role) throws DaoException {
        boolean result = false;
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_DELETE_USER_ROLE);
            statement.setString(1, login);
            statement.setString(2, role.name());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int affectedRows = resultSet.getInt(1);
                if (affectedRows != 0) {
                    result = true;
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Updating user roles failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return result;
    }

    /**
     * Update roles {@code User} entity by {@code User.login} field
     * using {@code PreparedStatement}.
     *
     * @param login {@code int} value of {@code User.login} field.
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
    private List<Role> findUserRoles(String login) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Role> roles = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_USER_ROLES_BY_LOGIN);
            statement.setString(1, login);

            resultSet = statement.executeQuery();
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
