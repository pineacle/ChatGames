package me.pineacle.chatgames;

import lombok.Getter;
import me.pineacle.chatgames.API.IChatGames;
import me.pineacle.chatgames.API.game.IGame;
import me.pineacle.chatgames.commands.CommandHandler;
import me.pineacle.chatgames.game.GameManager;
import me.pineacle.chatgames.game.GameRegistry;
import me.pineacle.chatgames.listeners.ChatEvent;
import me.pineacle.chatgames.listeners.JoinEvent;
import me.pineacle.chatgames.storage.config.Config;
import me.pineacle.chatgames.storage.config.Language;
import me.pineacle.chatgames.storage.database.Database;
import me.pineacle.chatgames.storage.database.sqlite.SQLite;
import me.pineacle.chatgames.user.UserManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public final class ChatGamesPlugin extends JavaPlugin implements IChatGames {

    @Getter private static ChatGamesPlugin instance;

    @Getter private Economy economy = null;
    @Getter private boolean usingVault = false;

    @Getter private UserManager userManager;
    @Getter private GameManager gameManager;
    @Getter private GameRegistry gameRegistry;

    @Getter private CommandHandler commandHandler;

    @Getter private Config gameConfig;
    @Getter private Language language;
    @Getter private FileConfiguration words;

    @Getter private Database database;

    @Override
    public void onEnable() {
        instance = this;

        userManager = new UserManager();
        gameManager = new GameManager(this);
        gameRegistry = new GameRegistry();

        gameConfig = new Config(this);
        language = new Language(this, gameConfig);

        // generate words file
        File wordsFile = new File(getDataFolder() + "/words.yml");
        if (!wordsFile.exists()) {
            saveResource("words.yml", false);
        }
        this.words = YamlConfiguration.loadConfiguration(wordsFile);

        saveDefaultConfig();

        Bukkit.getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getCommand("chatgames").setExecutor(commandHandler = new CommandHandler(this));

        if (setupEconomy()) usingVault = true;

        setupDatabase();

        loadGames();

        gameRegistry.getMap().forEach((clazz, game) -> {
            if (clazz.isInstance(IGame.class))
                gameManager.getGamePool().add(game);
            getLogger().info("Registered Game: " + game.name());
        });

        gameManager.startGames();
    }

    @Override
    public void onDisable() {
        gameManager.unload();
    }


    private void loadGames() {
        gameManager.load();
    }

    public void setupDatabase() {
        boolean usingMySQL = getConfig().getBoolean("settings.use-mysql");

        if (!usingMySQL) {
            this.database = new SQLite(this);
        } else {
            // mysql
        }

        database.getNewConnection();

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        getLogger().info("Vault found, enabling Vault integration.");
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
