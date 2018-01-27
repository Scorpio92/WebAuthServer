package ru.scorpio92.authserver.db.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by scorpio92 on 1/14/18.
 */

public class DBHelper {

    private static final String dbUrl = "jdbc:mysql://localhost:3306/chat";
    private static final String user = "server";
    private static final String password = "server";

    private static volatile Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (DBHelper.class) {
                if (connection == null)
                    connection = DriverManager.getConnection(dbUrl, user, password);
            }
        }

        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

}
