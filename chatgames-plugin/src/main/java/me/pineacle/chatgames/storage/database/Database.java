package me.pineacle.chatgames.storage.database;

import lombok.Getter;
import lombok.SneakyThrows;
import me.pineacle.chatgames.API.user.User;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.storage.database.tasks.UpdateTask;
import me.pineacle.chatgames.user.UserImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class Database {

    protected final ChatGamesPlugin plugin;
    @Getter private final Cache<UserImpl> cache;

    @Getter private final Connection connection;

    /* Queries */
    public final String CREATE_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS `chatgame_players` (`uuid` varchar(64) NOT NULL, `wins` int NOT NULL, `toggled` boolean NOT NULL DEFAULT false, PRIMARY KEY (`uuid`))";
    public final String CREATE_CONFIG_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS `chatgame_config` (`setting` varchar(16) NOT NULL, `value` varchar(200) NOT NULL, PRIMARY KEY (`setting`))"; // to check for updates

    public final String INSERT = "INSERT INTO `chatgame_players` VALUES(?,?,?)";
    public final String UPDATE = "UPDATE `chatgame_players` SET wins=?, toggled=? WHERE uuid=?";

    protected Database(final ChatGamesPlugin plugin) {
        this.plugin = plugin;
        this.cache = new Cache<>();
        connection = getNewConnection();
        if (isConnected()) {
            new UpdateTask(this).runTaskAsynchronously(plugin);
        }
    }

    /**
     * Establishes a new connection
     *
     * @return connection
     */
    public abstract Connection getNewConnection();

    /**
     * Returns if a connection is established
     */
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Requests User data from the database and stores to cache
     *
     * @param uuid {@link UUID} of the player
     */
    public void request(UUID uuid) {
        Objects.requireNonNull(uuid, "UUID cannot be null");
        query("SELECT * FROM `chatgame_players` WHERE uuid='" + uuid + "'").thenApply(resultSet -> {
            try {
                if (resultSet.next()) {
                    UserImpl user = new UserImpl(UUID.fromString(resultSet.getString(1)), resultSet.getInt(2), resultSet.getBoolean(3));
                    cache.put(uuid, user);
                    return user;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        });

    }

    /**
     * Create a user in the database
     *
     * @param uuid {@link UUID} of the player
     */
    public void create(UUID uuid) {
        Objects.requireNonNull(uuid, "UUID cannot be null");
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, uuid.toString()); // uuid
            statement.setInt(2, 0); // wins
            statement.setBoolean(3, true); // toggled on by default
            statement.addBatch();
            update(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            UserImpl user = new UserImpl(uuid, 0, true);
            cache.put(uuid, user);
        }
    }

    /**
     * Save users data from the cache
     */
    @SneakyThrows
    public void save(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        PreparedStatement statement = connection.prepareStatement(UPDATE);
        try {
            statement.setInt(1, user.getWins());
            statement.setBoolean(2, user.isToggled());
            statement.setString(3, user.getUuid().toString());
        } catch (Exception ignored) {
        }
        update(statement);
        cache.remove(user.getUuid());
    }

    /**
     * Queries if a player is stored in database
     *
     * @param uuid player uuid
     * @return if player is stored in database
     */
    @SneakyThrows
    public boolean isStored(UUID uuid) {
        if(!isConnected()) throw new SQLException("Database was not able to connect, and therefore cannot process request.");

        ResultSet resultSet = query("SELECT * FROM chatgame_players WHERE uuid= '" + uuid.toString() + "'").get();

        if (resultSet != null)
            try {
                if (resultSet.next())
                    return (resultSet.getString("uuid") != null);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        return false;
    }

    /**
     * disconnects from the database
     * does this need to be done async?
     */
    public void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
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
    public void execute(Connection connection, String query) {
        connection.createStatement().execute(query);
    }

    /**
     * Query the database asynchronously
     *
     * @param qry query to run
     * @return {@link CompletableFuture} of the {@link ResultSet}
     */
    public CompletableFuture<ResultSet> query(final String qry) {
        if (!isConnected()) return null;

        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = connection.prepareStatement(qry);
                return ps.executeQuery();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        });
    }

}
