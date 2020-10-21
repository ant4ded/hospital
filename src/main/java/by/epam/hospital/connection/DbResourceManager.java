package by.epam.hospital.connection;

import java.util.ResourceBundle;

public class DbResourceManager {
    private static final String BUNDLE_NAME = "db_config";

    private static final DbResourceManager INSTANCE = new DbResourceManager();

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    public static DbResourceManager getInstance() {
        return INSTANCE;
    }

    public String getValue(String key) {
        return resourceBundle.getString(key);
    }
}
