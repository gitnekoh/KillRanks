package me.nekoh.killranks.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import me.nekoh.killranks.gui.Gui;
import me.nekoh.killranks.manager.PlayerManager;
import me.nekoh.killranks.player.RankPlayer;
import me.nekoh.killranks.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RankCommand extends BaseCommand {
    @CommandAlias("rank")
    public static void rank(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("only player"));
            return;
        }

        Player player = (Player) sender;

        RankPlayer rankPlayer = PlayerManager.get(player, true);
        player.openInventory(Gui.setupInventory(rankPlayer, player));
    }

}
