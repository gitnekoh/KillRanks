package me.nekoh.killranks;

import co.aikar.commands.BukkitCommandManager;
import lombok.Getter;
import lombok.Setter;
import me.nekoh.killranks.command.RankCommand;
import me.nekoh.killranks.data.Data;
import me.nekoh.killranks.gui.Gui;
import me.nekoh.killranks.listener.DataListener;
import me.nekoh.killranks.listener.KillListener;
import me.nekoh.killranks.listener.PrefixListener;
import me.nekoh.killranks.manager.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class KillRanks extends JavaPlugin {

    @Getter
    @Setter
    private static boolean loaded;
    private ConfigManager configManager;
    private Data data;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.data = new Data(this);
        new DataListener(this);
        new Gui(this);
        new KillListener(this);
        new PrefixListener(this);
        new BukkitCommandManager(this).registerCommand(new RankCommand());
    }
}
