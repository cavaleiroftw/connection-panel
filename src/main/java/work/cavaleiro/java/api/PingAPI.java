package work.cavaleiro.java.api;

import lombok.Getter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@Getter
public class PingAPI {

    public static int ping(Player player) {
        int waiting = 999;
        int pinging = ((CraftPlayer) player).getHandle().ping - 10;

        if (pinging <= 500)
            return waiting = pinging;

        return waiting;
    }
}
