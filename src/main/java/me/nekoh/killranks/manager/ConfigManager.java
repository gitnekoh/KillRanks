package me.nekoh.killranks.manager;

import lombok.Getter;
import me.nekoh.killranks.KillRanks;
import me.nekoh.killranks.data.DataType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class ConfigManager {

    private final FileConfiguration config;
    private FileConfiguration data;
    private File dataFile;
    private KillRanks killRanks;
    private DataType dataType;
    private String mongoLink;
    private boolean prefix;

    public ConfigManager(KillRanks killRanks) {
        this.killRanks = killRanks;
        this.config = killRanks.getConfig();
        loadConfig();
        if (dataType.equals(DataType.FLATFILE)) createDataFile();
    }

    private void loadConfig() {
        this.dataType = DataType.valueOf(this.config.getString("dataType"));
        this.mongoLink = this.config.getString("mongoLink");
        this.prefix = this.config.getBoolean("prefix");
    }

    //spigot skidd0r
    private void createDataFile() {
        dataFile = new File(this.killRanks.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            this.killRanks.saveResource("data.yml", false);
        }

        data = new YamlConfiguration();

        try {
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
