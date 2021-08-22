package me.pineacle.chatgames.storage.database.sqlite;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.storage.database.DatabaseBackend;
import me.pineacle.chatgames.user.User;
import me.pineacle.chatgames.utils.exeptions.UnavailableUserException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class SQLite extends DatabaseBackend {

    protected SQLite(ChatGamesPlugin plugin) {
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
