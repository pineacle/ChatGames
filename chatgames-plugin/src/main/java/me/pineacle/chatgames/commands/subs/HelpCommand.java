package me.pineacle.chatgames.commands.subs;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.commands.SubCommand;
import me.pineacle.chatgames.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends SubCommand {

    private final ChatGamesPlugin plugin;

    public HelpCommand(ChatGamesPlugin plugin) {
        super("help");
        this.plugin = plugin;
        setPermission("chatgames.help");
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
        sender.sendMessage("");
        sender.sendMessage(StringUtils.format("&6&lCHAT GAMES"));
        for (SubCommand subCommand : plugin.getCommandHandler().getSubCommands()) {
            String usage = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments() : "");

            if (sender instanceof Player) {

                List<String> help = new ArrayList<>();
                help.add(ChatColor.GOLD + usage);
                for (String tutLine : subCommand.getDescription()) {
                    help.add(ChatColor.YELLOW + tutLine);
                }

                ((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
                        .color(ChatColor.GREEN)
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
                        .create());

            } else {
                sender.sendMessage(ChatColor.YELLOW + usage);
            }
        }
        sender.sendMessage("");

    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Shows available commands");
    }

}
