package me.pineacle.chatgames.commands;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.commands.subs.ForceCommand;
import me.pineacle.chatgames.commands.subs.HelpCommand;
import me.pineacle.chatgames.commands.subs.ToggleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandHandler implements CommandExecutor {

    private final ChatGamesPlugin plugin;

    private final List<SubCommand> subCommands;
    private final Map<Class<? extends SubCommand>, SubCommand> subCommandsClazz;

    public CommandHandler(ChatGamesPlugin plugin) {
        this.plugin = plugin;
        subCommands = new ArrayList<>();
        subCommandsClazz = new HashMap<>();

        registerSubCommand(new ToggleCommand(plugin));
        registerSubCommand(new HelpCommand(plugin));
        registerSubCommand(new ForceCommand(plugin));

    }

    public void registerSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
        subCommandsClazz.put(subCommand.getClass(), subCommand);
    }

    public List<SubCommand> getSubCommands() {
        return new ArrayList<>(subCommands);
    }

    public SubCommand getSubCommand(Class<? extends SubCommand> subCommandClass) {
        return subCommandsClazz.get(subCommandClass);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            getSubCommand(HelpCommand.class).execute(sender, label, args);
            return true;
        }

        for (SubCommand subCommand : subCommands) {

            if (!subCommand.getConsoleCompatible()) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command is not available for console.");
                    return true;
                }
            }

            if (subCommand.isValidTrigger(args[0])) {

                if (!subCommand.hasPermission(sender)) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }

                if (args.length - 1 >= subCommand.getMinimumArguments()) {
                    try {
                        subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
                    } catch (CommandException e) {
                        sender.sendMessage(ChatColor.RED + e.getMessage());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
                }

                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "Unknown sub-command. Type \"/" + label + " help\" for a list of commands.");
        return true;
    }
}
