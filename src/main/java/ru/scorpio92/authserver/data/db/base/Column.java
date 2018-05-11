package ru.scorpio92.authserver.data.db.base;

public class Column {

    private String columnName;
    private String columnValue;

    public Column(String columnName, String columnValue) {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnValue() {
        return columnValue;
    }
}
