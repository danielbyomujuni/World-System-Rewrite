package org.trainerlord.worldsystem;
//Local classes
import org.bukkit.plugin.Plugin;
import org.trainerlord.worldsystem.commands.CommandRegistry;
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
        getLogger().info("World System Enabled");

        //Creates All Nessiary Configuation Files
        configs.createConfigs();

        //Inits all Nessisary Commands
        //TODO Command Alisis

        this.getCommand("ws").setExecutor(new CommandRegistry());
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }


    public static Plugin getInstance() {
        return null;
    }

}
