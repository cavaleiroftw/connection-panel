package work.cavaleiro.java.demonstration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.cavaleiro.java.api.PingAPI;
import work.cavaleiro.java.api.statistic.PingStatistic;
import work.cavaleiro.java.demonstration.inventory.PingInventory;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = ((Player) sender).getPlayer();

        if (args.length <= 0)
            player.sendMessage("§aUse §8/ping§a to calculate your ping or §8/ping panel§a to see the panel.");
        else if (args[0].equalsIgnoreCase("panel"))
            new PingInventory(null).handle(player);
        else
            player.sendMessage("§cYour ping is from " + PingAPI.ping(player) + "! §8(" + PingStatistic.calculate(player) + "§8)");

        return false;
    }
}
