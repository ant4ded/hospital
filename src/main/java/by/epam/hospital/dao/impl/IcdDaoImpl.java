package by.epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.IcdDao;
import by.epam.hospital.entity.Icd;
import by.epam.hospital.entity.table.IcdFieldName;

import java.sql.*;
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
    private static final String SP_FIND_BY_CODE =
            "CALL FindIcdByCode(?)";

    /**
     * Find {@code Icd} entity by {@code Icd.code} field.
     *
     * @param code {@code String} value unique {@code Icd.code} field.
     * @return {@code Optional<Icd>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs or if
     *                      {@link ConnectionPool} throws
     *                      {@link ConnectionException}.
     * @see Optional
     */
    @Override
    public Optional<Icd> findByCode(String code) throws DaoException {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        Icd icd = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareCall(SP_FIND_BY_CODE);
            statement.setString(1, code);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                icd = new Icd();
                icd.setId(resultSet.getInt(IcdFieldName.ID));
                icd.setCode(resultSet.getString(IcdFieldName.CODE));
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
}
