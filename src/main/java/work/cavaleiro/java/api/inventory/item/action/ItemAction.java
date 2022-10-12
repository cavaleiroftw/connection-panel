package work.cavaleiro.java.api.inventory.item.action;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ItemAction {
    void handleAction(InventoryClickEvent event) throws Exception;
}
