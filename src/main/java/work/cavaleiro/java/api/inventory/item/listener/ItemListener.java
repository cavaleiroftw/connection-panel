package work.cavaleiro.java.api.inventory.item.listener;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import work.cavaleiro.java.api.inventory.item.Item;
import work.cavaleiro.java.api.inventory.Inventory;
import work.cavaleiro.java.api.inventory.item.action.ItemAction;
import work.cavaleiro.java.api.inventory.item.click.ItemClick;

public final class ItemListener implements Listener {

    @EventHandler
    void event(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() == null
                || player.getItemInHand().getType().equals(Material.AIR) || !player.getItemInHand().hasItemMeta()) return;

        Item item = Item.convertItem(player.getItemInHand());
        if (item == null) return;

        ItemClick click = item.getClick();
        if (click != null) {
            if (event.getAction().name().startsWith("RIGHT"))
                click.handleClick(event);
        }
    }

    @SneakyThrows
    @EventHandler(priority = EventPriority.MONITOR)
    void event(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof Inventory)) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
                return;

            Item item = Item.convertItem(event.getCurrentItem());
            if (item == null) return;

            ItemAction action = item.getAction();
            if (action != null)
                action.handleAction(event);
        }
    }
}
