package work.cavaleiro.java;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import work.cavaleiro.java.api.inventory.item.listener.ItemListener;
import work.cavaleiro.java.api.inventory.listener.InventoryListener;
import work.cavaleiro.java.demonstration.PingCommand;

import java.util.logging.Level;

@Getter
public class Client extends JavaPlugin {
    @Getter
    public static Client instance;

    @Override
    public void onLoad() {
        instance = this;

        getLogger().log(Level.INFO, "> Initializing Client...");
    }

    @Override
    public void onEnable() {
        /* Client ->> Registering Command */
        getServer().getPluginCommand("ping").setExecutor(new PingCommand());

        /* Client -> Registering Listeners */
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        /* Client ->> initialized */
        getLogger().log(Level.INFO, "> Client initialized!");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "> Client disabled!");
    }
}
