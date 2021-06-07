package org.trainerlord.worldsystem.gui.inventory;

import org.trainerlord.worldsystem.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DependListener {

    ItemStack getItemStack(Player p, WorldPlayer wp);

}