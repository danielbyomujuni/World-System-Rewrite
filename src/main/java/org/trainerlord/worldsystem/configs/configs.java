package org.trainerlord.worldsystem.configs;

//---Imports---//

//Local classes
import org.trainerlord.worldsystem.WorldSystem;

//Minecraft Related classes
import org.bukkit.Bukkit;

//Java classes
import java.io.File;


//TODO Clean Code
//TODO Document this Mess
//TODO More Comments
//TODO Fix Errors

public class configs {

    //---Global Varibles---//
    private static File dir;
    private static File config;
    private static File dconfig;
    private static File languages;
    private static File gui;

    //---Methods---//
    public static void createConfigs() {
        
        File folder = WorldSystem.getPlugin(WorldSystem.class).getDataFolder();
        dir = new File(folder + "/worldsources");
        config = new File(folder, "config.yml");
        dconfig = new File(folder, "dependence.yml");
        languages = new File(folder + "/languages");
        gui = new File(folder, "gui.yml");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        PluginConfig.checkConfig(config);
    }

    /*************************
     * Create Language Files *
     *************************/

     public static void createLanguageFiles() {
        if (!languages.exists())
        languages.mkdirs();

    PluginConfig.checkConfig(config);
    MessageConfig.checkConfig(new File(languages, "en.yml"));
    MessageConfig.checkConfig(new File(languages, "de.yml"));
    MessageConfig.checkConfig(new File(languages, "hu.yml"));
    MessageConfig.checkConfig(new File(languages, "nl.yml"));
    MessageConfig.checkConfig(new File(languages, "pl.yml"));
    MessageConfig.checkConfig(new File(languages, "es.yml"));
    MessageConfig.checkConfig(new File(languages, "ru.yml"));
    MessageConfig.checkConfig(new File(languages, "fi.yml"));
    MessageConfig.checkConfig(new File(languages, "ja.yml"));
    MessageConfig.checkConfig(new File(languages, "zh.yml"));
    MessageConfig.checkConfig(new File(languages, "fr.yml"));
    MessageConfig.checkConfig(new File(languages, PluginConfig.getLanguage() + ".yml"));

     }
}
