package ru.scorpio92.authserver.data.db.base;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import ru.scorpio92.authserver.tools.Logger;

public abstract class AbstractTable {

    private final static String COMMA = ",";
    private final static String PARAM = "?";
    private final static String EQUALS = "=";
    private final static String WHERE = "where clause";
    private final static String EXCLUDED = "EXCLUDED.";

    private String tableName;

    public AbstractTable(String tableName) {
        this.tableName = tableName;
    }

    protected PreparedStatement getSelectStatement(List<String> columns, String whereClause) throws SQLException {
        String sqlTemplate = "SELECT * FROM table";

        if (columns != null) {
            String what = "";
            for (String column : columns) {
                what = what.concat(column).concat(COMMA);
            }
            what = what.substring(0, what.lastIndexOf(","));
            sqlTemplate = sqlTemplate.replace("*", what);
        }

        sqlTemplate = sqlTemplate.replace("table", tableName);
        if (whereClause != null) {
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
        while (resultSet.next()) {
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
        return getInsertStatement(columns, null);
    }

    protected PreparedStatement getInsertStatement(List<String> columns, String conflictColumn) throws SQLException {
        String sqlTemplate = "INSERT INTO table (names) VALUES (values)";
        sqlTemplate = sqlTemplate.replace("table", tableName);
        if (conflictColumn != null) {
            sqlTemplate = sqlTemplate.concat(" ON CONFLICT (conflict_column) DO UPDATE SET sets");
            sqlTemplate = sqlTemplate.replace("conflict_column", conflictColumn);
        }
        String names = "";
        String values = "";
        String conflictValues = "";
        for (String column : columns) {
            names = names.concat(column).concat(COMMA);
            values = values.concat(PARAM).concat(COMMA);
            if (conflictColumn != null)
                conflictValues = conflictValues.concat(column).concat(EQUALS).concat(EXCLUDED).concat(column).concat(COMMA);
        }
        names = names.substring(0, names.lastIndexOf(","));
        values = values.substring(0, values.lastIndexOf(","));
        if (conflictColumn != null)
            conflictValues = conflictValues.substring(0, conflictValues.lastIndexOf(","));
        sqlTemplate = sqlTemplate
                .replace("names", names)
                .replace("values", values)
                .replace("sets", conflictValues);
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
        if (whereClause != null) {
            sqlTemplate = sqlTemplate.concat(" ").concat(WHERE);
            sqlTemplate = sqlTemplate.replace("clause", whereClause);
        }
        return DBHelper.getPreparedStatement(sqlTemplate);
    }

    protected PreparedStatement getDeleteStatement(String clauseColumn) throws SQLException {
        String sqlTemplate = "DELETE FROM table WHERE ";
        sqlTemplate = sqlTemplate.replace("table", tableName);
        sqlTemplate = sqlTemplate.concat(clauseColumn).concat(EQUALS).concat(PARAM);
        return DBHelper.getPreparedStatement(sqlTemplate);
    }

    protected void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            preparedStatement.close();
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}