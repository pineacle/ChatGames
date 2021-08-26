package me.pineacle.chatgames.storage.config;

import lombok.Getter;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.utils.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;

public class Language implements IConfiguration {

    private final ChatGamesPlugin plugin;
    private final Config configManager;

    @Getter private final HashMap<String, String> translationMap;

    public Language(ChatGamesPlugin plugin, Config config) {
        this.plugin = plugin;
        this.configManager = config;
        this.translationMap = new HashMap<>();
        init();
    }

    @Override
    public void init() {
        if (!this.configManager.getServerLocale().equals("en_US")) {
            File langFile = new File(this.plugin.getDataFolder(), "languages/" + this.configManager.getServerLocale() + ".yml");
            if (!langFile.exists()) {
                this.plugin.getLogger().warning("Defaulting language to en_US. Couldn't find the language file '" + this.configManager.getServerLocale() + ".yml'");
            } else {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(langFile);
                for (String s : yamlConfiguration.getKeys(false)) {
                    this.translationMap.put(s, yamlConfiguration.getString(s));
                }
                this.plugin.getLogger().warning("Found language file '" + this.configManager.getServerLocale() + ".yml'");
            }
        } else {
            File langFile = new File(this.plugin.getDataFolder(), "languages/en_US.yml");
            if (!langFile.exists())
                try {
                    File defaultLanguageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");
                    InputStream stream = plugin.getResource("en_US.yml");
                    byte[] buffer = new byte[stream.available()];
                    stream.read(buffer);
                    OutputStream outStream = new FileOutputStream(defaultLanguageFile);
                    outStream.write(buffer);
                } catch (IOException e) {
                    this.plugin.getLogger().warning("Couldn't copy language file!");
                }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(langFile);
            for (String s : yamlConfiguration.getKeys(false)) {
                this.translationMap.put(s, yamlConfiguration.getString(s));
            }
            this.plugin.getLogger().warning("Found language file '" + this.configManager.getServerLocale() + ".yml'");
        }
    }

    @Override
    public void reload() {
        getTranslationMap().clear();
        init();
    }

    @Override
    public FileConfiguration get() {
        return null; // unused, use #getTranslationMap()
    }

    public String get(String path) {
        return StringUtils.format(getTranslationMap().get(path));
    }
}
