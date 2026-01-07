package io.github.luka3101.coolTeleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoolTeleportCommand implements CommandExecutor {

    private final Map<UUID, Long> cooldown = new HashMap<>();
    private final long cooldownDuration = 5000;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Shows error message if you run this command via console
        if (!(commandSender instanceof Player player)) {
            System.out.println("Only a player can issue this command!");
            return true;
        }

        // Shows error message if the player only runs /coolteleport without providing arguments
        if (strings.length < 1) {
            player.sendMessage(ChatColor.RED + "Missing arguments! Usage: /coolteleport <target player>/<x> <y> <z>");
            return true;
        }

        long currentTime = System.currentTimeMillis();

        // If the player is in cooldown
        if (cooldown.containsKey(player.getUniqueId())) {
            long lastUsedTime = cooldown.get(player.getUniqueId());
            long difference = currentTime - lastUsedTime;

            if (difference < cooldownDuration) {
                long timeLeft = (cooldownDuration - difference)/1000;
                player.sendMessage(ChatColor.RED + "You must wait " + timeLeft + " seconds before trying this command again.");
                return true;
            }
        }

        // Player isn't in cooldown, proceed with the command

        // If player provides a single argument: <target player>
        if (strings.length == 1) {
            Player target = Bukkit.getPlayerExact(strings[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Couldn't find this player!");
                return true;
            }

            player.teleport(target.getLocation());
            player.sendMessage("Teleported you to " + target.getName());
        }

        // If player provides 3 arguments: <x> <y> <z>
        else if (strings.length == 3) {
            double x, y, z;
            try {
                x = Double.parseDouble(strings[0]);
                y = Double.parseDouble(strings[1]);
                z = Double.parseDouble(strings[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid coordinates!");
                return true;
            }

            player.teleport(new Location(player.getWorld(), x, y, z));
            player.sendMessage("Teleported you to " + x + ", " + y + ", " + z);
        } else {
            player.sendMessage(ChatColor.RED + "Invalid arguments. Usage: /coolteleport <target player>/<x> <y> <z>");
            return true;
        }

        cooldown.put(player.getUniqueId(), currentTime);

        return true;
    }
}
