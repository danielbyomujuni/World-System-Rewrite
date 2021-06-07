package org.trainerlord.worldsystem.gui.clicklistener;

import org.trainerlord.worldsystem.gui.inventory.OrcInventory;
import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.trainerlord.worldsystem.gui.inventory.OrcClickListener;
import org.bukkit.entity.Player;

public class ComingSoonClickListener implements OrcClickListener  {
    @Override
    public void onClick(Player p, OrcInventory inv, OrcItem item) {
        p.closeInventory();
        p.sendMessage("§cComing soon...");
    }
}
