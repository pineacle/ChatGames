package me.pineacle.chatgames.storage.database;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.user.User;
import me.pineacle.chatgames.utils.exeptions.UnavailableUserException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public abstract class DatabaseBackend extends Queries {

    protected final ChatGamesPlugin plugin;

    protected DatabaseBackend(final ChatGamesPlugin plugin) {
        this.plugin = plugin;
    }

    protected void async(final @NotNull Runnable runnable) {
        plugin.async(runnable);
    }

    /**
     * Requests User data from the database
     * <p><b>Save back to the cache/database after editing</b></p>
     *
     * @param uuid {@link UUID} of the {@link org.bukkit.entity.Player}
     * @return User requested
     * @throws UnavailableUserException
     */
    protected abstract User request(@NotNull UUID uuid) throws UnavailableUserException;

    /**
     * Save users data from the cache
     * <p><b>Save back to the cache/database after editing</b></p>
     *
     * @param uuid {@link UUID} of the {@link org.bukkit.entity.Player}
     * @param user User after query
     */
    protected abstract boolean save(@NotNull UUID uuid, Consumer<User> user);

}
