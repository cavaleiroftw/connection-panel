package work.cavaleiro.java.api.inventory.item;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class ItemController {

    static Set<ItemController> controllers = new HashSet<>();

    Player player;
    Map<Integer, Item> contents = new HashMap<>();

    public ItemController(Player player) {
        this.player = player;
        controllers.add(this);
    }

    public static ItemController get(Player player) {
        return controllers.stream().filter(controller -> controller.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
    }

    public static void remove(Player player) {
        controllers.remove(get(player));
    }

    public void setItem(int slot, Item item) {
        player.getInventory().setItem(slot, item);
        contents.put(slot, item);
        update();
    }

    public void removeItem(Item item) {
        contents.forEach((slot, content) -> {
            if (item.isSimilar(content))
                contents.remove(slot);
        });
        update();
    }

    public void update() {
        controllers.remove(get(player));
        controllers.add(this);
    }
}
