package org.trainerlord.worldsystem;
//Local classes
import org.trainerlord.worldsystem.configs.configs;

//Minecraft Related classes
import org.bukkit.plugin.java.JavaPlugin;

//Java classes

public class WorldSystem extends JavaPlugin {
    //ANCHOR Main Method

    /***************
     * Main Method *
     ***************/
    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
        configs.createConfigs();
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

}
