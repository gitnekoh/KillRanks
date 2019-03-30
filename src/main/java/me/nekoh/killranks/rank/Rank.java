package me.nekoh.killranks.rank;

import lombok.Getter;
import me.nekoh.killranks.manager.RankManager;

@Getter
public class Rank {

    private String name;
    private String prefix;
    private int killsNeeded;

    public Rank(String name, String prefix, int killsNeeded) {
        this.name = name;
        this.prefix = prefix;
        this.killsNeeded = killsNeeded;
        RankManager.getRanks().put(this.name, this);
        RankManager.getSortedRanks().add(this);
    }
}
