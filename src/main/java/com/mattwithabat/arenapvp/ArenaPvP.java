package com.mattwithabat.arenapvp;

import com.mattwithabat.arenapvp.arena.ArenaManager;
import com.mattwithabat.arenapvp.commands.ArenaCommand;
import com.mattwithabat.arenapvp.commands.DuelCommand;
import com.mattwithabat.arenapvp.commands.KitCommand;
import com.mattwithabat.arenapvp.data.EloManager;
import com.mattwithabat.arenapvp.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArenaPvP extends JavaPlugin {

    private static ArenaPvP instance;
    private ArenaManager arenaManager;
    private EloManager eloManager;
    private PlayerData playerData;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        playerData = new PlayerData(this);
        eloManager = new EloManager(this);
        arenaManager = new ArenaManager(this);

        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("duel").setExecutor(new DuelCommand(this));

        Bukkit.getPluginManager().registerEvents(new com.mattwithabat.arenapvp.listeners.PlayerListener(this), this);

        getLogger().info("ArenaPvP enabled");
    }

    @Override
    public void onDisable() {
        if (arenaManager != null) {
            arenaManager.shutdown();
        }
        if (playerData != null) {
            playerData.save();
        }
        getLogger().info("ArenaPvP disabled");
    }

    public static ArenaPvP getInstance() {
        return instance;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public EloManager getEloManager() {
        return eloManager;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }
}
