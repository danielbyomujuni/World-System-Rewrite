package org.trainerlord.worldsystem.configs.database;

import org.trainerlord.worldsystem.configs.PluginConfig;
import lombok.Getter;


//TODO Complete
//TODO Clean Code
//TODO Document this Mess
//TODO More Comments
public class DatabaseProvider {
    @Getter
    public static DatabaseProvider instance = new DatabaseProvider();

    @Getter
    public final DatabaseUtil util;

    private DatabaseProvider() {
        String dbType = PluginConfig.getDatabaseType();
        if (dbType.equalsIgnoreCase("sqlite"))
            util = new SqliteConnection();
        else if (dbType.equalsIgnoreCase("mysql"))
            util = new MysqlConnection();
        else {
            throw new IllegalArgumentException("Unknown database type: " + dbType);
        }
    }
}

