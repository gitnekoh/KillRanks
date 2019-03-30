package me.nekoh.killranks.listener;

import me.nekoh.killranks.KillRanks;
import me.nekoh.killranks.manager.PlayerManager;
import me.nekoh.killranks.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PrefixListener implements Listener {

    public PrefixListener(KillRanks killRanks) {
        Bukkit.getPluginManager().registerEvents(this, killRanks);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(CC.translate(PlayerManager.get(event.getPlayer(), true).getRank().getPrefix() + event.getFormat()));
    }
}
