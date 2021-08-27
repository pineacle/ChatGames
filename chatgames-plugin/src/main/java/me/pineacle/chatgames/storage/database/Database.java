package me.pineacle.chatgames.storage.database;

import lombok.SneakyThrows;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.user.User;
import me.pineacle.chatgames.utils.exeptions.UnavailableUserException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class Database {

    protected final ChatGamesPlugin plugin;
    private Cache cache;

    private Connection connection;

    protected Database(final ChatGamesPlugin plugin) {
        this.plugin = plugin;
        this.cache = new Cache();
        plugin.async(() -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.createStatement().execute("/* ping */ SELECT 1");
                }
            } catch (SQLException e) {
                connection = getNewConnection();
            }
        });
    }

    public abstract Connection getNewConnection();

    public abstract boolean isConnected();

    /**
     * Requests User data from the database
     * <br>
     * <b>Save back to the cache/database after editing</b>
     *
     * @param uuid {@link UUID} of the player
     * @return User requested
     * @throws UnavailableUserException if no user is found
     */
    public User request(@NotNull UUID uuid) throws UnavailableUserException {
        return null;
    }

    /**
     * Save users data from the cache
     *
     * @param uuid {@link UUID} of the player
     */
    public void save(@NotNull UUID uuid) {

    }

    /* Queries */
    protected final String CREATE_IF_NOT_EXIST = "";
    //   protected final String INSERT = "INSERT INTO chatgame_players VALUES(?,?,?) ON DUPLICATE KEY UPDATE name=?";

    /**
     * Checks connection
     *
     */
    protected void checkConnection() {
        execute(connection, "CREATE TABLE IF NOT EXISTS `chatgame_players` (`uuid` varchar(64) NOT NULL, `name` varchar(16) NOT NULL, `wins` int NOT NULL, PRIMARY KEY (`uuid`))");
    }

    /**
     * disconnects from the database
     */
    public void shutdown() {

    }

    /**
     * Run asynchronous tasks
     * <br>
     * <b>All database queries and updates must be asynchronous</b>
     */
    protected void async(final @NotNull Runnable runnable) {
        plugin.async(runnable);
    }


    /**
     * Updates table
     *
     * @param statement SQL statement
     */
    @SneakyThrows
    public void update(PreparedStatement statement) {
        if (!isConnected()) return;
        try {
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes query
     *
     * @param connection sql connection
     * @param query      query to execute
     */
    @SneakyThrows
    protected void execute(Connection connection, String query) {
        connection.createStatement().execute(query);
    }

    /**
     * Query the database
     *
     * @param qry query to run
     * @return {@link CompletableFuture} of the {@link ResultSet}
     */
    public CompletableFuture<ResultSet> query(final String qry) {
        if (!isConnected()) return null;

        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = getNewConnection().prepareStatement(qry);
                return ps.executeQuery();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        });
    }

}
