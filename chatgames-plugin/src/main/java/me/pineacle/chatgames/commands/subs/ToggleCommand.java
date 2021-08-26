package me.pineacle.chatgames.commands.subs;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.commands.SubCommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ToggleCommand extends SubCommand {

    private final ChatGamesPlugin plugin;

    public ToggleCommand(ChatGamesPlugin plugin) {
        super("toggle");
        this.plugin = plugin;
        setPermission("chatgames.toggle");
    }

    @Override
    public boolean getConsoleCompatible() {
        return true;
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
        if (plugin.getGameManager().getGameSchedulerTask() != null) {
            plugin.getGameManager().stopGames();
            sender.sendMessage(plugin.getLanguage().get("game-stopped"));
        } else {
            plugin.getGameManager().startGames();
            sender.sendMessage(plugin.getLanguage().get("game-started"));
        }
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Starts the chat games if not enabled already.");
    }

}
