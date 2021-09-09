package me.pineacle.chatgames.storage.database.sqlite;

import lombok.SneakyThrows;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.storage.database.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class SQLite extends Database {

    public SQLite(ChatGamesPlugin plugin) {
        super(plugin);
    }

    @SneakyThrows
    @Override
    public Connection getNewConnection() {
        File dataFolder = new File(plugin.getDataFolder(), "database.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");

            CompletableFuture<Connection> conFuture = CompletableFuture.supplyAsync(() -> {

                try {
                    return DriverManager.getConnection("jdbc:sqlite:" + dataFolder.getAbsolutePath());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return null;
            });

            return conFuture.get();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
