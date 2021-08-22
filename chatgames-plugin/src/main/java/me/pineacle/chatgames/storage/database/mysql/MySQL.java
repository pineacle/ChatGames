package me.pineacle.chatgames.storage.database.mysql;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.storage.database.DatabaseBackend;
import me.pineacle.chatgames.user.User;
import me.pineacle.chatgames.utils.exeptions.UnavailableUserException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class MySQL extends DatabaseBackend {

    protected MySQL(ChatGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected User request(@NotNull UUID uuid) throws UnavailableUserException {
        return null;
    }

    @Override
    protected boolean save(@NotNull UUID uuid, Consumer<User> user) {
        return false;
    }
}
