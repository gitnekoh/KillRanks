package me.nekoh.killranks.listener;

import me.nekoh.killranks.KillRanks;
import me.nekoh.killranks.manager.PlayerManager;
import me.nekoh.killranks.player.RankPlayer;
import me.nekoh.killranks.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {

    private final KillRanks killRanks;

    public DataListener(KillRanks killRanks) {
        this.killRanks = killRanks;
        Bukkit.getPluginManager().registerEvents(this, killRanks);
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        if(!KillRanks.isLoaded()) {
            event.setKickMessage(CC.translate("&cServer is still setting up!"));
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        RankPlayer rankPlayer = PlayerManager.get(event.getUniqueId(), true);
        Bukkit.getScheduler().runTaskAsynchronously(this.killRanks, () -> killRanks.getData().loadPlayer(rankPlayer));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        RankPlayer rankPlayer = PlayerManager.get(event.getPlayer(), true);

        Bukkit.getScheduler().runTaskAsynchronously(this.killRanks, () -> {
            killRanks.getData().savePlayer(rankPlayer);
            Bukkit.getScheduler().runTask(this.killRanks, () -> PlayerManager.remove(event.getPlayer().getUniqueId()));
        });
    }
}
