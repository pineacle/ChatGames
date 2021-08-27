package me.pineacle.chatgames.commands.subs;

import me.pineacle.chatgames.API.game.Game;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.commands.SubCommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ForceCommand extends SubCommand {

    private final ChatGamesPlugin plugin;

    public ForceCommand(ChatGamesPlugin plugin) {
        super("force");
        this.plugin = plugin;
        setPermission("chatgames.force");
    }

    @Override
    public boolean getConsoleCompatible() {
        return true;
    }

    @Override
    public String getPossibleArguments() {
        return "[game-type]";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {

        if (args.length == 0) {
            plugin.getGameManager().force(Optional.empty());
            return;
        }

        Optional<Game> gameOptional = plugin.getGameManager().getGamePool().stream().filter(game -> game.getGameName().equalsIgnoreCase(args[0])).findFirst();
        plugin.getGameManager().force(gameOptional);

    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Force a game of an optional type");
    }
}
