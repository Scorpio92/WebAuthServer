package ru.scorpio92.authserver.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.scorpio92.authserver.db.core.AbstractTable;
import ru.scorpio92.authserver.entity.Account;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class AccountsTable extends AbstractTable {

    private final static String ACCOUNT_ID_COLUMN = "accountId";
    private final static String NICKNAME_COLUMN = "nickname";
    private final static String AUTH_TOKEN_COLUMN = "authToken";
    private final static String AUTH_TOKEN_TIME_COLUMN = "tokenCreateTime";
    private final static String EMAIL_COLUMN = "email";

    public AccountsTable() {
        super("Accounts");
    }

    public boolean checkAccountExists(String nickname) throws SQLException {
        return checkColumnExists(NICKNAME_COLUMN, nickname);
    }

    public boolean checkAuthTokenExists(String authToken) throws SQLException {
        return checkColumnExists(AUTH_TOKEN_COLUMN, authToken);
    }

    public void insertAccount(Account account) throws SQLException {
        List<String> columns = new ArrayList<>(Arrays.asList(NICKNAME_COLUMN, AUTH_TOKEN_COLUMN));
        if(account.getEmail() != null)
            columns.add(EMAIL_COLUMN);

        PreparedStatement preparedStatement = getInsertStatement(columns);
        preparedStatement.setString(1, account.getNickname());
        preparedStatement.setString(2, account.getAuthToken());
        if(account.getEmail() != null)
            preparedStatement.setString(3, account.getEmail());

        preparedStatement.executeUpdate();
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
