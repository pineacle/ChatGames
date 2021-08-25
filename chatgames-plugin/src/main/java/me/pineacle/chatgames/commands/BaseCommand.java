package me.pineacle.chatgames.commands;

import lombok.RequiredArgsConstructor;
import me.pineacle.chatgames.ChatGamesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public class BaseCommand implements CommandExecutor, TabCompleter {

    private final ChatGamesPlugin plugin;

    /*
        /chatgames toggle - toggles games for player (chatgames.toggle)
        /chatgames disable - stops the chatgames (chatgames.disable) (chatgames.admin)
        /chatgames enable - starts the chatgames again (chatgames.enable) (chatgames.admin)
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // is console
        if (!(sender instanceof Player)) {

            if (args.length == 0) {
                plugin.getLogger().warning("Missing argument: /chatgames [enable/disable]");
                return false;
            }

            if (args[0].equalsIgnoreCase("enable")) {
                if (plugin.getGameManager().getGameSchedulerTask() != null)
                    plugin.getLogger().warning("Game is already enabled!");
                else
                    plugin.getGameManager().startGames();
                return true;
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (plugin.getGameManager().getGameSchedulerTask() == null)
                    plugin.getLogger().warning("Game is already disabled!");
                else
                    plugin.getGameManager().stopGames();
                return true;
            }

        } else {
            // is player
            Player player = (Player) sender;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
