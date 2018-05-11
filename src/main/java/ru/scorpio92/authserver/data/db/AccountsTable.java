package ru.scorpio92.authserver.data.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.scorpio92.authserver.data.db.base.AbstractTable;
import ru.scorpio92.authserver.data.model.Account;

public class AccountsTable extends AbstractTable {

    private final static String ACCOUNT_ID_COLUMN = "account_id";
    private final static String LOGIN_COLUMN = "login";
    private final static String PASSWORD_HASH_COLUMN = "password_hash";
    private final static String NICKNAME_COLUMN = "nickname";
    private final static String EMAIL_COLUMN = "email";

    public AccountsTable() {
        super("accounts");
    }

    public boolean checkAccountExists(String login, String passwordHash) throws SQLException {
        PreparedStatement preparedStatement = getSelectStatement(Arrays.asList(ACCOUNT_ID_COLUMN), LOGIN_COLUMN + " = ? AND " + PASSWORD_HASH_COLUMN + " = ?");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, passwordHash);
        ResultSet resultSet = preparedStatement.executeQuery();
        int count = 0;
        while (resultSet.next()) {
            count++;
        }
        closePreparedStatement(preparedStatement);
        return count != 0;
    }


    public void insertAccount(Account account) throws SQLException {
        List<String> columns = new ArrayList<>(Arrays.asList(LOGIN_COLUMN, PASSWORD_HASH_COLUMN, NICKNAME_COLUMN));
        if (account.getEmail() != null)
            columns.add(EMAIL_COLUMN);

        PreparedStatement preparedStatement = getInsertStatement(columns);
        preparedStatement.setString(1, account.getLogin());
        preparedStatement.setString(2, account.getPasswordHash());
        preparedStatement.setString(3, account.getNickname());
        if (account.getEmail() != null)
            preparedStatement.setString(4, account.getEmail());

        preparedStatement.executeUpdate();
        closePreparedStatement(preparedStatement);
    }
}
