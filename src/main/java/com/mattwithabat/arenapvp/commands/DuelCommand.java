package com.mattwithabat.arenapvp.commands;

import com.mattwithabat.arenapvp.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {
    private final ArenaManager arenaManager;

    public DuelCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§cUsage: /duel <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cPlayer not found");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§cYou can't duel yourself");
            return true;
        }

        player.sendMessage("§aDuel request sent to " + target.getName());
        target.sendMessage("§e" + player.getName() + " wants to duel you! Type §a/accept " + player.getName() + " §eto accept.");

        return true;
    }
}
