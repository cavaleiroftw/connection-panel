package work.cavaleiro.java.api.inventory.factory;

import org.bukkit.entity.Player;
import work.cavaleiro.java.api.inventory.Inventory;

public class InventoryFactory {

    public static Inventory createInventory(String title) {
        return new Inventory(title, 3) {
            @Override
            public void handle(Player player) {

            }
        };
    }

    public static Inventory createInventory(String title, int rows) {
        return new Inventory(title, rows) {
            @Override
            public void handle(Player player) {

            }
        };
    }

    public static Inventory createInventory(String title, int rows, Inventory last) {
        return new Inventory(title, rows, last) {
            @Override
            public void handle(Player player) {

            }
        };
    }
}
