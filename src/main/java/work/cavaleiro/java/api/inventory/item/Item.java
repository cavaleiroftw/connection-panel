package work.cavaleiro.java.api.inventory.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import work.cavaleiro.java.api.inventory.item.action.ItemAction;
import work.cavaleiro.java.api.inventory.item.click.ItemClick;
import work.cavaleiro.java.api.inventory.item.update.ItemUpdater;
import work.cavaleiro.java.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Getter
public class Item extends ItemStack {

    static Set<Item> items = new HashSet<>();

    ItemAction action = null;
    ItemClick click = null;
    ItemUpdater updater = null;

    public Item(ItemStack stack) {
        super(stack);
    }

    public Item(Material material) {
        super(material);
    }

    public Item(Material material, int amount) {
        super(material, amount);
    }

    public Item(Material material, int amount, int id) {
        super(material, amount, (short) id);
    }

    public Item(int readInt) {
        super(readInt);
    }

    public static Item convertItem(ItemStack stack) {
        return items.stream().filter(item -> item.isSimilar(stack)).findFirst().orElse(null);
    }

    public static boolean contains(ItemStack stack) {
        return items.contains(convertItem(stack));
    }

    public Item type(Material material) {
        super.setType(material);
        return this;
    }

    public Item name(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        setItemMeta(meta);
        return this;
    }

    public Item enchant(Enchantment enchantment, int level) {
        ItemMeta meta = getItemMeta();
        meta.addEnchant(enchantment, level, true);
        setItemMeta(meta);
        return this;
    }

    public Item skullName(String name) {
        SkullMeta meta = (SkullMeta) getItemMeta();
        meta.setOwner(name);
        setItemMeta(meta);
        return this;
    }

    public Item skullUrl(String url) {
        SkullMeta itemMeta = (SkullMeta) getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        setItemMeta(itemMeta);
        return this;
    }

    public Item skullItem(String value, String signature) {
        SkullMeta itemMeta = (SkullMeta) getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value, signature));

        Field field;
        try {
            field = itemMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setItemMeta(itemMeta);
        return this;
    }

    public Item durability(int durability) {
        super.setDurability((short) durability);
        return this;
    }

    public Item amount(int value) {
        super.setAmount(value);
        return this;
    }

    public Item lore(String... lore) {
        return this.lore(Arrays.asList(lore));
    }

    public Item lore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public Item lore(String lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(StringUtils.formatForLore(lore));
        setItemMeta(meta);
        return this;
    }

    public Item itemFlags(ItemFlag... flags) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flags);
        setItemMeta(meta);
        return this;
    }

    public Item action(ItemAction action) {
        this.action = action;
        items.add(this);
        return this;
    }

    public Item click(ItemClick click) {
        this.click = click;
        items.add(this);
        return this;
    }

    public Item updater(ItemUpdater updater) {
        this.updater = updater;
        items.add(this);
        return this;
    }

    public ItemStack toStack() {
        return new ItemStack(this);
    }
}
