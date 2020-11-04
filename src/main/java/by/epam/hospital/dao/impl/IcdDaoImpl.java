package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.IcdDao;
import by.epam.hospital.entity.Icd;
import by.epam.hospital.entity.table.IcdFieldName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * {@code IcdDaoImpl} implementation of {@link IcdDao}.
 * Implements all required methods for work with the {@link Icd} database entity.
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

public class IcdDaoImpl implements IcdDao {
    /**
     * Sql {@code String} object for find {@code Icd} entity
     * by {@code id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_ID = """
            SELECT code, title
            FROM icd
            WHERE id = ?""";
    /**
     * Sql {@code String} object for find {@code Icd} entity
     * by {@code id} in data base.
     * Written for the MySQL dialect.
     */
    private static final String SQL_FIND_BY_CODE = """
            SELECT id, title
            FROM icd
            WHERE code = ?""";

    /**
     * Find {@code Icd} entity by {@code Icd.code} field.
     * using {@code PreparedStatement}.
     *
     * @param code {@code String} value unique {@code Icd.code} field.
     * @return {@code Optional<Icd>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<Icd> findByCode(String code) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Icd icd = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_CODE);
            statement.setString(1, code);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                icd = new Icd();
                icd.setId(resultSet.getInt(IcdFieldName.ID));
                icd.setCode(code);
                icd.setTitle(resultSet.getString(IcdFieldName.TITLE));
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find icd failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(icd);
    }

    /**
     * Find {@code Icd} entity by {@code Icd.id} field
     * using {@code PreparedStatement}.
     *
     * @param id {@code int} value of {@code Icd.id} field.
     * @return {@code Optional<Icd>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs
     *                      and if {@code ConnectionPool}
     *                      throws {@code ConnectionException}.
     * @see PreparedStatement
     * @see ConnectionException
     * @see Optional
     */
    @Override
    public Optional<Icd> findById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<Icd> optionalIcd = Optional.empty();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                Icd icd = new Icd();
                icd.setId(id);
                icd.setCode(resultSet.getString(IcdFieldName.CODE));
                icd.setTitle(resultSet.getString(IcdFieldName.TITLE));
                optionalIcd = Optional.of(icd);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source.", e);
        } catch (SQLException e) {
            throw new DaoException("Find optionalIcd failed.", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return optionalIcd;
    }
}
