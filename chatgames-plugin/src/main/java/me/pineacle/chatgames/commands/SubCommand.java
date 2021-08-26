package me.pineacle.chatgames.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    @Getter private String name;
    @Getter @Setter private String permission;
    private String[] aliases;

    public SubCommand(String name) {
        this(name, new String[0]);
    }

    public SubCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public final boolean hasPermission(CommandSender sender) {
        if (permission == null) return true;
        return sender.hasPermission(permission);
    }

    public abstract boolean getConsoleCompatible();

    public abstract String getPossibleArguments();

    public abstract int getMinimumArguments();

    public abstract void execute(CommandSender sender, String label, String[] args) throws CommandException;

    public abstract List<String> getDescription();

    public final boolean isValidTrigger(String name) {
        if (this.name.equalsIgnoreCase(name)) {
            return true;
        }

        if (aliases != null) {
            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }

        return false;
    }

}
