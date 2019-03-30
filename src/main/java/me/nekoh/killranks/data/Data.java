package me.nekoh.killranks.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.nekoh.killranks.KillRanks;
import me.nekoh.killranks.manager.RankManager;
import me.nekoh.killranks.player.RankPlayer;
import me.nekoh.killranks.rank.Rank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

@Getter
public class Data {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> players;
    private MongoCollection<Document> ranks;
    private KillRanks killRanks;
    private boolean successful;

    public Data(KillRanks killRanks) {
        this.killRanks = killRanks;

        switch (this.killRanks.getConfigManager().getDataType()) {
            case MONGODB:
                try {
                    this.mongoClient = new MongoClient(new MongoClientURI(this.killRanks.getConfigManager().getMongoLink()));
                    this.mongoDatabase = this.mongoClient.getDatabase("killranks");
                    this.players = this.mongoDatabase.getCollection("players");
                    this.ranks = this.mongoDatabase.getCollection("ranks");
                    for (Document doc : this.ranks.find()) {
                        new Rank(doc.getString("name"), doc.getString("prefix"), doc.getInteger("killsNeeded"));
                        Bukkit.getLogger().log(Level.INFO, "Loaded rank " + doc.getString("name") + ".name");
                    }
                    this.successful = true;
                } catch (Exception e) {
                    this.successful = false;
                }
                break;
            case FLATFILE:
                for (Map.Entry<String, Object> entry : killRanks.getConfigManager().getConfig().getConfigurationSection("ranks").getValues(false).entrySet()) {
                    FileConfiguration config = killRanks.getConfigManager().getConfig();
                    new Rank(entry.getKey(), config.getString("ranks." + entry.getKey() + ".prefix"), config.getInt("ranks." + entry.getKey() + ".killsNeeded"));
                    Bukkit.getLogger().log(Level.INFO, "Loaded rank " + entry.getKey());
                }
                break;
        }

        RankManager.getSortedRanks().sort((o1, o2) -> -(o1.getKillsNeeded() - o2.getKillsNeeded()));
        KillRanks.setLoaded(true);
    }

    public void loadPlayer(RankPlayer rankPlayer) {
        switch (this.killRanks.getConfigManager().getDataType()) {
            case MONGODB:
                Document doc = this.players.find(Filters.eq("uuid", rankPlayer.getUuid().toString())).first();
                if (doc == null) {
                    doc = new Document("uuid", rankPlayer.getUuid().toString());
                    doc.append("rank", RankManager.getSortedRanks().get(0).getName())
                            .append("kills", 0);
                    this.players.insertOne(doc);
                    rankPlayer.setRank(RankManager.getSortedRanks().get(0));
                    rankPlayer.setKills(0);
                } else {
                    rankPlayer.setRank(RankManager.getRanks().get(doc.getString("rank")));
                    rankPlayer.setKills(doc.getInteger("kills"));
                }
                break;
            case FLATFILE:
                FileConfiguration data = this.killRanks.getConfigManager().getData();
                if (data.getString(rankPlayer.getUuid().toString()) == null) {
                    data.set(rankPlayer.getUuid().toString() + ".rank", RankManager.getSortedRanks().get(0).getName());
                    data.set(rankPlayer.getUuid().toString() + ".kills", 0);
                    try {
                        data.save(this.killRanks.getConfigManager().getDataFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    rankPlayer.setRank(RankManager.getSortedRanks().get(0));
                    rankPlayer.setKills(0);
                } else {
                    rankPlayer.setRank(RankManager.getRanks().get(data.getString(rankPlayer.getUuid().toString() + ".rank")));
                    rankPlayer.setKills(data.getInt(rankPlayer.getUuid().toString() + ".kills"));
                }
                break;
        }
    }

    public void savePlayer(RankPlayer rankPlayer) {
        switch (this.killRanks.getConfigManager().getDataType()) {
            case FLATFILE:
                this.killRanks.getConfigManager().getData().set(rankPlayer.getUuid().toString() + ".rank", rankPlayer.getRank().getName());
                this.killRanks.getConfigManager().getData().set(rankPlayer.getUuid().toString() + ".kills", rankPlayer.getKills());
                try {
                    this.killRanks.getConfigManager().getData().save(this.killRanks.getConfigManager().getDataFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MONGODB:
                Document doc = new Document("uuid", rankPlayer.getUuid().toString());
                doc.append("rank", rankPlayer.getRank().getName())
                        .append("kills", rankPlayer.getKills());
                this.players.replaceOne(Filters.eq("uuid", rankPlayer.getUuid().toString()), doc, new ReplaceOptions().upsert(true));
                break;
        }
    }

}
