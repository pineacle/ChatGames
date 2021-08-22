package me.pineacle.chatgames;

import lombok.Getter;
import me.pineacle.chatgames.API.IChatGames;
import me.pineacle.chatgames.events.PluginLoadEvent;
import me.pineacle.chatgames.game.GameManager;
import me.pineacle.chatgames.game.GameRegistry;
import me.pineacle.chatgames.game.games.ExactGame;
import me.pineacle.chatgames.user.UserManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class ChatGamesPlugin extends JavaPlugin implements IChatGames {

    @Getter
    private static ChatGamesPlugin instance;

    @Getter
    private Economy economy = null;
    @Getter
    private boolean usingVault = false;

    @Getter
    private UserManager userManager;
    @Getter
    private GameManager gameManager;
    @Getter
    private GameRegistry gameRegistry;

    @Override
    public void onEnable() {
        instance = this;
        userManager = new UserManager();
        gameManager = new GameManager(this);
        gameRegistry = new GameRegistry();

        registerProvidedGames();

        if (setupEconomy()) usingVault = true;

        Bukkit.getServer().getPluginManager().callEvent(new PluginLoadEvent(this));

    }

    @Override
    public void onDisable() {
    }


    private void registerProvidedGames() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    @Override
    public BukkitTask sync(@NotNull final Runnable task) {
        return getServer().getScheduler().runTask(this, task);
    }

    @Override
    public BukkitTask syncAfter(@NotNull final Runnable task, final long delay) {
        return getServer().getScheduler().runTaskLater(this, task, delay);
    }

    @Override
    public BukkitTask syncRepeating(@NotNull final Runnable task, final long delay, final long period) {
        return getServer().getScheduler().runTaskTimer(this, task, delay, period);
    }

    @Override
    public BukkitTask async(@NotNull Runnable task) {
        return getServer().getScheduler().runTaskAsynchronously(this, task);
    }

    @Override
    public void cancelTask(@NotNull final BukkitTask task) {
        Objects.requireNonNull(task, "task can't be null");
        task.cancel();
    }

    @Override
    public void cancelTask(final int id) {
        getServer().getScheduler().cancelTask(id);
    }

}
