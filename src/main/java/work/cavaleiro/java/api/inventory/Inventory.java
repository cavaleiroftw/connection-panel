package work.cavaleiro.java.api.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;
import work.cavaleiro.java.api.inventory.item.Item;
import work.cavaleiro.java.api.inventory.item.action.ItemAction;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class Inventory implements InventoryHolder {

    public Map<Integer, Item> contents;

    @NonNull
    public String title;
    public int size, rows, pageNumber = 1, pageIndex = 0, totalPages = 1, maxItems;
    public boolean isPaginated, returnable;
    public Inventory lastInventory;
    BukkitTask bukkitTask = null;
    private org.bukkit.inventory.Inventory inventory;

    public Inventory(String title, int rows) {
        this(title, rows, null);
    }

    public Inventory(String title, int rows, Inventory lastInventory) {
        this(title, rows, 0, lastInventory);
    }

    public Inventory(String title, int rows, int maxItems) {
        this(title, rows, maxItems, null);
    }

    public Inventory(@NonNull String title, int rows, int maxItems, Inventory lastInventory) {
        this.title = title;
        this.rows = rows;
        this.size = rows * 9;

        contents = new HashMap<>();

        this.lastInventory = lastInventory;
        returnable = lastInventory != null;

        isPaginated = maxItems > 0;
        this.maxItems = maxItems;
    }

    public void clear() {
        if (inventory != null && !contents.isEmpty()) {
            inventory.clear();
            contents.clear();
        }
    }

    public void setItem(int slot, Item content) {
        contents.put(slot, content);
    }

    public void clear(int... slots) {
        for (int slot : slots) {
            Item item = getContents().get(slot);
            if (item == null) continue;

            item.setType(Material.AIR);
            contents.remove(slot);
        }
    }

    public void addBackToPage() {
        addBackToPage(getRows() * 9 - 5);
    }

    public void addBackToPage(int slot) {
        setItem(slot, new Item(Material.ARROW).name("§cVoltar.").action(event -> {
            Player player = (Player) event.getWhoClicked();

            lastInventory.handle(player);
            playSound(player, SoundType.CHANGE_MENU);
        }));
    }

    public void open(Player bukkitPlayer) {
        inventory = Bukkit.createInventory(this, size, title);
        contents.forEach((slot, content) -> inventory.setItem(slot, content));

        bukkitPlayer.openInventory(inventory);
    }

    public abstract void handle(Player bukkitPlayer);

    public Inventory instance() {
        return this;
    }

    public void playSound(Player player, SoundType type) {
        player.playSound(player.getLocation(), type.getSound(), 5.5F, 5.5F);
    }

    public void updateTitle(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, "minecraft:chest", new ChatMessage(title), player.getOpenInventory().getTopInventory().getSize());
        ep.playerConnection.sendPacket(packet);
        ep.updateInventory(ep.activeContainer);
    }

    public Item requestButton(boolean state, ItemAction action) {
        return new Item(Material.INK_SACK, 1, (state ? 10 : 8))
                .name(state ? "§aConfirmar" : "§cCancelar")
                .lore("§7Clique para " + (state ? "confirmar" : "cancelar") + " a operação.")
                .action(action);
    }

    public Item toggleButton(boolean state, String name) {
        return new Item(Material.INK_SACK, 1, (state ? 10 : 8))
                .name((state ? "§a" : "§c") + name)
                .lore("§fEstado: " + (state ? "§aAtivo" : "§cInativo"),
                        "",
                        "§eClique para alternar!");
    }

    public void addBorder(Player player) {
        if (getPageNumber() > 1)
            setItem(getRows() / 2 * 9, new Item(Material.ARROW).name("§cPágina anterior.").action(event -> {
                setPageNumber(getPageNumber() - 1);
                handle(player);
            }));

        if (getPageNumber() < getTotalPages())
            setItem((getRows() / 2 * 9) + 8, new Item(Material.ARROW).name("§aPróxima página.").action(event -> {
                setPageNumber(getPageNumber() + 1);
                handle(player);
            }));
    }

    @Getter
    @AllArgsConstructor
    public enum SoundType {
        CHANGE_MENU(Sound.WOOD_CLICK),
        CLICK_MENU(Sound.CLICK),
        SUCCESS(Sound.SUCCESSFUL_HIT),
        ERROR(Sound.VILLAGER_HIT);

        Sound sound;
    }
}