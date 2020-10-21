package by.epam.hospital.connection;

public class DbParameterName {
    public static final String CONFIG_PREFIX = "db.";

    public static final String CONNECTION_URL = "url";
    public static final String CONNECTION_DRIVER = "driver";
    public static final String CONNECTION_USER = "user";
    public static final String CONNECTION_PASSWORD = "password";
    public static final String CONNECTION_SERVER_TIMEZONE = "serverTimezone";
    public static final String CONNECTION_AUTO_RECONNECT = "autoReconnect";
    public static final String CONNECTION_CHARACTER_ENCODING = "characterEncoding";
    public static final String CONNECTION_USE_SSL = "useSSL";
    public static final String CONNECTION_POOL_SIZE = "poolSize";

    public static final String CONFIG_URL = CONFIG_PREFIX + CONNECTION_URL;
    public static final String CONFIG_DRIVER = CONFIG_PREFIX + CONNECTION_DRIVER;
    public static final String CONFIG_USER = CONFIG_PREFIX + CONNECTION_USER;
    public static final String CONFIG_PASSWORD = CONFIG_PREFIX + CONNECTION_PASSWORD;
    public static final String CONFIG_SERVER_TIMEZONE = CONFIG_PREFIX + CONNECTION_SERVER_TIMEZONE;
    public static final String CONFIG_AUTO_RECONNECT = CONFIG_PREFIX + CONNECTION_AUTO_RECONNECT;
    public static final String CONFIG_CHARACTER_ENCODING = CONFIG_PREFIX + CONNECTION_CHARACTER_ENCODING;
    public static final String CONFIG_USE_SSL = CONFIG_PREFIX + CONNECTION_USE_SSL;
    public static final String CONFIG_POOL_SIZE = CONFIG_PREFIX + CONNECTION_POOL_SIZE;

    private DbParameterName() {
    }
}
