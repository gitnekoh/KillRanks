package me.nekoh.killranks.listener;

import me.nekoh.killranks.KillRanks;
import me.nekoh.killranks.manager.PlayerManager;
import me.nekoh.killranks.manager.RankManager;
import me.nekoh.killranks.player.RankPlayer;
import me.nekoh.killranks.rank.Rank;
import me.nekoh.killranks.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillListener implements Listener {

    public KillListener(KillRanks killRanks) {
        Bukkit.getPluginManager().registerEvents(this, killRanks);
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player && event.getEntity().getKiller() != null)) {
            return;
        }

        Player player = event.getEntity().getKiller();
        RankPlayer rankPlayer = PlayerManager.get(player, true);
        rankPlayer.setKills(rankPlayer.getKills() + 1);

        if (calcNewRank(rankPlayer) != rankPlayer.getRank()) {
            Rank newRank = calcNewRank(rankPlayer);
            player.sendMessage(CC.translate("\n&eYou have been promoted to next rank!\n   &a" + rankPlayer.getRank().getName() + " -> " + newRank.getName() + "\n"));
            rankPlayer.setRank(newRank);
        }
    }

    private Rank calcNewRank(RankPlayer rankPlayer) {
        int currentKills = rankPlayer.getKills();
        Rank newRank = rankPlayer.getRank();

        for (Rank rank : RankManager.getSortedRanks()) {
            if (rank.getKillsNeeded() > newRank.getKillsNeeded() && currentKills >= rank.getKillsNeeded()) {
                newRank = rank;
            }
        }
        return newRank;
    }
}
