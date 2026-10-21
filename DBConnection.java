package com.shopsphere.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBConnection provides centralized JDBC database connectivity.
 * Reads configuration from db.properties with environment variable overrides.
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final Properties props = new Properties();

    static {
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                LOGGER.warning("db.properties not found on classpath, using defaults.");
                props.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
                props.setProperty("db.username", "root");
                props.setProperty("db.password", "root");
            }
            Class.forName(props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load database driver or properties", e);
        }
    }

    private DBConnection() {
        // Utility class
    }

    /**
     * Obtains a new database connection.
     * @return Connection instance
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        String url = System.getenv("SHOPSPHERE_DB_URL");
        if (url == null || url.trim().isEmpty()) {
            url = props.getProperty("db.url");
        }

        String username = System.getenv("SHOPSPHERE_DB_USER");
        if (username == null || username.trim().isEmpty()) {
            username = props.getProperty("db.username");
        }

        String password = System.getenv("SHOPSPHERE_DB_PASS");
        if (password == null) {
            password = props.getProperty("db.password", "");
        }

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Safely closes an AutoCloseable resource.
     * @param closeable resource to close
     */
    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
