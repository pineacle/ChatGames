package me.pineacle.chatgames.storage.database.mysql;

import lombok.SneakyThrows;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.storage.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class MySQL extends Database {

    public MySQL(ChatGamesPlugin plugin) {
        super(plugin);
    }

    @SneakyThrows
    @Override
    public Connection getNewConnection() {

        CompletableFuture<Connection> conFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return DriverManager.getConnection(
                        "jdbc:mysql://" + plugin.getConfig().getString("settings.mysql.host")
                                + ":" + plugin.getConfig().getString("settings.mysql.port") + "/"
                                + plugin.getConfig().getString("settings.mysql.database")
                                + "?autoReconnect=true", plugin.getConfig().getString("settings.mysql.user"), plugin.getConfig().getString("settings.mysql.password"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

        return conFuture.get();
    }
}
