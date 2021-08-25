package me.pineacle.chatgames.storage.config;

import org.bukkit.configuration.file.FileConfiguration;

public interface IConfiguration {

    void init();

    void reload();

    FileConfiguration get();

}
