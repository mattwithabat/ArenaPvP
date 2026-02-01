package com.mattwithabat.arenapvp.arena;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Arena {
    private final String id;
    private final Location spawn1;
    private final Location spawn2;
    private UUID player1;
    private UUID player2;
    private boolean active;

    public Arena(String id, Location spawn1, Location spawn2) {
        this.id = id;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.active = false;
    }

    public String getId() {
        return id;
    }

    public boolean addPlayer(Player player) {
        if (player1 == null) {
            player1 = player.getUniqueId();
            player.teleport(spawn1);
            return true;
        } else if (player2 == null) {
            player2 = player.getUniqueId();
            player.teleport(spawn2);
            active = true;
            return true;
        }
        return false;
    }

    public UUID getOpponent(UUID uuid) {
        if (uuid.equals(player1)) return player2;
        if (uuid.equals(player2)) return player1;
        return null;
    }

    public boolean hasPlayer(UUID uuid) {
        return uuid.equals(player1) || uuid.equals(player2);
    }

    public void removePlayer(UUID uuid) {
        if (uuid.equals(player1)) player1 = null;
        if (uuid.equals(player2)) player2 = null;
        if (player1 == null && player2 == null) {
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEmpty() {
        return player1 == null && player2 == null;
    }

    public UUID getPlayer1() {
        return player1;
    }

    public UUID getPlayer2() {
        return player2;
    }
}
