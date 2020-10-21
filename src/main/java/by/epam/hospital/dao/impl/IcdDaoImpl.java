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

public class IcdDaoImpl implements IcdDao {
    private static final String SQL_FIND_BY_ID = "SELECT id, title  FROM icd WHERE code = ?";

    @Override
    public Optional<Icd> findByCode(String code) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Icd icd = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);

            statement.setString(1, code);

            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                icd = new Icd(resultSet.getInt(IcdFieldName.ID), code, resultSet.getString(IcdFieldName.TITLE));

                ConnectionPool.closeConnection(connection, statement, resultSet);
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not find row on users table", e);
        } finally {
            ConnectionPool.closeConnection(connection, statement, resultSet);
        }
        return Optional.ofNullable(icd);
    }
}
