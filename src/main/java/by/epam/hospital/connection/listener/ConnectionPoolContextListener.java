package by.epam.hospital.connection.listener;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConnectionPoolContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(ConnectionPoolContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //noinspection ResultOfMethodCallIgnored
        ConnectionPool.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            logger.warn("Error destroying connection pool.", e);
        }
    }
}
