package ru.scorpio92.authserver.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import ru.scorpio92.authserver.db.core.AbstractTable;
import ru.scorpio92.authserver.entity.SessionKey;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class AuthInfoTable extends AbstractTable {

    public static final int ACTIVE_KEY = 1;
    public static final int NOT_ACTIVE_KEY = 0;

    private final static String AUTH_TOKEN_COLUMN = "authToken";
    private final static String SESSION_KEY_ID_COLUMN = "sessionKeyId";
    private final static String SESSION_KEY_COLUMN = "sessionKey";
    private final static String IV_COLUMN = "IV";
    private final static String ACTIVE_COLUMN = "active";
    private final static String KEY_TIME_COLUMN = "keyCreateTime";

    public AuthInfoTable() {
        super("AuthInfo");
    }

    public boolean checkKeyIsActive(String sessionKeyId) throws SQLException {
        PreparedStatement preparedStatement = getSelectStatement(Arrays.asList(ACTIVE_COLUMN), SESSION_KEY_ID_COLUMN + " = ?");
        preparedStatement.setString(1, sessionKeyId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        int active = resultSet.getInt(ACTIVE_COLUMN);
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return active == ACTIVE_KEY;
    }

    public boolean checkAuthToken(String authToken, String sessionKeyId) throws SQLException {
        PreparedStatement preparedStatement = getSelectStatement(Arrays.asList(AUTH_TOKEN_COLUMN), SESSION_KEY_ID_COLUMN + " = ?");
        preparedStatement.setString(1, sessionKeyId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        String authTokenFromDB = resultSet.getString(AUTH_TOKEN_COLUMN);
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return authToken.equals(authTokenFromDB);
    }

    public SessionKey getSessionKey(String sessionKeyId) throws SQLException {
        PreparedStatement preparedStatement = getSelectStatement(Arrays.asList(SESSION_KEY_COLUMN, IV_COLUMN), SESSION_KEY_ID_COLUMN + " = ?");
        preparedStatement.setString(1, sessionKeyId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        String key = resultSet.getString(SESSION_KEY_COLUMN);
        String IV = resultSet.getString(IV_COLUMN);
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return new SessionKey(sessionKeyId, key, IV);
    }

    public void disableKey(String sessionKeyId) throws SQLException {
        PreparedStatement preparedStatement = getUpdateStatement(Arrays.asList(ACTIVE_COLUMN), SESSION_KEY_ID_COLUMN + " = ?");
        preparedStatement.setInt(1, NOT_ACTIVE_KEY);
        preparedStatement.setString(2, sessionKeyId);
        preparedStatement.executeUpdate();
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private void insertAuthInfo(String authToken, SessionKey sessionKey) throws SQLException {
        PreparedStatement preparedStatement = getInsertStatement(Arrays.asList(AUTH_TOKEN_COLUMN, SESSION_KEY_ID_COLUMN, SESSION_KEY_COLUMN, IV_COLUMN, ACTIVE_COLUMN));
        preparedStatement.setString(1, authToken);
        preparedStatement.setString(2, sessionKey.getSessionKeyId());
        preparedStatement.setString(3, sessionKey.getSessionKey());
        preparedStatement.setString(4, sessionKey.getIV());
        preparedStatement.setInt(5, ACTIVE_KEY);
        preparedStatement.executeUpdate();
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private void updateAuthInfo(String authToken, SessionKey sessionKey) throws SQLException {
        PreparedStatement preparedStatement = getUpdateStatement(Arrays.asList(SESSION_KEY_ID_COLUMN, SESSION_KEY_COLUMN, IV_COLUMN, ACTIVE_COLUMN), AUTH_TOKEN_COLUMN + " = ?");
        preparedStatement.setString(1, sessionKey.getSessionKeyId());
        preparedStatement.setString(2, sessionKey.getSessionKey());
        preparedStatement.setString(3, sessionKey.getIV());
        preparedStatement.setInt(4, ACTIVE_KEY);
        preparedStatement.setString(5, authToken);
        preparedStatement.executeUpdate();
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void setAuthInfo(String authToken, SessionKey sessionKey) throws SQLException {
        if(checkColumnExists(AUTH_TOKEN_COLUMN, authToken)) {
            updateAuthInfo(authToken, sessionKey);
        } else {
            insertAuthInfo(authToken, sessionKey);
        }
    }


}
