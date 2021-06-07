package org.trainerlord.worldsystem.commands;


import de.butzlabben.world.autoupdater.AutoUpdater;
import org.trainerlord.worldsystem.WorldSystem;
import org.trainerlord.worldsystem.configs.MessageConfig;
import org.trainerlord.worldsystem.configs.PluginConfig;
import org.trainerlord.worldsystem.configs.WorldConfig;
import de.butzlabben.world.gui.WorldChooseGUI;
import de.butzlabben.world.gui.WorldSystemGUI;
import de.butzlabben.world.util.MoneyUtil;
import de.butzlabben.world.wrapper.SystemWorld;
import org.trainerlord.worldsystem.gui.WorldSystemGUI;
import org.trainerlord.worldsystem.wrapper.WorldPlayer;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WSCommands {
    public boolean mainCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender cs = sender;

        String prefix = PluginConfig.getPrefix();
        cs.sendMessage(
                prefix + "WorldSystem by Butzlabben v" + WorldSystem.getInstance().getDescription().getVersion());
        cs.sendMessage(prefix + "Contributors: Jubeki, montlikadani, jstoeckm2");
        List<String> cmdHelp = MessageConfig.getCommandHelp();
        cmdHelp.forEach(s -> cs.sendMessage(prefix + s));
        if (cs.hasPermission("ws.delete")) {
            cs.sendMessage(MessageConfig.getDeleteCommandHelp());
        }
        return true;
    }

    public boolean guiCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            WorldPlayer wp = new WorldPlayer(p);
            if (!wp.isOnSystemWorld()) {
                p.sendMessage(MessageConfig.getNotOnWorld());
                return false;
            }
            if (!wp.isOwnerofWorld()) {
                p.sendMessage(MessageConfig.getNoPermission());
                return false;
            }
            p.openInventory(new WorldSystemGUI().getInventory(p));
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    /*public boolean confirmCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender cs = sender;

        if (AutoUpdater.getInstance().confirmed()) {
            cs.sendMessage(PluginConfig.getPrefix() + "§cAlready confirmed or no confirm needed");
            return false;
        }
        AutoUpdater.getInstance().confirm();
        cs.sendMessage(PluginConfig.getPrefix() + "§aAutoupdate confirmed, §crestart §ato apply changes");
        return true;
    }*/
