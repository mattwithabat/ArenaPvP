package com.mattwithabat.arenapvp.commands;

import com.mattwithabat.arenapvp.kits.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {
    private final KitManager kitManager;

    public KitCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            listKits(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /kit create <name>");
                    return true;
                }
                kitManager.createKit(player, args[1]);
                break;
            case "delete":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /kit delete <name>");
                    return true;
                }
                kitManager.deleteKit(args[1]);
                player.sendMessage("§aKit deleted");
                break;
            case "list":
                listKits(player);
                break;
            default:
                player.sendMessage("§cUnknown subcommand");
        }

        return true;
    }

    private void listKits(Player player) {
        player.sendMessage("§6Available kits:");
        kitManager.getKits().forEach(name -> player.sendMessage("§e- " + name));
    }
}
