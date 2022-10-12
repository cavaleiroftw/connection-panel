package work.cavaleiro.java.event.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import work.cavaleiro.java.event.EventBuilder;

@Getter
@AllArgsConstructor
public final class UpdateEvent extends EventBuilder {
    Player player;
}
