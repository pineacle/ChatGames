package me.pineacle.chatgames.storage.database.tasks;

import lombok.AllArgsConstructor;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.storage.database.Database;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class UpdateTask extends BukkitRunnable {

    private final Database database;
    private final ChatGamesPlugin plugin;

    private final int DATABASE_VERSION = 1;

    @Override
    public void run() {
        createTables();

        int version = getDatabaseVersion(database.getConnection());

        // todo: handle future updates here

        setDatabaseVersion(database.getConnection(), version);

    }

    /**
     * creates the tables if the don't exist
     */
    private void createTables() {
        database.execute(database.getConnection(), database.CREATE_IF_NOT_EXIST);
        database.execute(database.getConnection(), database.CREATE_CONFIG_IF_NOT_EXIST);
    }

    /**
     * @param connection established connection
     * @return servers current database version
     */
    private int getDatabaseVersion(Connection connection) {
        try (PreparedStatement select = connection.prepareStatement("SELECT `value` FROM `chatgame_config` WHERE `setting`='version'")) {
            try (ResultSet resultSet = select.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("value");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    /**
     * @param connection     established connection
     * @param currentVersion returns the database version
     */
    private void setDatabaseVersion(Connection connection, int currentVersion) {
        try (PreparedStatement select = plugin.isUsingMySQL() ? connection.prepareStatement("INSERT INTO `chatgame_config` VALUES('version', ?) ON DUPLICATE KEY UPDATE `value`=?") : connection.prepareStatement("INSERT OR REPLACE INTO `chatgame_config` (setting, value) VALUES('version', ?)")) {
            select.setInt(1, currentVersion);
            if (plugin.isUsingMySQL()) select.setInt(2, currentVersion);
            select.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
