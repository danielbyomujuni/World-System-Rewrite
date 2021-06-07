package org.trainerlord.worldsystem.gui;



import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.trainerlord.worldsystem.configs.GuiConfig;
import org.trainerlord.worldsystem.gui.WorldSystemGUI;
import org.trainerlord.worldsystem.gui.clicklistener.ComingSoonClickListener;
import org.trainerlord.worldsystem.gui.clicklistener.CommandExecutorClickListener;
import org.trainerlord.worldsystem.gui.inventory.DependListener;
import org.trainerlord.worldsystem.gui.inventory.OrcInventory;
import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.trainerlord.worldsystem.gui.worldoptions.FireStatus;
import org.trainerlord.worldsystem.gui.worldoptions.TntStatus;
import org.trainerlord.worldsystem.wrapper.WorldPlayer;

import java.util.HashMap;
import java.util.UUID;
public class WorldOptionsGUI extends OrcInventory {
    public final static HashMap<UUID, String> data = new HashMap<>();
    private final static String path = "options.world.";

    public WorldOptionsGUI() {
        super(GuiConfig.getTitle(GuiConfig.getConfig(), "options.world"), GuiConfig.getRows("options.world"), GuiConfig.isFill("options.world"));

        loadItem("fire", "/ws fire", true, new FireStatus());
        loadItem("tnt", "/ws tnt", true, new TntStatus());

        if (!GuiConfig.isEnabled(path + "reset"))
            return;

        OrcItem item = GuiConfig.getItem(path + "reset");
        if (item != null) {
            item.setOnClick((p, inv, i) -> {
                p.closeInventory();
                p.chat("/ws reset");
            });
            addItem(GuiConfig.getSlot(path + "reset"), item);
        }

        if (GuiConfig.isEnabled(path + "back")) {
            OrcItem back = OrcItem.back.clone();
            back.setOnClick((p, inv, i) -> {
                p.closeInventory();
                p.openInventory(new WorldSystemGUI().getInventory(p));
            });
            addItem(GuiConfig.getSlot(path + "back"), back);
        }
    }

    public void loadItem(String subpath, String message, boolean state, DependListener depend) {
        if (!GuiConfig.isEnabled(path + subpath))
            return;
        OrcItem item = GuiConfig.getItem(path + subpath);
        if (item != null) {
            if (message == null) {
                item.setOnClick(new ComingSoonClickListener());
            } else {
                item.setOnClick(new CommandExecutorClickListener(message));
            }
            if (state) {
                if (depend == null) {
                    addItem(GuiConfig.getState(path + subpath), OrcItem.coming_soon.clone());
                } else {
                    addItem(GuiConfig.getState(path + subpath), OrcItem.disabled.clone().setDepend(depend));
                }
            }
            addItem(GuiConfig.getSlot(path + subpath), item);
        }
    }

    public void loadItem(String subpath, String message, boolean state) {
        loadItem(subpath, message, state, null);
    }

    public void loadItem(String subpath, boolean state) {
        loadItem(subpath, null, state);
    }

    @Override
    public Inventory getInventory(Player p, String title) {
        if (data.containsKey(p.getUniqueId()))
            return super.getInventory(p, title.replaceAll("%data", data.get(p.getUniqueId())));
        return super.getInventory(p, title);
    }

    @Override
    public Inventory getInventory(Player p) {
        if (data.containsKey(p.getUniqueId()))
            return super.getInventory(p, getTitle().replaceAll("%data", data.get(p.getUniqueId())));
        return super.getInventory(p, getTitle());
    }

    public boolean canOpen(Player p) {
        return new WorldPlayer(p).isOwnerofWorld();
    }

}
