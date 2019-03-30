package me.nekoh.killranks.manager;

import lombok.Getter;
import me.nekoh.killranks.player.RankPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    @Getter
    private static HashMap<UUID, RankPlayer> players = new HashMap<>();

    public static RankPlayer get(UUID uuid, boolean cache) {
        return players.computeIfAbsent(uuid, uuid1 -> new RankPlayer(uuid, cache));
    }

    public static RankPlayer get(Player player, boolean cache) {
        return players.computeIfAbsent(player.getUniqueId(), uuid1 -> new RankPlayer(player.getUniqueId(), cache));
    }

    public static RankPlayer remove(UUID uuid) {
        return players.remove(uuid);
    }
}
