package by.epam.hospital.connection;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionUtil {
    private final static Logger LOGGER = Logger.getLogger(ConnectionUtil.class);

    public static void closeConnection(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }
}
