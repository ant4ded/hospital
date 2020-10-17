package by.epam.hospital.connection;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceFactory {
    private static final String FILENAME = "db_prop.properties";

    private DataSourceFactory() {
    }

    public static MysqlDataSource createMysqlDataSource() throws ConnectionException {
        MysqlDataSource dataSource;
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(DataSourceFactory.class.
                getClassLoader().getResource(FILENAME).getFile())) {
            properties.load(inputStream);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty(DbPropName.URL));
            dataSource.setPort(Integer.parseInt(properties.getProperty(DbPropName.PORT)));
            dataSource.setUser(properties.getProperty(DbPropName.USER));
            dataSource.setPassword(properties.getProperty(DbPropName.PASSWORD));
            dataSource.setServerTimezone(properties.getProperty(DbPropName.SERVER_TIMEZONE));
            dataSource.setAutoReconnect(Boolean.parseBoolean(properties.getProperty(DbPropName.AUTO_RECONNECT)));
            dataSource.setCharacterEncoding(properties.getProperty(DbPropName.CHARACTER_ENCODING));
            dataSource.setUseSSL(Boolean.parseBoolean(properties.getProperty(DbPropName.USE_SSL)));
        } catch (NullPointerException | IOException e) {
            throw new ConnectionException("Properties file not found", e);
        } catch (SQLException e) {
            throw new ConnectionException("Invalid properties", e);
        }
        return dataSource;
    }
}

