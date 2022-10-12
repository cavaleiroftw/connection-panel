package work.cavaleiro.java.api.inventory.item.serializer;

import lombok.val;
import org.bukkit.inventory.ItemStack;
import work.cavaleiro.java.util.object.ObjectString;

public class Serializer {

    public static Object write(final ItemStack itens) {
        val objectString = new ObjectString<>(itens);
        return objectString.getCode();
    }

    public static ItemStack read(final String code) {
        val objectString = new ObjectString<ItemStack>(code);
        return (ItemStack) objectString.getObject();
    }
}
