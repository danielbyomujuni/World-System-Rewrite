package org.trainerlord.worldsystem.configs;

//Local classes

import org.bukkit.World;
import org.trainerlord.worldsystem.WorldSystem;
//Minecraft Related classes

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.GameMode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.trainerlord.worldsystem.utils.PlayerPositions;

//Java classes

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


//TODO Complete
//TODO Clean Code
//TODO Document this Mess
//TODO More Comments
//TODO Finish TODOS



public class PluginConfig {

    // ---Global Varibles---//
    private final static GameMode[] gameModes = new GameMode[]{GameMode.SURVIVAL, GameMode.CREATIVE,
        GameMode.ADVENTURE, GameMode.SPECTATOR};

    private static File file;
    

    // ---Methods---//

    /* Contructor */
    private PluginConfig() {
    }

    /****************
     * Check Config *
     * 
     * @param f *
     ***************/
    // Verifies That the Config is Valid
    public static void checkConfig(File f) {
        file = f;
        if (file.exists()) {
            YamlConfiguration cfg = getConfig();

            //This Runs if at least one data type is incorrect
            // to either create a new config or to repair the old one
            if (!verifyConfigDataTypes(cfg)) {
                try {
                    Files.copy(file.toPath(),
                            new File(file.getParentFile(), "config-broken-"
                                    + new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date()) + ".yml").toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(file.toPath());
                    System.err.println("[WorldSystem] Config is broken, creating a new one!");
                    checkConfig(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource("config.yml");
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    System.err.println("Wasn't able to create Config");
                    e.printStackTrace();
                }
            }

            if (getSpawn(null).getWorld() == null) {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + "§cWorld is null in spawn.world!");
            }

        }
    }

    /***************************
     * Verify Config DataTypes *
     ***************************/
    //
    //This Method Makes Sure that all the
    // datatypes are correct in the config file
    //
    public static Boolean verifyConfigDataTypes(YamlConfiguration cfg) {


        //Verify INTS
        if (!cfg.isInt("unloadingtime") &&
                !cfg.isInt("player.gamemode") &&
                !cfg.isInt("request_expires") &&
                !cfg.isInt("delete_after") &&
                !cfg.isInt("database.mysql_settings.port") &&
                !cfg.isInt("lagsystem.period_in_seconds") &&
                !cfg.isInt("lagsystem.period_in_seconds") &&
                !cfg.isInt("lagsystem.entities_per_world") &&
                !cfg.isInt("lagsystem.garbagecollector.period_in_minutes") &&
                !cfg.isInt("spawn.gamemode")) {
            return false;
        }
        //Verify Doubles
        if (!(cfg.isDouble("spawn.spawnpoint.x") || cfg.isInt("spawn.spawnpoint.x")) &&
                !(cfg.isDouble("spawn.spawnpoint.y") || cfg.isInt("spawn.spawnpoint.y")) &&
                !(cfg.isDouble("spawn.spawnpoint.z") || cfg.isInt("spawn.spawnpoint.z")) &&
                !(cfg.isDouble("spawn.spawnpoint.yaw") || cfg.isInt("spawn.spawnpoint.yaw")) &&
                !(cfg.isDouble("spawn.spawnpoint.pitch") || cfg.isInt("spawn.spawnpoint.pitch")) &&
                !(cfg.isDouble("worldspawn.spawnpoint.x") || cfg.isInt("worldspawn.spawnpoint.x")) &&
                !(cfg.isDouble("worldspawn.spawnpoint.y") || cfg.isInt("worldspawn.spawnpoint.y")) &&
                !(cfg.isDouble("worldspawn.spawnpoint.z") || cfg.isInt("worldspawn.spawnpoint.z")) &&
                !(cfg.isDouble("worldspawn.spawnpoint.yaw") || cfg.isInt("worldspawn.spawnpoint.yaw")) &&
                !(cfg.isDouble("worldspawn.spawnpoint.pitch") || cfg.isInt("worldspawn.spawnpoint.pitch"))) {
            return false;
        }
        //Verify Strings
        if (!cfg.isString("worldfolder") &&
                !cfg.isString("language") &&
                !cfg.isString("prefix") &&
                !cfg.isString("worldtemplates.default") &&
                !cfg.isString("database.type") &&
                !cfg.isString("database.worlds_table_name") &&
                !cfg.isString("database.players_table_name") &&
                !cfg.isString("database.mysql_settings.host") &&
                !cfg.isString("database.mysql_settings.username") &&
                !cfg.isString("database.mysql_settings.password") &&
                !cfg.isString("database.mysql_settings.database") &&
                !cfg.isString("database.sqlite_settings.file") &&
                !cfg.isString("spawn.spawnpoint.world")) {
            return false;
        }
        //Verify Booleans
        if (!cfg.isBoolean("seperate_inventory") &&
                !cfg.isBoolean("need_confirm") &&
                !cfg.isBoolean("contact_authserver") &&
                !cfg.isBoolean("spawn_teleportation") &&
                !cfg.isBoolean("worldtemplates.multi_choose") &&
                !cfg.isBoolean("load_worlds_async") &&
                !cfg.isBoolean("lagsystem.garbagecollector.use") &&
                !cfg.isBoolean("spawn.spawnpoint.use_last_location") &&
                !cfg.isBoolean("worldspawn.use") &&
                !cfg.isBoolean("worldspawn.use_last_location")) {
            return false;
        }
        return true;
    }

    public static YamlConfiguration getConfig() {
        try {
            return YamlConfiguration
                    .loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Cannot access config file");
    }

    // ---Properties---//
    // Checks to enable 'Garbage Collector'
        //TODO Garbage Collector DOCUMENTATION
        //TODO LagSystem DOCUMENTATION
    public static boolean useGC() {
        return getConfig().getBoolean("lagsystem.garbagecollector.use", false);
    }

    // Gets The 'Garbage Collector Time Period'
        //TODO Garbage Collector DOCUMENTATION
        //TODO LagSystem DOCUMENTATION
    public static int getGCPeriod() {
        return getConfig().getInt("lagsystem.garbagecollector.period_in_minutes", 5);
    }

    // Gets The 'World Entity Limit'
        //TODO EnityLimit DOCUMENTATION
        //TODO LagSystem DOCUMENTATION
    public static int getEntitysPerWorld() {
        return getConfig().getInt("lagsystem.entities_per_world", 350);
    }

    // Gets The 'Lag Detector Time Period'
        //TODO Lag Detector DOCUMENTATION
        //TODO LagSystem DOCUMENTATION
    public static int getLagCheckPeriod() {
        return getConfig().getInt("lagsystem.period_in_seconds", 10);
    }

    // Checks to use 'World Spawn'
        //TODO world config DOCUMENTATION
    public static boolean useWorldSpawn() {
        return getConfig().getBoolean("worldspawn.use", true);
    }
    // Gets The 'Gamemode'
        //TODO world config DOCUMENTATION
        //TODO list each Number's meaning
            /*
                0 SURVIVAL, 
                1 CREATIVE,
                2 ADVENTURE, 
                3 SPECTATOR
            */
    public static GameMode getGamemode() {
        return gameModes[getConfig().getInt("player.gamemode", 0)];
    }
    // Checks for Seperate Inventories
        //TODO world config DOCUMENTATION
        //TODO Cheating Warnings
    public static boolean isInventoriesSeperate() {
        return getConfig().getBoolean("player.seperate_inventory", true);
    }

    // Gets The 'World Unload Time'
        //TODO world config DOCUMENTATION
        //TODO Set a Hard Limit
            //The Hard Limit is to prevent worlds from not being saved
    public static int getUnloadingTime() {
        return getConfig().getInt("unloadingtime", 20);
    }

    // Gets The 'Hub World gamemode'
        //TODO World Tranfer DOCUMENTATION
        //TODO Less Confustion Nomenclature
        //TODO list each Number's meaning
            /*
                0 SURVIVAL, 
                1 CREATIVE,
                2 ADVENTURE, 
                3 SPECTATOR
            */
    public static GameMode getSpawnGamemode() {
        return gameModes[getConfig().getInt("spawn.gamemode", 2)];
    }

    // Gets The 'World Storage Locations'
        //TODO World Config DOCUMENTATION
        //? Maybe Useless
    public static String getWorlddir() {
        return getConfig().getString("worldfolder", "plugins/WorldSystem/Worlds") + "/";
    }

    // Allows For The Player to choose from diffrent Template Worlds
        //TODO World Config DOCUMENTATION
        //TODO Make it Permission Based
            //TODO Allow it to be unlocked based on permsiion
            // Authors Note: Good MCRPG home locations unlock as game progresseses
        //TODO Less Confustion Nomenclature
    public static boolean isMultiChoose() {
        return getConfig().getBoolean("worldtemplates.multi_choose", false);
    }
    // Gets the Main Template
        //TODO World Config DOCUMENTATION
        //TODO Make it Permission Based
            //ie make it so it can change based on rank/permission
        //TODO Less Confustion Nomenclature
    public static String getDefaultWorldTemplate() {
        return getConfig().getString("worldtemplates.default", "");
    }

    // Gets Language
        //TODO Language DOCUMENTATION
        //TODO Get Translaters
    public static String getLanguage() {
        return getConfig().getString("language", "en");
    }
    // Gets World System Prefix
        //TODO other? DOCUMENTATION
    public static String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "§8[§3WorldSystem§8] §6"));
    }

    // Gets the WorldSystem Player World SpawnPoint 
        //TODO other? DOCUMENTATION
        //TODO DOCUMENTATION Glossary
    public static Location getWorldSpawn(World w) {
        return getLocation(getConfig(), "worldspawn.spawnpoint", w);
    }


    //Gets Hub World Spawn
        //TODO World Tranfer DOCUMENTATION
        //TODO Write This Down on a Seperate Paper to I have it Organized
            //OR Trello
    public static Location getSpawn(Player player) {
        YamlConfiguration cfg = getConfig();
        Location location = getLocation(cfg, "spawn.spawnpoint", Bukkit.getWorld(cfg.getString("spawn.spawnpoint.world", "world")));
        return PlayerPositions.instance.injectPlayersLocation(player, location);
    }


    //Gets The Require Experation Time
    //TODO World Tranfer DOCUMENTATION
    //TODO Alot of things
    public static int getRequestExpire() {
        return getConfig().getInt("request_expires", 20);
    }

    //Gets The location to go to in the world
    //TODO World Tranfer DOCUMENTATION
    private static Location getLocation(YamlConfiguration cfg, String path, World world) {
        return new Location(world, cfg.getDouble(path + ".x", 0), cfg.getDouble(path + ".y", 20),
                cfg.getDouble(path + ".z", 0), (float) cfg.getDouble(path + ".yaw", 0),
                (float) cfg.getDouble(path + ".pitch", 0));
    }

    //Checks if an action needs user confirmation
    //TODO World Admin DOCUMENTATION
    public static boolean confirmNeed() {
        return getConfig().getBoolean("need_confirm", true);
    }

    //Checks Mojang Auth Database
    //TODO Server Admin DOCUMENTATION
    //TODO Remove?
    public static boolean contactAuth() {
        return getConfig().getBoolean("contact_authserver", true);
    }

    //Checks if to force spawn teleportation
    //TODO Server Admin DOCUMENTATION
    public static boolean spawnTeleportation() {
        return getConfig().getBoolean("spawn_teleportation", true);
    }


    //Checks if should delete world
    //TODO World Admin DOCUMENTATION
    public static boolean shouldDelete() {
        return getConfig().getInt("delete_after") != -1;
    }

    //Checks if should delete after X seconds world
    //TODO Server Admin DOCUMENTATION
    public static long deleteAfter() {
        return getConfig().getLong("delete_after");
    }

    //Checks if to force last location teleportation
    //TODO Server Admin DOCUMENTATION
    public static boolean useWorldSpawnLastLocation() {
        return getConfig().getBoolean("worldspawn.use_last_location");
    }


    //Checks if to force world spawn teleportation
    //TODO Server Admin DOCUMENTATION
    public static boolean useSpawnLastLocation() {
        return getConfig().getBoolean("spawn.spawnpoint.use_last_location");
    }

    //Gets the world Table Name
    //TODO Server Admin DOCUMENTATION
    public static String getWorldsTableName() {
        return getConfig().getString("database.worlds_table_name");
    }

    //Gets the player Table Name
    //TODO Server Admin DOCUMENTATION
    public static String getPlayersTableName() {
        return getConfig().getString("database.players_table_name");
    }

    //Gets the player UUID Table Name
    //TODO Server Admin DOCUMENTATION
    //TODO Remove?
    public static String getUUIDTableName() {
        return getConfig().getString("database.players_uuids");
    }

    //Gets Database Type
    //TODO Server Admin DOCUMENTATION
    public static String getDatabaseType() {
        return getConfig().getString("database.type");
    }

    //Gets SQLDatabase File Path
    //TODO Server Admin DOCUMENTATION
    public static String getSqliteFile() {
        return getConfig().getString("database.sqlite_settings.file");
    }
    //Gets SQLDatabase File Host
    //TODO Server Admin DOCUMENTATION
    public static String getMysqlHost() {
        return getConfig().getString("database.mysql_settings.host");
    }

    //Gets SQLDatabase File Port
    //TODO Server Admin DOCUMENTATION
    public static int getMysqlPort() {
        return getConfig().getInt("database.mysql_settings.port");
    }

    //Gets SQLDatabase Username
    //TODO Server Admin DOCUMENTATION
    //TODO Safety Warning
    public static String getMysqlUser() {
        return getConfig().getString("database.mysql_settings.username");
    }

    //Gets SQLDatabase Password
    //TODO Server Admin DOCUMENTATION
    //TODO Safety Warning
    public static String getMysqlPassword() {
        return getConfig().getString("database.mysql_settings.password");
    }


    //Gets SQLDatabase Name
    //TODO Server Admin DOCUMENTATION
    public static String getMysqlDatabaseName() {
        return getConfig().getString("database.mysql_settings.database");
    }

    //Checks to load worlds Async
    //TODO World Gen DOCUMENTATION
    //TODO Remove?
    public static boolean loadWorldsASync() {
        return getConfig().getBoolean("load_worlds_async");
    }
}
