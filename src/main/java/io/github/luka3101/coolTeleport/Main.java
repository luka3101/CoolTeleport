package io.github.luka3101.coolTeleport;

import io.github.luka3101.coolTeleport.commands.CoolTeleportCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("coolteleport").setExecutor(new CoolTeleportCommand(this));
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
