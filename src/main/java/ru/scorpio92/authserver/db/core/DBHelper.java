package ru.scorpio92.authserver.db.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ru.scorpio92.authserver.ServerConfigStore;

/**
 * Created by scorpio92 on 1/14/18.
 */

public class DBHelper {

    private static volatile Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (DBHelper.class) {
                if (connection == null)
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + ServerConfigStore.DB_NAME, ServerConfigStore.DB_USER_ROOT, ServerConfigStore.DB_PASSWORD_ROOT);
            }
        }

        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }
}
