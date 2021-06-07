package org.trainerlord.worldsystem.gui.inventory.pages;


import org.trainerlord.worldsystem.gui.inventory.OrcItem;
import org.trainerlord.worldsystem.gui.inventory.OrcInventory;
import org.trainerlord.worldsystem.configs.GuiConfig;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryPage extends OrcInventory {
    InventoryPage next, before = null;
    private int i = 0;

    public InventoryPage(String title, int page, int pages) {
        super(title, 6);

        YamlConfiguration cfg = GuiConfig.getConfig();
        String path = "options.players.currentpage";

        OrcItem oi = new OrcItem(GuiConfig.getMaterial(cfg, path), GuiConfig.getData(cfg, path),
                GuiConfig.getDisplay(cfg, path).replaceAll("%page", "" + page), GuiConfig.getLore(cfg, path));
        addItem(GuiConfig.getSlot(path), oi);

        path = "options.players.pagebefore";
        oi = GuiConfig.getItem(path);
        oi.setOnClick((p, inv, item) -> {
            p.closeInventory();
            p.openInventory(this.before.getInventory(p));
        });
        addItem(GuiConfig.getSlot(path), oi);

        path = "options.players.nextpage";
        oi = GuiConfig.getItem(path);
        oi.setOnClick((p, inv, item) -> {
            p.closeInventory();
            p.openInventory(this.next.getInventory(p));
        });
        addItem(GuiConfig.getSlot(path), oi);
    }

    @Override
    public Inventory getInventory(Player p) {
        return super.getInventory(p);
    }

    public void addItem(OrcItem item) {
        if (i > 36) {
            System.err.println("More items than allowed in page view");
            return;
        }
        addItem(i, item);
        i++;
    }
}
