package by.epam.hospital.connection;

import com.mysql.cj.jdbc.Driver;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public enum CustomConnectionPool {
    INSTANCE;

    private static final int DEFAULT_POOL_SIZE = 32;
    private BlockingQueue<ProxyConnection> freeConnections;
    private Queue<ProxyConnection> usedConnections;
    private final Logger logger = Logger.getLogger(CustomConnectionPool.class);

    CustomConnectionPool() {
        try {
            DriverManager.registerDriver(new Driver());
            freeConnections = new LinkedBlockingDeque<>(DEFAULT_POOL_SIZE);
            usedConnections = new ArrayDeque<>();
            for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
                ProxyConnection connection =
                        new ProxyConnection(DataSourceFactory.createMysqlDataSource().getConnection());
                freeConnections.add(connection);
            }
        } catch (SQLException | ConnectionException e) {
            logger.error(e);
        }
    }

    public Connection getConnection() throws ConnectionException {
        ProxyConnection connection;
        try {
            connection = freeConnections.take();
            usedConnections.offer(connection);
        } catch (InterruptedException e) {
            throw new ConnectionException("Error with get connection", e);
        }
        return connection;
    }

    public void releaseConnection(Connection connection) throws ConnectionException {
        if (connection.getClass() == ProxyConnection.class) {
            usedConnections.remove(connection);
            freeConnections.offer((ProxyConnection) connection);
        } else {
            throw new ConnectionException("Error with release non proxy connection");
        }
    }

    public void destroyPool() throws ConnectionException {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            try {
                freeConnections.take().reallyClose();
            } catch (SQLException e) {
                throw new ConnectionException("Error with close connection", e);
            } catch (InterruptedException e) {
                throw new ConnectionException("Error with get connection", e);
            }
        }
        deregisterDriver();
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

