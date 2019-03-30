package me.nekoh.killranks.manager;

import lombok.Getter;
import me.nekoh.killranks.rank.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankManager {

    @Getter
    private static List<Rank> sortedRanks = new ArrayList<>();
    @Getter
    private static HashMap<String, Rank> ranks = new HashMap<>();

}
