package com.mattwithabat.arenapvp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mattwithabat.arenapvp.ArenaPvP;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final ArenaPvP plugin;
    private final File dataFile;
    private final Gson gson;
    private Map<UUID, Integer> wins;
    private Map<UUID, Integer> losses;

    public PlayerData(ArenaPvP plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        load();
    }

    public int getWins(UUID uuid) {
        return wins.getOrDefault(uuid, 0);
    }

    public int getLosses(UUID uuid) {
        return losses.getOrDefault(uuid, 0);
    }

    public void addWin(UUID uuid) {
        wins.put(uuid, getWins(uuid) + 1);
    }

    public void addLoss(UUID uuid) {
        losses.put(uuid, getLosses(uuid) + 1);
    }

    public void save() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            Map<String, Object> data = new HashMap<>();
            data.put("wins", wins);
            data.put("losses", losses);
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        if (!dataFile.exists()) {
            wins = new HashMap<>();
            losses = new HashMap<>();
            return;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> data = gson.fromJson(reader, type);

            wins = deserializeIntMap((Map<String, Object>) data.get("wins"));
            losses = deserializeIntMap((Map<String, Object>) data.get("losses"));
        } catch (IOException e) {
            e.printStackTrace();
            wins = new HashMap<>();
            losses = new HashMap<>();
        }
    }

    private Map<UUID, Integer> deserializeIntMap(Map<String, Object> map) {
        if (map == null) return new HashMap<>();
        Map<UUID, Integer> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                UUID uuid = UUID.fromString(entry.getKey());
                result.put(uuid, ((Number) entry.getValue()).intValue());
            } catch (Exception ignored) {}
        }
        return result;
    }
}
