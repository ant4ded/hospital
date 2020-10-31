package by.epam.hospital.connection;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static final Logger logger = Logger.getLogger(ConnectionPool.class);
    private static final ConnectionPool instance = new ConnectionPool();
    private final int poolSize;
    private final BlockingQueue<Connection> freeConnections;
    private final BlockingQueue<Connection> usedConnections;

    private ConnectionPool() {
        DbResourceManager dbResourceManager = DbResourceManager.getInstance();
        String url = dbResourceManager.getValue(DbParameterName.CONFIG_URL);
        this.poolSize = Integer.parseInt(dbResourceManager.getValue(DbParameterName.CONFIG_POOL_SIZE));

        Properties properties = new Properties();
        properties.put(DbParameterName.CONNECTION_USER,
                dbResourceManager.getValue(DbParameterName.CONFIG_USER));
        properties.put(DbParameterName.CONNECTION_PASSWORD,
                dbResourceManager.getValue(DbParameterName.CONFIG_PASSWORD));
        properties.put(DbParameterName.CONNECTION_SERVER_TIMEZONE,
                dbResourceManager.getValue(DbParameterName.CONFIG_SERVER_TIMEZONE));
        properties.put(DbParameterName.CONNECTION_AUTO_RECONNECT,
                dbResourceManager.getValue(DbParameterName.CONFIG_AUTO_RECONNECT));
        properties.put(DbParameterName.CONNECTION_CHARACTER_ENCODING,
                dbResourceManager.getValue(DbParameterName.CONFIG_CHARACTER_ENCODING));
        properties.put(DbParameterName.CONNECTION_USE_SSL,
                dbResourceManager.getValue(DbParameterName.CONFIG_USE_SSL));

        freeConnections = new ArrayBlockingQueue<>(poolSize);
        usedConnections = new ArrayBlockingQueue<>(poolSize);

        try {
            Class.forName(dbResourceManager.getValue(DbParameterName.CONFIG_DRIVER));
            for (int i = 0; i < poolSize; i++) {
                freeConnections.add(new ProxyConnection(DriverManager.getConnection(url, properties)));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e);
        }
    }

    public static ConnectionPool getInstance() {
        return instance;
    }

    public Connection getConnection() throws ConnectionException {
        Connection connection = null;
        try {
            connection = freeConnections.take();
            if (!usedConnections.offer(connection)) {
                throw new ConnectionException("Can not add connection to usedConnections. Used queue is full.");
            }
        } catch (InterruptedException e) {
            logger.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) throws ConnectionException {
        if (connection.getClass() == ProxyConnection.class) {
            if (!usedConnections.remove(connection)) {
                throw new ConnectionException("Can not delete connection from usedConnections. Connection not present.");
            }
            if (!freeConnections.offer(connection)) {
                throw new ConnectionException("Can not add connection to freeConnections. Used queue is full.");
            }
        } else {
            throw new ConnectionException("Error with release non proxy connection");
        }
    }

    public void destroyPool() throws ConnectionException {
        for (Connection usedConnection : usedConnections) {
            releaseConnection(usedConnection);
        }
        for (int i = 0; i < poolSize; i++) {
            try {
                ((ProxyConnection) freeConnections.take()).reallyClose();
            } catch (SQLException e) {
                throw new ConnectionException("Error with close connection", e);
            } catch (InterruptedException e) {
                logger.warn("Interrupted!", e);
                Thread.currentThread().interrupt();
            }
        }
        deregisterDriver();
    }

    public static void closeConnection(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
                connection.close();
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            closeConnection(connection, statement);
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    private void deregisterDriver() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error(e);
            }
        });
    }
}

