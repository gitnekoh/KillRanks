package me.nekoh.killranks.gui;

import me.nekoh.killranks.KillRanks;
import me.nekoh.killranks.manager.PlayerManager;
import me.nekoh.killranks.manager.RankManager;
import me.nekoh.killranks.player.RankPlayer;
import me.nekoh.killranks.rank.Rank;
import me.nekoh.killranks.util.CC;
import me.nekoh.killranks.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Gui implements Listener {

    public Gui(KillRanks killRanks) {
        Bukkit.getPluginManager().registerEvents(this, killRanks);
    }

    public static Inventory setupInventory(RankPlayer rankPlayer, Player player) {
        Inventory inventory = Bukkit.createInventory(null, ((int) (RankManager.getRanks().size() / 9) * 9) + 9, CC.translate("&eRanks:"));

        for (Rank rank : RankManager.getSortedRanks()) {
            boolean unlocked = rank.getKillsNeeded() <= rankPlayer.getKills() || rank == rankPlayer.getRank();
            ItemStack rankItem = new ItemBuilder(Material.WOOL).setName(CC.translate("&e" + rank.getName())).setDurability((byte) (unlocked ? 5 : 14)).setLore(
                    CC.translate("&eUnlocked: " + (unlocked ? "&ayes" : "&cno")),
                    CC.translate("&eKills needed: &f" + rank.getKillsNeeded()),
                    CC.translate("&ePrefix: " + rank.getPrefix())).toItemStack();
            inventory.addItem(rankItem);
        }

        ItemStack playerItem = new ItemBuilder(Material.SKULL_ITEM).setName(CC.translate("&ePlayer Info")).setDurability((byte) 3).setLore(
                CC.translate("&eRank: &f" + rankPlayer.getRank().getName()),
                CC.translate("&eKills: &f" + rankPlayer.getKills())
        ).toItemStack();

        ((SkullMeta) playerItem.getItemMeta()).setOwner(player.getName());
        inventory.setItem(inventory.getSize() - 1, playerItem);
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        RankPlayer rankPlayer = PlayerManager.get((Player) event.getWhoClicked(), true);

        if (player.getOpenInventory().getTitle().equals(setupInventory(rankPlayer, player).getTitle())) {
            event.setCancelled(true);
        }
    }
}
