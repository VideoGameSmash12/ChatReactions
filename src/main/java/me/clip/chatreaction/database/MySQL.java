package me.clip.chatreaction.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

public class MySQL extends Database
{
    private String hostname = "localhost";
    private String portnmbr = "3306";
    private String username = "minecraft";
    private String password = "";
    private String database = "minecraft";

    private boolean ssl = false;

    public MySQL(String prefix, String hostname, String portnmbr, String database, String username, String password, boolean ssl)
    {
        super(prefix);
        this.hostname = hostname;
        this.portnmbr = portnmbr;
        this.database = database;
        this.username = username;
        this.password = password;
        this.ssl = ssl;
    }

    protected boolean initialize()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            return true;
        }
        catch (ClassNotFoundException e)
        {
            Bukkit.getLogger().severe("[ChatReaction] Class Not Found Exception: " + e.getMessage() + ".");
            return false;
        }
    }

    public Connection open()
    {
        open(true);
        return this.connection;
    }

    public Connection open(boolean showError)
    {
        if (initialize())
        {
            String url = "";

            try
            {
                url = "jdbc:mysql://" + this.hostname + ":" + this.portnmbr +
                        "/" + this.database + "?allowReconnect=true" + (this.ssl ? "&useSSL=true" : "");

                this.connection = DriverManager.getConnection(url, this.username, this.password);

                if (checkConnection())
                    this.connected = true;

                return this.connection;
            }
            catch (SQLException e)
            {
                if (showError)
                {
                    Bukkit.getLogger().severe("[ChatReaction] " + url);
                    Bukkit.getLogger().severe("[ChatReaction] Could not be resolved because of an SQL Exception: " + e.getMessage() + ".");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void close()
    {
        try
        {
            if (this.connection != null)
                this.connection.close();
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe("[ChatReaction] Failed to close database connection: " + e.getMessage());
        }
    }

    public Connection getConnection()
    {
        if (this.connection == null)
            return open();

        try
        {
            if (this.connection.isClosed())
            {
                return open();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return this.connection;
    }

    public boolean checkConnection()
    {
        return this.connection != null;
    }

    public ResultSet query(String query)
    {
        Statement statement = null;
        ResultSet result = null;

        try
        {
            for (int counter = 0; counter < 5 && result == null; counter++)
            {
                try
                {
                    statement = this.connection.createStatement();
                    result = statement.executeQuery("SELECT CURTIME()");
                }
                catch (SQLException e)
                {
                    if (counter == 4)
                    {
                        throw e;
                    }

                    if (e.getMessage().contains("connection closed"))
                    {
                        Bukkit.getLogger().severe("[ChatReaction] Error in SQL query. Attempting to reestablish connection. Attempt #" + (counter + 1) + "!");
                        open(false);
                    }
                    else
                    {
                        throw e;
                    }
                }
            }

            switch (getStatement(query))
            {
                case SELECT:
                    result = statement.executeQuery(query);
                    return result;
            }

            statement.executeUpdate(query);
            return result;
        }
        catch (SQLException e)
        {
            Bukkit.getLogger().severe("[ChatReaction] Error in SQL query: " + e.getMessage());

            return result;
        }
    }


    public PreparedStatement prepare(String query)
    {
        PreparedStatement ps = null;

        try
        {
            ps = this.connection.prepareStatement(query);
            return ps;
        }
        catch (SQLException e)
        {
            if (!e.toString().contains("not return ResultSet"))
            {
                Bukkit.getLogger().severe("[ChatReaction] Error in SQL prepare() query: " + e.getMessage());

            }

            return ps;
        }
    }

    public boolean createTable(String query)
    {
        Statement statement;

        try
        {
            if (query.equals("") || query == null)
            {
                Bukkit.getLogger().severe("[ChatReaction] SQL query empty: createTable(" + query + ")");
                return false;
            }

            statement = this.connection.createStatement();
            statement.execute(query);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe("[ChatReaction] " + e.getMessage());
            return false;
        }
    }

    public boolean checkTable(String table)
    {
        try
        {
            Statement statement = getConnection().createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM " + table);

            return result != null;
        }
        catch (SQLException e)
        {
            if (e.getMessage().contains("exist"))
            {
                return false;
            }

            Bukkit.getLogger().severe("[ChatReaction] Error in SQL query: " + e.getMessage());
        }

        return query("SELECT * FROM " + table) == null;
    }

    public boolean wipeTable(String table)
    {
        Statement statement;
        String query;

        try
        {
            if (!checkTable(table))
            {
                Bukkit.getLogger().severe("[ChatReaction] Error wiping table: \"" + table + "\" does not exist.");
                return false;
            }

            statement = getConnection().createStatement();
            query = "DELETE FROM " + table + ";";
            statement.executeUpdate(query);

            return true;
        }
        catch (SQLException e)
        {
            if (!e.toString().contains("not return ResultSet"))
            {
                return false;
            }
            return false;
        }
    }

    public String getCreateStatement(String table)
    {
        if (checkTable(table))
        {
            try
            {
                ResultSet result = query("SHOW CREATE TABLE " + table);
                result.next();
                return result.getString(2);
            }
            catch (Exception exception)
            {
            }
        }

        return "";
    }
}