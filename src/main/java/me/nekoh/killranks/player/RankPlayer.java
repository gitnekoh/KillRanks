package me.nekoh.killranks.player;

import lombok.Getter;
import lombok.Setter;
import me.nekoh.killranks.manager.PlayerManager;
import me.nekoh.killranks.rank.Rank;

import java.util.UUID;

@Getter
@Setter
public class RankPlayer {

    private final UUID uuid;
    private Rank rank;
    private int kills;
    private boolean loaded;

    public RankPlayer(UUID uuid, boolean cache) {
        this.uuid = uuid;
        if (cache) PlayerManager.getPlayers().put(uuid, this);
    }
}
