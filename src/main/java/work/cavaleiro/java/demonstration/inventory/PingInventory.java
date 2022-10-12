package work.cavaleiro.java.demonstration.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import work.cavaleiro.java.api.PingAPI;
import work.cavaleiro.java.api.inventory.Inventory;
import work.cavaleiro.java.api.inventory.item.Item;
import work.cavaleiro.java.api.statistic.PingStatistic;

import java.util.ArrayList;
import java.util.List;

public class PingInventory extends Inventory {
    public PingInventory(Inventory last) {
        super("Panel", Math.min((Bukkit.getOnlinePlayers().size() / 9) + 5, 6), 28, last);
    }

    @Override
    public void handle(Player bukkitPlayer) {
        clear();
        int index = 10, last = index;

        for (Player player : Bukkit.getOnlinePlayers()) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

            if (isReturnable())
                addBackToPage();

            if (players.isEmpty()) {
                setItem(22, new Item(Material.BARRIER).name("§cPlayers not found!"));

                open(bukkitPlayer);
                return;
            }

            setTotalPages((players.size() / getMaxItems()) + 1);
            addBorder(bukkitPlayer);

            for (int i = 0; i < getMaxItems(); i++) {
                int finalIndex = index;

                setPageIndex(getMaxItems() * (getPageNumber() - 1) + i);
                if (getPageIndex() >= players.size()) break;

                Item template = new Item(Material.SKULL_ITEM).skullName(player.getName()).name(player.getName());

                template.updater(() -> {
                    InventoryView inventory = bukkitPlayer.getOpenInventory();
                    if (!inventory.getTitle().equalsIgnoreCase(getTitle())) return;

                    Item item = getContents().get(finalIndex);
                    if (item == null) return;

                    item.lore("§8Pinging...",
                            "",
                            "§aPing: §e" + PingAPI.ping(player),
                            "§aType: " + PingStatistic.calculate(player));
                    inventory.setItem(finalIndex, item);

                    bukkitPlayer.updateInventory();
                });

                index += 1;
                if (index == (last + 7)) {
                    index += 2;
                    last = index;
                }
            }
            open(bukkitPlayer);
        }
    }
}
