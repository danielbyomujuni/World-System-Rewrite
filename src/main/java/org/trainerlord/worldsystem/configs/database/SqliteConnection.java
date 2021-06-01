package org.trainerlord.worldsystem.configs.database;


import org.trainerlord.worldsystem.configs.PluginConfig;
import org.bukkit.Bukkit;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

//TODO Complete
//TODO Clean Code
//TODO Document this Mess
//TODO More Comments


public class SqliteConnection extends DatabaseConnection {

    private void connect(String file) {
        synchronized (lock) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] Drivers are not working properly");
                return;
            }
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + file);
                Bukkit.getLogger().log(Level.INFO, "[WorldSystem | SQLite] Connected to local file database");
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] Failed to connect with given server:");
                e.printStackTrace();
            }
        }
    }

    public void connect() {
        connect(PluginConfig.getSqliteFile());
    }
}