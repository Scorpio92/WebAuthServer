package ru.scorpio92.authserver.data.db;

import java.sql.SQLException;

import ru.scorpio92.authserver.data.db.base.AbstractTable;

public class AuthInfoTable extends AbstractTable {

    private final static String ACCOUNT_ID_COLUMN = "account_id";
    private final static String AUTH_TOKEN_COLUMN = "auth_token";
    private final static String AUTH_TOKEN_TIME_COLUMN = "token_create_timestamp";

    public AuthInfoTable() {
        super("auth_info");
    }

    public boolean checkAuthTokenExists(String authToken) throws SQLException {
        return checkColumnExists(AUTH_TOKEN_COLUMN, authToken);
    }
}
