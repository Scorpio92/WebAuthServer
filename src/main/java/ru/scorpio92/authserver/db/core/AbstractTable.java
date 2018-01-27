package ru.scorpio92.authserver.db.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by scorpio92 on 1/14/18.
 */

public abstract class AbstractTable {

    private final static String COMMA = ",";
    private final static String PARAM = "?";
    private final static String EQUALS = "=";
    private final static String WHERE = "where clause";

    private String tableName;

    public AbstractTable(String tableName) {
        this.tableName = tableName;
    }

    protected PreparedStatement getSelectStatement(List<String> columns, String whereClause) throws SQLException {
        String sqlTemplate = "SELECT * FROM table";

        if(columns != null) {
            String what = "";
            for (String column : columns) {
                what = what.concat(column).concat(COMMA);
            }
            what = what.substring(0, what.lastIndexOf(","));
            sqlTemplate = sqlTemplate.replace("*", what);
        }

        sqlTemplate = sqlTemplate.replace("table", tableName);
        if(whereClause != null) {
            sqlTemplate = sqlTemplate.concat(" ").concat(WHERE);
            sqlTemplate = sqlTemplate.replace("clause", whereClause);
        }

        return DBHelper.getPreparedStatement(sqlTemplate);
    }

    protected boolean checkColumnExists(String column, String value) throws SQLException {
        PreparedStatement preparedStatement = getSelectStatement(Arrays.asList(column), column + " = ?");
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        int count = 0;
        while (resultSet.next()){
            count++;
        }
        try {
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return count != 0;
    }

    protected PreparedStatement getInsertStatement(List<String> columns) throws SQLException {
        String sqlTemplate = "INSERT INTO table (names) VALUES (values)";
        sqlTemplate = sqlTemplate.replace("table", tableName);
        String names = "";
        String values = "";
        for (String column : columns) {
            names = names.concat(column).concat(COMMA);
            values = values.concat(PARAM).concat(COMMA);
        }
        names = names.substring(0, names.lastIndexOf(","));
        values = values.substring(0, values.lastIndexOf(","));
        sqlTemplate = sqlTemplate.replace("names", names).replace("values", values);
        return DBHelper.getPreparedStatement(sqlTemplate);
    }

    protected PreparedStatement getUpdateStatement(List<String> columns, String whereClause) throws SQLException {
        String sqlTemplate = "UPDATE table SET values";
        sqlTemplate = sqlTemplate.replace("table", tableName);
        String values = "";
        for (String column : columns) {
            values = values.concat(column).concat(EQUALS).concat(PARAM).concat(COMMA);
        }
        values = values.substring(0, values.lastIndexOf(","));
        sqlTemplate = sqlTemplate.replace("values", values);
        if(whereClause != null) {
            sqlTemplate = sqlTemplate.concat(" ").concat(WHERE);
            sqlTemplate = sqlTemplate.replace("clause", whereClause);
        }
        return DBHelper.getPreparedStatement(sqlTemplate);
    }
}
