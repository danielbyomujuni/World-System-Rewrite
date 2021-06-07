package org.trainerlord.worldsystem.gui.playeroption;

import org.trainerlord.worldsystem.gui.inventory.DependListener;
import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.trainerlord.worldsystem.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeleportStatus implements DependListener {

    private final WorldPlayer wp;

    public TeleportStatus(WorldPlayer wp) {
        this.wp = wp;
    }

    @Override
    public ItemStack getItemStack(Player p, WorldPlayer player) {
        return wp.canTeleport() ? OrcItem.enabled.getItemStack(p, wp) : OrcItem.disabled.getItemStack(p, wp);
    }
}
