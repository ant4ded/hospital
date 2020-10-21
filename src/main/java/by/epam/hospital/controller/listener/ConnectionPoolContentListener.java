package by.epam.hospital.controller.listener;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionPool;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConnectionPoolContentListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(ConnectionPoolContentListener.class);

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
