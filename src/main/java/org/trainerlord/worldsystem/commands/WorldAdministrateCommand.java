package org.trainerlord.worldsystem.commands;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.trainerlord.worldsystem.WorldSystem;
import org.trainerlord.worldsystem.configs.DataConfig;
import org.trainerlord.worldsystem.configs.MessageConfig;
import org.trainerlord.worldsystem.configs.PluginConfig;
import org.trainerlord.worldsystem.configs.WorldConfig;
import org.trainerlord.worldsystem.utils.PlayerPositions;
import org.trainerlord.worldsystem.wrapper.PlayerWrapper;
import org.trainerlord.worldsystem.wrapper.WorldPlayer;

//TODO Fix Errors


import java.io.File;
import java.io.IOException;

public class WorldAdministrateCommand {
    public boolean setTime(CommandSender sender, String ticks) {
        switch (ticks) {
            case "day":
                return setTime(sender, 0);
            case "night":
                return setTime(sender, 14000);
            case "dawn":
                return setTime(sender, 23000);
            default:
                try {
                    return setTime(sender, Long.parseLong(ticks));
                } catch (NumberFormatException e) {
                    sender.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws time [day/night/dawn/0-24000]"));
                    return false;
                }

        }
    }

    public boolean setStorm(CommandSender sender, boolean storm) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command has to be executed as a player!");
            return false;
        }

        Player p = (Player) sender;
        DataConfig dc = new DataConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNotOnWorld());
            return false;
        }

        if (dc.getWorldname(0).equals(p.getWorld().getName())) {//TODO MultiWorld
            p.getWorld().setStorm(storm);
            return true;
        }


        p.sendMessage(MessageConfig.getNotOnWorld());
        return false;
    }

    public boolean setTime(CommandSender sender, long ticks) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command has to be executed as a player!");
            return false;
        }

        Player p = (Player) sender;
        DataConfig dc = new DataConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNotOnWorld());
            return false;
        }

        if (dc.getWorldname(0).equals(p.getWorld().getName())) { //TODO MultiWorld
            p.getWorld().setTime(ticks);
            return true;
        }


        p.sendMessage(MessageConfig.getNotOnWorld());
        return false;
    }

    public boolean delMemberCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws delmember <Player>"));
                return false;
            }

            DataConfig dc = new DataConfig(p);
            if (!dc.hasWorld()) {
                p.sendMessage(MessageConfig.getNoWorldOwn());
                return false;
            }

            @SuppressWarnings("deprecation")
            OfflinePlayer a = PlayerWrapper.getOfflinePlayer(args[1]);
            WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname(0)); //TODO MultiWorld
            if (a == null) {
                p.sendMessage(MessageConfig.getNotRegistered().replaceAll("%player", args[1]));
                return false;
            } else if (!wc.isMember(a.getUniqueId())) {
                p.sendMessage(MessageConfig.getNoMemberOwn());
                return false;
            }
            //TODO MultiWorld
            WorldRemovememberEvent event = new WorldRemovememberEvent(a.getUniqueId(), dc.getWorldname(0), p);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return false;

            if (a.isOnline()) {
                Player t = (Player) a;
                if (t.getWorld().getName().equals(new DataConfig(p).getWorldname(0))) {//TODO MultiWorld
                    t.teleport(PluginConfig.getSpawn(t));
                    t.setGameMode(PluginConfig.getSpawnGamemode());
                }
            }

            wc.removeMember(a.getUniqueId());
            try {
                wc.save();
            } catch (IOException e) {
                p.sendMessage(MessageConfig.getUnknownError());
                e.printStackTrace();
            }
            p.sendMessage(MessageConfig.getMemberRemoved().replaceAll("%player", a.getName()));
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    public boolean deleteCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender cs = sender;

        if (args.length < 2) {
            cs.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws delete <Player>"));
            return false;
        }

        DataConfig dc = new DataConfig(args[1]);
        if (!dc.hasWorld()) {
            cs.sendMessage(MessageConfig.getNoWorldOther());
            return false;
        }

        String worldname = dc.getWorldname(0);//TODO MultiWorld
        SystemWorld sw = SystemWorld.getSystemWorld(worldname);
        WorldDeleteEvent event = new WorldDeleteEvent(cs, sw);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;

        if (sw != null && sw.isLoaded())
            sw.directUnload(Bukkit.getWorld(worldname));

        WorldConfig config = WorldConfig.getWorldConfig(worldname);
        // Delete unnecessary positions
        PlayerPositions.instance.deletePositions(config);

        Bukkit.getScheduler().runTaskLater(WorldSystem.getInstance(), () -> {
            OfflinePlayer op = dc.getOwner();

            String uuid = op.getUniqueId().toString();
            File dir = new File(PluginConfig.getWorlddir() + "/" + worldname);
            if (!dir.exists())
                dir = new File(Bukkit.getWorldContainer(), worldname);
            if (dir.exists())
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (Exception e) {
                    cs.sendMessage(MessageConfig.getUnknownError());
                    e.printStackTrace();
                }
            File dconfig = new File("plugins//WorldSystem//dependence.yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dconfig); //json
            cfg.set("Dependences." + uuid + ".ID", null);
            cfg.set("Dependences." + uuid + ".ActualName", null);
            cfg.set("Dependences." + uuid, null);
            try {
                cfg.save(dconfig);
            } catch (Exception e) {
                cs.sendMessage(MessageConfig.getUnknownError());
                e.printStackTrace();
            }
            cs.sendMessage(MessageConfig.getDeleteWorldOther().replaceAll("%player", op.getName()));
            if (op.isOnline()) {
                Player p1 = Bukkit.getPlayer(op.getUniqueId());
                p1.sendMessage(MessageConfig.getDeleteWorldOwn());
            }
        }, 10);
        return true;
    }

    public boolean addMemberCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
        Player p = (Player) sender;
        if (args.length < 2) {
            p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws addmember <Player>"));
            return false;
        }

        DataConfig dc = new DataConfig(p);
        if (!dc.hasWorld()) {
            p.sendMessage(MessageConfig.getNoWorldOwn());
            return false;
        }
        @SuppressWarnings("deprecation")
        OfflinePlayer a = PlayerWrapper.getOfflinePlayer(args[1]);
        WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname());
        if (a == null) {
            p.sendMessage(MessageConfig.getNotRegistered().replaceAll("%player", args[1]));
            return false;
        } else if (wc.isMember(a.getUniqueId())) {
            p.sendMessage(MessageConfig.getAlreadyMember());
            return false;
        }

        WorldAddmemberEvent event = new WorldAddmemberEvent(a.getUniqueId(), dc.getWorldname(), p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;

        wc.addMember(a.getUniqueId());
        try {
            wc.save();
        } catch (IOException e) {
            p.sendMessage(MessageConfig.getUnknownError());
            e.printStackTrace();
        }
        p.sendMessage(MessageConfig.getMemberAdded().replaceAll("%player", a.getName()));
        return true;

    }

    public boolean toggleTeleportCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws toggletp <Player>"));
                return false;
            }

            DataConfig dc = new DataConfig(p);
            if (!dc.hasWorld()) {
                p.sendMessage(MessageConfig.getNoWorldOwn());
                return false;
            }
            OfflinePlayer a = PlayerWrapper.getOfflinePlayer(args[1]);
            WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname(0)); //TODO MultiWorld
            if (!wc.isMember(a.getUniqueId())) {
                p.sendMessage(MessageConfig.getNoMemberOwn());
                return false;
            }
            WorldPlayer wp = new WorldPlayer(a, dc.getWorldname(0));//TODO MultiWorld
            if (wp.isOwnerofWorld()) {
                p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
                return false;
            }
            if (wp.toggleTeleport()) {
                p.sendMessage(MessageConfig.getToggleTeleportEnabled().replaceAll("%player", a.getName()));
            } else {
                p.sendMessage(MessageConfig.getToggleTeleportDisabled().replaceAll("%player", a.getName()));
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    public boolean toggleGamemodeCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws togglegm <Player>"));
                return false;
            }

            DataConfig dc = new DataConfig(p);
            if (!dc.hasWorld()) {
                p.sendMessage(MessageConfig.getNoWorldOwn());
                return false;
            }
            @SuppressWarnings("deprecation")
            OfflinePlayer a = PlayerWrapper.getOfflinePlayer(args[1]);
            WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname(0)); //TODO MultiWorld
            if (!wc.isMember(a.getUniqueId())) {
                p.sendMessage(MessageConfig.getNoMemberOwn());
                return false;
            }
            WorldPlayer wp = new WorldPlayer(a, dc.getWorldname());
            if (wp.isOwnerofWorld()) {
                p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
                return false;
            }
            if (wp.toggleGamemode()) {
                p.sendMessage(MessageConfig.getToggleGameModeEnabled().replaceAll("%player", a.getName()));
            } else {
                p.sendMessage(MessageConfig.getToggleGameModeDisabled().replaceAll("%player", a.getName()));
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    public boolean toggleWorldeditCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws togglewe <Player>"));
                return false;
            }

            DataConfig dc = new DataConfig(p);
            if (!dc.hasWorld()) {
                p.sendMessage(MessageConfig.getNoWorldOwn());
                return false;
            }
            @SuppressWarnings("deprecation")
            OfflinePlayer a = PlayerWrapper.getOfflinePlayer(args[1]);
            WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname(0)); //TODO MultiWorld
            if (!wc.isMember(a.getUniqueId())) {
                p.sendMessage(MessageConfig.getNoMemberOwn());
                return false;
            }
            WorldPlayer wp = new WorldPlayer(a, dc.getWorldname(0)); //TODO Multiword
            if (wp.isOwnerofWorld()) {
                p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
                return false;
            }
            if (wp.toggleWorldEdit()) {
                p.sendMessage(MessageConfig.getToggleWorldeditEnabled().replaceAll("%player", a.getName()));
            } else {
                p.sendMessage(MessageConfig.getToggleWorldeditDisabled().replaceAll("%player", a.getName()));
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }

    public boolean toggleBuildCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                p.sendMessage(MessageConfig.getWrongUsage().replaceAll("%usage", "/ws togglebuild <Player>"));
                return false;
            }

            DataConfig dc = new DataConfig(p);
            if (!dc.hasWorld()) {
                p.sendMessage(MessageConfig.getNoWorldOwn());
                return false;
            }
            @SuppressWarnings("deprecation")
            OfflinePlayer a = PlayerWrapper.getOfflinePlayer(args[1]);
            WorldConfig wc = WorldConfig.getWorldConfig(dc.getWorldname(0)); //TODO MultiWorld
            if (!wc.isMember(a.getUniqueId())) {
                p.sendMessage(MessageConfig.getNoMemberOwn());
                return false;
            }
            WorldPlayer wp = new WorldPlayer(a, dc.getWorldname(0)); //TODO MultiWorld
            if (wp.isOwnerofWorld()) {
                p.sendMessage(PluginConfig.getPrefix() + "§cYou are the owner");
                return false;
            }
            if (wp.toggleBuild()) {
                p.sendMessage(MessageConfig.getToggleBuildEnabled().replaceAll("%player", a.getName()));
            } else {
                p.sendMessage(MessageConfig.getToggleBuildDisabled().replaceAll("%player", a.getName()));
            }
            return true;
        } else {
            sender.sendMessage("No Console"); //TODO Get Config
            return false;
        }
    }
}
