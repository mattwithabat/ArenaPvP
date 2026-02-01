package com.mattwithabat.arenapvp.kits;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KitManager {
    private final File kitFile;
    private FileConfiguration config;

    public KitManager(File dataFolder) {
        this.kitFile = new File(dataFolder, "kits.yml");
        load();
    }

    public void createKit(Player player, String name) {
        ItemStack[] contents = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        config.set("kits." + name + ".contents", contents);
        config.set("kits." + name + ".armor", armor);
        save();

        player.sendMessage("§aKit '" + name + "' created");
    }

    public void applyKit(Player player, String name) {
        if (!config.contains("kits." + name)) {
            player.sendMessage("§cKit not found");
            return;
        }

        List<ItemStack> contents = (List<ItemStack>) config.getList("kits." + name + ".contents");
        List<ItemStack> armor = (List<ItemStack>) config.getList("kits." + name + ".armor");

        if (contents != null) {
            player.getInventory().setContents(contents.toArray(new ItemStack[0]));
        }
        if (armor != null) {
            player.getInventory().setArmorContents(armor.toArray(new ItemStack[0]));
        }

        player.sendMessage("§aKit applied");
    }

    public void deleteKit(String name) {
        config.set("kits." + name, null);
        save();
    }

    public List<String> getKits() {
        if (config.getConfigurationSection("kits") == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(config.getConfigurationSection("kits").getKeys(false));
    }

    private void load() {
        if (!kitFile.exists()) {
            try {
                kitFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(kitFile);
    }

    private void save() {
        try {
            config.save(kitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
