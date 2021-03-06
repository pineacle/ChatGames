package me.pineacle.chatgames.commands.subs;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.commands.SubCommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class HideCommand extends SubCommand {

    private final ChatGamesPlugin plugin;

    public HideCommand(ChatGamesPlugin plugin) {
        super("hide");
        this.plugin = plugin;
        setPermission("chatgames.hide");
    }

    @Override
    public boolean getConsoleCompatible() {
        return false;
    }

    @Override
    public String getPossibleArguments() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        Player player = (Player) sender;
        if (plugin.getUserManager().isToggled(player.getUniqueId())) {
            // hide
            plugin.getUserManager().setToggled(player.getUniqueId(), false);
            player.sendMessage(plugin.getLanguage().get("game-hidden"));
        } else {
            // unhide
            plugin.getUserManager().setToggled(player.getUniqueId(), true);
            player.sendMessage(plugin.getLanguage().get("game-unhidden"));
        }
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Hides the chat games from popping up in chat");
    }

}
