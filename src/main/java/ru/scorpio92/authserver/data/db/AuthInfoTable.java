package ru.scorpio92.authserver.data.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.scorpio92.authserver.data.db.base.AbstractTable;
import ru.scorpio92.authserver.data.model.AuthInfo;

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

    public void insertAuthInfo(AuthInfo authInfo) throws SQLException {
        List<String> columns = new ArrayList<>(Arrays.asList(ACCOUNT_ID_COLUMN, AUTH_TOKEN_COLUMN));
        PreparedStatement preparedStatement = getInsertStatement(columns, ACCOUNT_ID_COLUMN);
        preparedStatement.setInt(1, authInfo.getAccountId());
        preparedStatement.setString(2, authInfo.getAuthToken());
        preparedStatement.execute();
        closePreparedStatement(preparedStatement);
    }

    public void deleteAuthInfo(String authToken) throws SQLException {
        PreparedStatement preparedStatement = getDeleteStatement(AUTH_TOKEN_COLUMN);
        preparedStatement.setString(1, authToken);
        preparedStatement.execute();
        closePreparedStatement(preparedStatement);
    }

    public int getAccountIdByToken(String authToken) throws SQLException {
        PreparedStatement preparedStatement = getSelectStatement(Arrays.asList(ACCOUNT_ID_COLUMN), AUTH_TOKEN_COLUMN + " = ?");
        preparedStatement.setString(1, authToken);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(ACCOUNT_ID_COLUMN);
    }
}
