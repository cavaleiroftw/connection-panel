package work.cavaleiro.java.api.inventory.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import work.cavaleiro.java.api.inventory.Inventory;
import work.cavaleiro.java.api.inventory.item.Item;
import work.cavaleiro.java.event.builder.UpdateEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InventoryListener implements Listener {

    Map<UUID, Inventory> map = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    void event(InventoryClickEvent event) throws Exception {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Inventory) {
            event.setCancelled(true);
            event.getWhoClicked().setItemOnCursor(null);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
                return;

            Inventory inventory = (Inventory) event.getInventory().getHolder();
            Item item = inventory.getContents().get(event.getSlot());

            if (item == null) return;

            //if (!Item.contains(event.getCurrentItem())) return;

            if (item.getAction() != null)
                item.getAction().handleAction(event);
        }
    }

    @EventHandler
    void event(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Inventory) {
            Inventory inventory = (Inventory) holder;

            map.put(event.getPlayer().getUniqueId(), inventory);
        }
    }

    @EventHandler
    void event(InventoryCloseEvent event) {
        map.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    void event(UpdateEvent event) {
        map.values().forEach(inventory -> {
            if (inventory == null) return;

            inventory.getContents().values().forEach(content -> {
                if (content == null) return;

                if (content.getUpdater() != null)
                    content.getUpdater().handleUpdater();
            });
        });
    }

    @EventHandler
    void event(PlayerQuitEvent event) {
        map.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    void event(PlayerKickEvent event) {
        map.remove(event.getPlayer().getUniqueId());
    }
}