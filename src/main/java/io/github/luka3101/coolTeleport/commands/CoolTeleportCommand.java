package io.github.luka3101.coolTeleport.commands;

import io.github.luka3101.coolTeleport.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoolTeleportCommand implements CommandExecutor {

    private final Main main;

    public CoolTeleportCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Shows error message if you run this command via console
        if (!(commandSender instanceof Player player)) {
            System.out.println("Only a player can issue this command!");
            return true;
        }

        // Shows error message if the player provides arguments incorrectly
        if (strings.length != 1 && strings.length != 3) {
            player.sendMessage(ChatColor.RED + "Usage: /coolteleport <target player>/<x> <y> <z>");
            return true;
        }

        Player target;
        Location location;

        // If player provides a single argument: <target player>
        if (strings.length == 1) {
            target = Bukkit.getPlayerExact(strings[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Couldn't find this player!");
                return true;
            }

            location = null;
        }

        // If player provides 3 arguments: <x> <y> <z>
        else {
            target = null;

            double x, y, z;
            try {
                x = Double.parseDouble(strings[0]);
                y = Double.parseDouble(strings[1]);
                z = Double.parseDouble(strings[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid coordinates!");
                return true;
            }

            location = new Location(player.getWorld(), x, y, z);
        }

        long teleportDelayTicks = delayAmount(player);
        if (teleportDelayTicks > 0) {
            player.sendMessage(ChatColor.YELLOW + "Teleporting in " + teleportDelayTicks / 20 + " seconds...");
        }

        // Teleport the player
        Bukkit.getScheduler().runTaskLater(main, () -> {
            if (!player.isOnline()) return;

            if (target == null) {
                player.teleport(location);
            } else {
                if (!target.isOnline()) {
                    player.sendMessage(ChatColor.RED + "Couldn't find the player!");
                    return;
                }
                player.teleport(target.getLocation());
            }

            player.sendMessage(ChatColor.GREEN + "Teleported!");

        }, teleportDelayTicks);

        return true;
    }

    private long delayAmount(Player player) {
        if (player.hasPermission("coolteleport.delay.instant")) {
            return 0;
        }

        if (player.hasPermission("coolteleport.delay.short")) {
            return main.getConfig().getInt("teleport-delay.short", 2) * 20L;
        }

        return main.getConfig().getInt("teleport-delay.long", 5) * 20L;
    }
}
