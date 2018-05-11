package ru.scorpio92.authserver.data.db.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ru.scorpio92.authserver.ServerConfigStore;

public class DBHelper {

    private static volatile Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (DBHelper.class) {
                if (connection == null)
                    connection = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/" + ServerConfigStore.DB_NAME,
                            ServerConfigStore.DB_USER,
                            ServerConfigStore.DB_PASSWORD
                    );
            }
        }

        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }
}
