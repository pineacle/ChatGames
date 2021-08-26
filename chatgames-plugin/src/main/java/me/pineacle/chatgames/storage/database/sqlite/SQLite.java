package me.pineacle.chatgames.storage.database.sqlite;

import lombok.SneakyThrows;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.storage.database.DatabaseBackend;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends DatabaseBackend {

    private Connection localConnection;

    protected SQLite(ChatGamesPlugin plugin) {
        super(plugin);
    }

    @SneakyThrows
    @Override
    protected Connection getConnection() {
        File dataFolder = new File(plugin.getDataFolder(), "database.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (localConnection != null && !localConnection.isClosed()) {
                return localConnection;
            }
            Class.forName("org.sqlite.JDBC");
            localConnection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return localConnection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected boolean isConnected() {
        return false;
    }


    @Override
    public void connect() {

    }


}
