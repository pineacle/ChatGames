package me.pineacle.chatgames.commands.subs;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.commands.SubCommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

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

    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Hides the chat games from popping up in chat");
    }

}
