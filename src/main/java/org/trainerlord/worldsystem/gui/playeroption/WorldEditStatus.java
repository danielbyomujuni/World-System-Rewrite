package org.trainerlord.worldsystem.gui.playeroption;

import org.trainerlord.worldsystem.gui.inventory.DependListener;
import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.trainerlord.worldsystem.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WorldEditStatus implements DependListener {
    private final WorldPlayer wp;

    public WorldEditStatus(WorldPlayer wp) {
        this.wp = wp;
    }

    public ItemStack getItemStack(Player p, WorldPlayer player) {
        return this.wp.canWorldedit() ? OrcItem.enabled.getItemStack(p, this.wp) : OrcItem.disabled.getItemStack(p, this.wp);
    }
}
