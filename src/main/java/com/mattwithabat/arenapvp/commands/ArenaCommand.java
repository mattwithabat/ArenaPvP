package com.mattwithabat.arenapvp.commands;

import com.mattwithabat.arenapvp.ArenaPvP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArenaCommand implements CommandExecutor {
    private final ArenaPvP plugin;

    public ArenaCommand(ArenaPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                plugin.getArenaManager().joinArena(player);
                break;
            case "leave":
                plugin.getArenaManager().leaveArena(player);
                break;
            case "stats":
                showStats(player, args.length > 1 ? Bukkit.getPlayer(args[1]) : player);
                break;
            default:
                sendHelp(player);
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6Arena Commands:");
        player.sendMessage("§e/arena join §7- Join the queue");
        player.sendMessage("§e/arena leave §7- Leave the arena");
        player.sendMessage("§e/arena stats [player] §7- View stats");
    }

    private void showStats(Player sender, Player target) {
        if (target == null) {
            sender.sendMessage("§cPlayer not found");
            return;
        }

        UUID uuid = target.getUniqueId();
        int elo = plugin.getEloManager().getRating(uuid);
        int wins = plugin.getPlayerData().getWins(uuid);
        int losses = plugin.getPlayerData().getLosses(uuid);
        double winRate = wins + losses > 0 ? (wins * 100.0 / (wins + losses)) : 0;

        sender.sendMessage("§6" + target.getName() + "'s Stats:");
        sender.sendMessage("§eElo Rating: §f" + elo);
        sender.sendMessage("§eWins: §f" + wins + " §7| §eLosses: §f" + losses);
        sender.sendMessage("§eWin Rate: §f" + String.format("%.1f%%", winRate));
    }
}
