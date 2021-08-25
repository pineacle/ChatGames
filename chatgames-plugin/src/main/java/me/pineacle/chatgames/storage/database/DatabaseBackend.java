package me.pineacle.chatgames.storage.database;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.user.User;
import me.pineacle.chatgames.utils.exeptions.UnavailableUserException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class DatabaseBackend {

    private final ChatGamesPlugin plugin;

    protected DatabaseBackend(final ChatGamesPlugin plugin) {
        this.plugin = plugin;
    }

    protected void async(final @NotNull Runnable runnable) {
        plugin.async(runnable);
    }

    protected String CREATE_IF_NOT_EXIST = "";

    /**
     * Requests User data from the database
     * <br>
     * <b>Save back to the cache/database after editing</b>
     *
     * @param uuid {@link UUID} of the player
     * @return User requested
     * @throws UnavailableUserException
     */
    public abstract User request(@NotNull UUID uuid) throws UnavailableUserException;

    /**
     * Save users data from the cache
     *
     * @param uuid {@link UUID} of the player
     */
    public abstract void save(@NotNull UUID uuid);

}
