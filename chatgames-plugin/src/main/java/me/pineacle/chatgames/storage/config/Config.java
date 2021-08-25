package me.pineacle.chatgames.storage.config;

import com.google.common.io.Files;
import lombok.Getter;
import me.pineacle.chatgames.ChatGamesPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Config implements IConfiguration {

    private final ChatGamesPlugin plugin;
    @Getter private FileConfiguration configuration;
    @Getter private String serverLocale;
    @Getter private String storageType;
    @Getter private File configFile;

    public Config(ChatGamesPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
        init();
    }

    @Override
    public void init() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists())
            plugin.saveDefaultConfig();
        int configVersion = this.configuration.getInt("settings.version");
        // if config is not up-to-date
        if (configVersion != 1) {
            String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-'config_backup.yml'").format(new Date());
            File destination = new File(plugin.getDataFolder(), "/backups/" + fileName);
            try {
                Files.copy(configFile, destination);
                configFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            plugin.getLogger().warning("Config is old or is missing information. Creating new file and saving backup.");
            plugin.saveDefaultConfig();
        }
        File languageFolder = new File(plugin.getDataFolder(), "languages/");
        if (!languageFolder.isDirectory()) {
            languageFolder.mkdir();
            try {
                File defaultLanguageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");
                InputStream stream = plugin.getResource("en_US.yml");
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                OutputStream outStream = new FileOutputStream(defaultLanguageFile);
                outStream.write(buffer);
            } catch (IOException e) {
                plugin.getLogger().warning("Couldn't copy language file!");
            }
        }
        this.serverLocale = this.configuration.getString("settings.language");
        if (this.serverLocale == null)
            this.serverLocale = "en_US";
    }

    @Override
    public void reload() {

    }

    @Override
    public FileConfiguration get() {
        return configuration;
    }

}
