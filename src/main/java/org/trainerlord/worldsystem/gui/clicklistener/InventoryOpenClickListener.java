package org.trainerlord.worldsystem.gui.clicklistener;

import org.trainerlord.worldsystem.gui.inventory.OrcInventory;
import org.trainerlord.worldsystem.gui.inventory.OrcClickListener;
import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.bukkit.entity.Player;
import org.trainerlord.worldsystem.configs.MessageConfig;
import org.bukkit.inventory.Inventory;

public class InventoryOpenClickListener implements OrcClickListener {
    private final OrcInventory open;

    public InventoryOpenClickListener(OrcInventory inv) {
        open = inv;
    }

    @Override
    public void onClick(Player p, OrcInventory inv, OrcItem item) {
        p.closeInventory();
        if (open == null) {
            return;
        }
        Inventory to = open.getInventory(p);
        if (to != null) {
            p.openInventory(to);
        } else {
            p.closeInventory();
            p.sendMessage(MessageConfig.getNoPermission());
        }
    }
}
