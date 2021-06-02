package org.trainerlord.worldsystem.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
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

//TODO Done
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


    public static void checkWorlds() {
        verifyDataConfig();

        long deleteTime = 1000 * 60 * 60 * 24 * PluginConfig.deleteAfter();
        long now = System.currentTimeMillis();

        for (WorldSystemData.Worlds playerWorld : data.playerWorlds) {
            long lastLoaded = playerWorld.lastLoaded;
            long diff = now - lastLoaded;

            if (diff > deleteTime) {
                Bukkit.getConsoleSender().sendMessage(
                        PluginConfig.getPrefix() + "World of " + playerWorld.OWNERName + " was not loaded for too long. Deleting!");
                //TODO New Command System
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ws delete " + playerWorld.OWNERName + " " + playerWorld.worldNumber);
            }
        }
    }

    private void setConfig() {
        verifyDataConfig();
        try {
            writeJSON(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refreshName() {
        if (hasWorld()) {
            verifyDataConfig();
            for (WorldSystemData.Worlds playerWorld : data.playerWorlds) {
                    for (WorldSystemData.PlayerData player : data.players) {
                        if (playerWorld.OWNER == player.WSUUID) {
                            playerWorld.OWNERName = PlayerWrapper.getOfflinePlayer(UUID.fromString(player.UUID)).getName();
                        }

                    }
            }
            try {
                writeJSON(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createNewWorld() {
        verifyDataConfig();
        for (WorldSystemData.PlayerData player : data.players) {
            if (player.WSUUID == WSuuid) {
                data.playerWorlds.add(new WorldSystemData.Worlds(WSuuid, PlayerWrapper.getOfflinePlayer(UUID.fromString(player.UUID)).getName(),player.WorldCount));
                player.WorldCount++;
                try {
                    writeJSON(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    public boolean hasWorld() {
        verifyDataConfig();
        for (WorldSystemData.PlayerData player : data.players) {
            if (player.WSUUID == WSuuid) {
                return player.WorldCount > 0;
            }
        }
        return false;
    }

    public String getWorldname(int WorldNumber) {
        verifyDataConfig();

        for (WorldSystemData.Worlds playerWorld : data.playerWorlds) {
            if (playerWorld.OWNER == WSuuid) {
                return playerWorld.OWNER + "-" + WorldNumber;
            }
        }
        return null;
    }
    public void setLastLoaded() {
        verifyDataConfig();

        for (WorldSystemData.Worlds playerWorld : data.playerWorlds) {
            if (playerWorld.OWNER == WSuuid) {
                playerWorld.lastLoaded = System.currentTimeMillis();
            }
        }

        try {
            writeJSON(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO Change System
    public OfflinePlayer getOwner() {
        return PlayerWrapper.getOfflinePlayer(getUUIDfromWSUUID(WSuuid));
    }

    /*
     *  New Spuplimental Code
     */






    //TODO Modify The Modifiers





    private static String getWSWORLD(UUID playerUUID) {
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
    private static String getWSUUID(UUID playerUUID) {
        //TODO Make More Effienct
        if (data != null) {
            for (WorldSystemData.PlayerData player : data.players) {
                if (player.UUID == playerUUID.toString()) {
                    return player.WSUUID;
                }
            }
        }
        String newUUID = createNewWSUUID();
        WorldSystemData.PlayerData newPlayer;
        newPlayer = new WorldSystemData.PlayerData(playerUUID,newUUID,0);
        data.players.add(newPlayer);
        return newUUID;
    }

    private static UUID getUUIDfromWSUUID(String WSUUID) {
        for (WorldSystemData.PlayerData player : data.players) {
            if (player.WSUUID == WSUUID) {
                return UUID.fromString(player.UUID);
            }
        }
        return null;
    }

    private static String createNewWSUUID() {
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
