package work.cavaleiro.java.api.statistic;

import lombok.Getter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@Getter
public class PingStatistic {

    public static String calculate(Player player) {
        double ping = ((CraftPlayer) player).getHandle().ping - 10;
        String waiting = "...";

        if (ping <= 15)
            return waiting = "§2excellent";
        else if (ping <= 59)
            return waiting = "§agood";
        else if (ping <= 89)
            return waiting = "§ereasonable";
        else if (ping <= 150)
            return waiting = "§cbad";
        else if (ping >= 151)
            return waiting = "§4terrible";

        return waiting;
    }
}
