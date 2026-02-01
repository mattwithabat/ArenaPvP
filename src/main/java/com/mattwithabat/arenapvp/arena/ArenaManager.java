package com.mattwithabat.arenapvp.arena;

import com.mattwithabat.arenapvp.ArenaPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class ArenaManager {
    private final ArenaPvP plugin;
    private final Map<String, Arena> arenas;
    private final Map<UUID, Arena> playerArenas;

    public ArenaManager(ArenaPvP plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.playerArenas = new HashMap<>();
        loadArenas();
    }

    public Arena findAvailableArena() {
        return arenas.values().stream()
                .filter(a -> !a.isActive() && !a.isEmpty())
                .findFirst()
                .orElse(null);
    }

    public boolean joinArena(Player player) {
        Arena arena = findAvailableArena();
        if (arena == null) {
            player.sendMessage("§cNo arenas available right now");
            return false;
        }

        if (!arena.addPlayer(player)) {
            player.sendMessage("§cCould not join arena");
            return false;
        }

        playerArenas.put(player.getUniqueId(), arena);

        if (arena.isActive()) {
            startMatch(arena);
        } else {
            player.sendMessage("§aWaiting for opponent...");
        }

        return true;
    }

    private void startMatch(Arena arena) {
        Player p1 = Bukkit.getPlayer(arena.getPlayer1());
        Player p2 = Bukkit.getPlayer(arena.getPlayer2());

        if (p1 != null) p1.sendMessage("§aMatch starting!");
        if (p2 != null) p2.sendMessage("§aMatch starting!");
    }

    public void leaveArena(Player player) {
        Arena arena = playerArenas.remove(player.getUniqueId());
        if (arena != null) {
            UUID opponent = arena.getOpponent(player.getUniqueId());
            if (opponent != null) {
                Player oppPlayer = Bukkit.getPlayer(opponent);
                if (oppPlayer != null) {
                    oppPlayer.sendMessage("§aYour opponent left. You win!");
                    handleWin(oppPlayer, player);
                }
            }
            arena.removePlayer(player.getUniqueId());
        }
    }

    public void onDeath(Player player) {
        Arena arena = playerArenas.remove(player.getUniqueId());
        if (arena != null) {
            Player opponent = Bukkit.getPlayer(arena.getOpponent(player.getUniqueId()));
            if (opponent != null) {
                handleWin(opponent, player);
            }
            arena.removePlayer(player.getUniqueId());
        }
    }

    private void handleWin(Player winner, Player loser) {
        plugin.getPlayerData().addWin(winner.getUniqueId());
        plugin.getPlayerData().addLoss(loser.getUniqueId());
        plugin.getEloManager().recordMatch(winner.getUniqueId(), loser.getUniqueId());

        winner.sendMessage("§eYou won! New Elo: §a" + plugin.getEloManager().getRating(winner.getUniqueId()));
        loser.sendMessage("§eYou lost. New Elo: §c" + plugin.getEloManager().getRating(loser.getUniqueId()));

        winner.teleport(getLobbySpawn());
        loser.teleport(getLobbySpawn());
    }

    private Location getLobbySpawn() {
        return new Location(
                Bukkit.getWorld(plugin.getConfig().getString("arena.spawn-world", "world")),
                plugin.getConfig().getDouble("arena.spawn-x", 0),
                plugin.getConfig().getDouble("arena.spawn-y", 64),
                plugin.getConfig().getDouble("arena.spawn-z", 0)
        );
    }

    private void loadArenas() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("arenas");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            String world = section.getString(key + ".world");
            double x1 = section.getDouble(key + ".spawn1.x");
            double y1 = section.getDouble(key + ".spawn1.y");
            double z1 = section.getDouble(key + ".spawn1.z");
            double x2 = section.getDouble(key + ".spawn2.x");
            double y2 = section.getDouble(key + ".spawn2.y");
            double z2 = section.getDouble(key + ".spawn2.z");

            Location spawn1 = new Location(Bukkit.getWorld(world), x1, y1, z1);
            Location spawn2 = new Location(Bukkit.getWorld(world), x2, y2, z2);

            arenas.put(key, new Arena(key, spawn1, spawn2));
        }
    }

    public void shutdown() {
        playerArenas.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(getLobbySpawn());
            }
        });
    }
}
