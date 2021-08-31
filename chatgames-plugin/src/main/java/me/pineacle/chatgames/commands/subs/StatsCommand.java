package me.pineacle.chatgames.commands.subs;

import me.pineacle.chatgames.API.user.User;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.commands.SubCommand;
import me.pineacle.chatgames.utils.StringUtils;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StatsCommand extends SubCommand {

    private final ChatGamesPlugin plugin;

    public StatsCommand(ChatGamesPlugin plugin) {
        super("stats");
        this.plugin = plugin;
        setPermission("chatgames.stats");
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
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        plugin.getLanguage().get().getStringList("stats-format")
                .stream()
                .map(StringUtils::format)
                .collect(Collectors.toList())
                .forEach(line -> player.sendMessage(line.replace("{wins}", String.valueOf(user.getWins())).replace("{game_status}", plugin.getGameManager().isToggled() ? "active" : "not active")
                        .replace("{status}", !user.isToggled() ? "hidden" : "visible")));
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Gets current statistics on chat games");
    }
}
