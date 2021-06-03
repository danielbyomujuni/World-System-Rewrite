package org.trainerlord.worldsystem.wrapper;


import org.trainerlord.worldsystem.guiInventory.OrcItem;
import org.trainerlord.worldsystem.configs.GuiConfig;

import lombok.Getter;

//TODO Clean Code
//TODO Document this Mess
//TODO More Comments
//TODO Fix Errors
public class WorldTemplate {
    public final String name;
    public final String permission;
    public final OrcItem icon;
    public final int slot;
    public final int cost;
    public final GeneratorSettings generatorSettings;

    public WorldTemplate(String name, String permission, int cost, GeneratorSettings generatorSettings) {
        this.name = name;
        this.permission = permission;
        this.cost = cost;
        this.generatorSettings = generatorSettings;

        this.icon = GuiConfig.getItem("worldchoose." + name);
        this.slot = GuiConfig.getSlot("worldchoose." + name);

        icon.setOnClick((p, inv, item) -> {
            p.closeInventory();
            p.chat("/ws get " + name);
        });
    }

    public int getSlot() {
        return slot;
    }

    public int getCost() {
        return cost;
    }

    public OrcItem getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return "plugins/WorldSystem/worldsources/" + name;
    }

    public String getPermission() {
        return permission;
    }
}
