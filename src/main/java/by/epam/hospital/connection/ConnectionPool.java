package by.epam.hospital.connection;

import com.mysql.cj.jdbc.Driver;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class CustomConnectionPool {

    private static final ConnectionPool instance = new ConnectionPool();

    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> usedConnections;
    private final Logger logger = Logger.getLogger(CustomConnectionPool.class);

    private final int poolSize;

    CustomConnectionPool() {
        DbResourceManager dbResourceManager = DbResourceManager.getInstance();
        String url = dbResourceManager.getValue(DbConfigParameterName.URL);
        this.poolSize = Integer.parseInt(dbResourceManager.getValue(DbConfigParameterName.POOL_SIZE));

        Properties properties = new Properties();
        properties.put("user", dbResourceManager.getValue(DbConfigParameterName.USER));
        properties.put("password", dbResourceManager.getValue(DbConfigParameterName.PASSWORD));
        properties.put("serverTimezone", dbResourceManager.getValue(DbConfigParameterName.SERVER_TIMEZONE));
        properties.put("autoReconnect", dbResourceManager.getValue(DbConfigParameterName.AUTO_RECONNECT));
        properties.put("characterEncoding", dbResourceManager.getValue(DbConfigParameterName.CHARACTER_ENCODING));
        properties.put("useSSL", dbResourceManager.getValue(DbConfigParameterName.USE_SSL));

        try {
            Class.forName(dbResourceManager.getValue(DbConfigParameterName.DRIVER));
            freeConnections = new LinkedBlockingDeque<>(poolSize);
            usedConnections = new ArrayDeque<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                freeConnections.add(new ProxyConnection(DriverManager.getConnection(url, properties)));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e);
        }
    }

    public Connection getConnection() throws ConnectionException {
        Connection connection;
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
            freeConnections.offer(connection);
        } else {
            throw new ConnectionException("Error with release non proxy connection");
        }
    }

    public void destroyPool() throws ConnectionException {
        for (int i = 0; i < poolSize; i++) {
            try {
                ((ProxyConnection)freeConnections.take()).reallyClose();
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

