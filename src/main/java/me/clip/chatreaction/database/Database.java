package me.clip.chatreaction.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class Database
{
    protected final String PREFIX;
    protected boolean connected;
    protected Connection connection;

    protected enum Statements
    {
        SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, CREATE, ALTER, DROP, TRUNCATE, RENAME;
    }

    public Database(String prefix)
    {
        this.PREFIX = prefix;
        this.connected = false;
        this.connection = null;
    }

    public String getTablePrefix()
    {
        return this.PREFIX;
    }

    abstract boolean initialize();

    public abstract Connection open();

    public abstract void close();

    public abstract Connection getConnection();

    public abstract boolean checkConnection();

    public abstract ResultSet query(String paramString);

    public abstract PreparedStatement prepare(String paramString);

    protected Statements getStatement(String query)
    {
        String trimmedQuery = query.trim();
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("SELECT"))
            return Statements.SELECT;
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("INSERT"))
            return Statements.INSERT;
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("UPDATE"))
            return Statements.UPDATE;
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("DELETE"))
            return Statements.DELETE;
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("CREATE"))
            return Statements.CREATE;
        if (trimmedQuery.substring(0, 5).equalsIgnoreCase("ALTER"))
            return Statements.ALTER;
        if (trimmedQuery.substring(0, 4).equalsIgnoreCase("DROP"))
            return Statements.DROP;
        if (trimmedQuery.substring(0, 8).equalsIgnoreCase("TRUNCATE"))
            return Statements.TRUNCATE;
        if (trimmedQuery.substring(0, 6).equalsIgnoreCase("RENAME"))
            return Statements.RENAME;
        if (trimmedQuery.substring(0, 2).equalsIgnoreCase("DO"))
            return Statements.DO;
        if (trimmedQuery.substring(0, 7).equalsIgnoreCase("REPLACE"))
            return Statements.REPLACE;
        if (trimmedQuery.substring(0, 4).equalsIgnoreCase("LOAD"))
            return Statements.LOAD;
        if (trimmedQuery.substring(0, 7).equalsIgnoreCase("HANDLER"))
            return Statements.HANDLER;
        if (trimmedQuery.substring(0, 4).equalsIgnoreCase("CALL"))
        {
            return Statements.CALL;
        }
        return Statements.SELECT;
    }

    public abstract boolean createTable(String paramString);

    public abstract boolean checkTable(String paramString);

    public abstract boolean wipeTable(String paramString);

    public abstract String getCreateStatement(String paramString);
}