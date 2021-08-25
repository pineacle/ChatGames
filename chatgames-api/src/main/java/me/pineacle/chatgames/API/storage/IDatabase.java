package me.pineacle.chatgames.API.storage;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings("unused")
public interface IDatabase {

    /**
     * Saves the user with {@link UUID} of uuid from cache
     * @param uuid UUID of {@link org.bukkit.entity.Player}
     * @since 1.0.0
     */
    void save(@NotNull UUID uuid);

    /**
     * Loads the user with {@link UUID} of uuid and saves to the cache
     * @param uuid UUID of {@link org.bukkit.entity.Player}
     */
    void load(@NotNull UUID uuid);

    /**
     * @return Connection to the database
     * @throws SQLException
     */
    Connection getConnection() throws SQLException;

    /**
     * Secures connection to the database
     */
    void connect();

}
