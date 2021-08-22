package me.pineacle.chatgames.API;

import me.pineacle.chatgames.API.game.IGameManager;
import me.pineacle.chatgames.API.user.IUserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface IChatGames extends Plugin {

    /**
     * Returns the UserManager
     *
     * @return UserManager singleton
     * @since 1.0.0
     */
    @NotNull IUserManager getUserManager();

    /**
     * Returns the GameManager
     *
     * @return GameManager singleton
     * @since 1.0.0
     */
    @NotNull IGameManager getGameManager();


    /**
     * Schedules a synchronized task on main thread
     *
     * @param task task to run
     * @return BukkitTask
     * @since 1.0.0
     */
    BukkitTask sync(@NotNull final Runnable task);

    /**
     * Schedules a synchronized task on main thread after specified amount of delay
     *
     * @param task task to run
     * @param delay delay in ticks (20L = 1s)
     * @return BukkitTask
     * @since 1.0.0
     */
    BukkitTask syncAfter(@NotNull final Runnable task, long delay);

    /**
     * Runs the task after delay on server thread repeatedly.
     *
     * @param task task to run
     * @param delay delay in ticks to start (20L = 1s)
     * @param interval interval of repeating (20L = 1s)
     * @return BukkitTask executed.
     * @since 1.0.0
     */
    BukkitTask syncRepeating(@NotNull final Runnable task, long delay, long interval);

    /**
     * Schedules an <b>asynchronous</b> task on separate thread
     *
     * <b>don't access the {@link Bukkit} API asynchronously</b>
     *
     * @param task task to run
     * @return BukkitTask
     * @since 1.0.0
     */
    BukkitTask async(@NotNull final Runnable task);

    /**
     * Cancels BukkitTask
     *
     * @param task BukkitTask to cancel
     * @since 1.0.0
     */
    void cancelTask(@NotNull final BukkitTask task);


    /**
     * Cancels BukkitTask with id {@param id}
     *
     * @param id id of the task
     * @since 1.0.0
     */
    void cancelTask(final int id);
}
