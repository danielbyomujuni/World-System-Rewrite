package org.trainerlord.worldsystem.gui.worldoptions;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.trainerlord.worldsystem.configs.DataConfig;
import org.trainerlord.worldsystem.configs.PluginConfig;
import org.trainerlord.worldsystem.gui.inventory.DependListener;
import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.trainerlord.worldsystem.wrapper.WorldPlayer;

import java.io.File;

public class FireStatus implements DependListener {
    @Override
    public ItemStack getItemStack(Player p, WorldPlayer wp) {
        String worldname = new DataConfig(p).getWorldname(0);//TODO MultiWorlds
        File file = new File(worldname + "/worldconfig.yml");
        if (!file.exists())
            file = new File(PluginConfig.getWorlddir() + "/worldconfig.yml");
        if (!file.exists())
            return null;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);//TODO GSON
        boolean b = cfg.getBoolean("Settings.Fire");
        if (b)
            return OrcItem.enabled.getItemStack(p);

        return null;
    }
}
