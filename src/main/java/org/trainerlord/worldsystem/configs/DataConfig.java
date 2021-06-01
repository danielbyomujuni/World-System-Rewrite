package org.trainerlord.worldsystem.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.trainerlord.worldsystem.WorldSystem;
import org.trainerlord.worldsystem.objects.WorldSystemData;
import org.trainerlord.worldsystem.wrapper.PlayerWrapper;

import java.io.*;
import java.util.UUID;

/*  Notes:
Highest ID has Been Abandoned


 */


//TODO Clean Code
//TODO Document this Mess
//TODO More Comments
//TODO Fix Errors
//TODO Verify that Some Methods Are Nessisary or need Replaced

//GOAL: Complete Dataconfig

public class DataConfig {

    private Gson gson;
    private String WSuuid;
    private static WorldSystemData data;

    public static void verifyDataConfig() {
        try {
            data = readJSON();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                data = new WorldSystemData();
                writeJSON(data);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //TODO Add Error Prompt
        }
    }


    public DataConfig() {
        setConfig();
    }

    public DataConfig(String s) {
        verifyDataConfig();
        OfflinePlayer op = null;
        try {
            op = PlayerWrapper.getOfflinePlayer(UUID.fromString(s));
        } catch (Exception ignored) {
        }
        if (op == null) {
            op = PlayerWrapper.getOfflinePlayer(s);
        }
        do {
            WSuuid = getWSUUID(op.getUniqueId());
        } while (WSuuid == null);
    }

    public DataConfig(Player p) {
        verifyDataConfig();
        do {
            WSuuid = getWSUUID(p.getUniqueId());
        }while (WSuuid == null);
        refreshName();
    }

    public DataConfig(OfflinePlayer p) {
        verifyDataConfig();
       do {
            WSuuid = getWSUUID(p.getUniqueId());
        }while (WSuuid == null);
        refreshName();
    }

    public DataConfig(UUID uuid) {
        verifyDataConfig();
        do {
            WSuuid = getWSUUID(uuid);
        } while (WSuuid == null);
    }


    //TODO Update to GSON
    public static void checkWorlds() {
        verifyDataConfig();

        long deleteTime = 1000 * 60 * 60 * 24 * PluginConfig.deleteAfter();
        long now = System.currentTimeMillis();
        for (String s : cfg.getConfigurationSection("Dependences").getKeys(false)) {
            if (!cfg.isLong("Dependences." + s + ".last_loaded") && !cfg.isInt("Dependences." + s + ".last_loaded"))
                continue;
            long lastLoaded = cfg.getLong("Dependences." + s + ".last_loaded");
            long diff = now - lastLoaded;
            if (diff > deleteTime) {
                Bukkit.getConsoleSender().sendMessage(
                        PluginConfig.getPrefix() + "World of " + s + " was not loaded for too long. Deleting!");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ws delete " + s);
            }
        }
    }
    //TODO Update to GSON
    private void setConfig() {
        verifyDataConfig();

        cfg.set("HighestID", -1);
        try {
            cfg.save(dconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO Update to GSON
    public void refreshName() {
        if (hasWorld()) {
            verifyDataConfig();
            String uuid = this.uuid.toString();
            cfg.set("Dependences." + uuid + ".ActualName", PlayerWrapper.getOfflinePlayer(this.uuid).getName());
            try {
                cfg.save(dconfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //TODO Update to GSON
    //TODO Change System
    public void createNewEntry() {
        verifyDataConfig();
        String uuid = this.uuid.toString();
        int id = cfg.getInt("HighestID");
        id++;
        cfg.set("HighestID", id);
        cfg.set("Dependences." + uuid + ".ID", id);
        cfg.set("Dependences." + uuid + ".ActualName", PlayerWrapper.getOfflinePlayer(this.uuid).getName());
        try {
            cfg.save(dconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO Update to GSON
    //TODO Change System
    public boolean hasWorld() {
        verifyDataConfig();
        String uuid = this.uuid.toString();
        //Fix for #40
        return cfg.isInt("Dependences." + uuid + ".ID");
    }
    //TODO Update to GSON
    //TODO Change System
    public String getWorldname() {
        verifyDataConfig();
        String uuid = this.uuid.toString();
        int id = dcfg.getInt("Dependences." + uuid + ".ID");
        return "ID" + id + "-" + uuid;
    }
    //TODO Update to GSON
    //TODO Change System
    public String getWorldNameByOfflinePlayer() {
        String name;
        String uuid = this.uuid.toString();
        verifyDataConfig();
        if (cfg.getString("Dependences." + uuid + ".ActualName") == null) {
            name = "n";
        } else {
            name = "ID" + cfg.getInt("Dependences." + uuid + ".ID") + "-" + uuid;
        }
        return name;
    }
    //TODO Update to GSON
    //TODO Change System
    public void setLastLoaded() {
        File dconfig = new File("plugins//WorldSystem//dependence.yml");
        verifyDataConfig();
        cfg.set("Dependences." + uuid + ".last_loaded", System.currentTimeMillis());
        try {
            cfg.save(dconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO Update to GSON
    //TODO Change System
    public int getID() {
        verifyDataConfig();
        return dcfg.getInt("Dependences." + this.uuid.toString() + ".ID");
    }
    //TODO Change System
    public OfflinePlayer getOwner() {
        return PlayerWrapper.getOfflinePlayer(uuid);
    }

    /*
     *  New Spuplimental Code
     */

    //TODO Modify The Modifiers






    public static String getWSUUID(UUID playerUUID) {
        //TODO Make More Effienct
        if (data != null) {
            for (WorldSystemData.PlayerData player : data.players) {
                if (player.UUID == playerUUID.toString()) {
                    return null;
                }
            }
        }
        return null;
    }

    public static String createNewUUID() {
        String WSUUID = RandomStringUtils.random(8, "0123456789abcdef");

        if (data != null) {
            for (WorldSystemData.PlayerData player : data.players) {
                if (player.WSUUID == WSUUID) {
                    return null;
                }
            }
        }
        return WSUUID;
    }

    private static void writeJSON(WorldSystemData data) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter(WorldSystem.getPlugin(WorldSystem.class).getDataFolder() + "/WSData.json");
        writer.write(gson.toJson(data));
        writer.close();
    }

    private static WorldSystemData readJSON() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(WorldSystem.getPlugin(WorldSystem.class).getDataFolder() + "/WSData.json"));
        WorldSystemData wsData = gson.fromJson(bufferedReader, WorldSystemData.class);
        return wsData;
    }
}
