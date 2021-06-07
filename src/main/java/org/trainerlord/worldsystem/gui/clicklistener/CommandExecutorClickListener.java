package org.trainerlord.worldsystem.gui.clicklistener;

import org.trainerlord.worldsystem.gui.inventory.OrcInventory;
import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.trainerlord.worldsystem.gui.inventory.OrcClickListener;
import org.bukkit.entity.Player;

public class CommandExecutorClickListener  implements OrcClickListener {

    private final String message;

    public CommandExecutorClickListener(String message) {
        this.message = message;
    }

    @Override
    public void onClick(Player p, OrcInventory inv, OrcItem item) {
        p.closeInventory();
        p.chat(message);
        // Fix for #9
        inv.redraw(p);
    }

}
