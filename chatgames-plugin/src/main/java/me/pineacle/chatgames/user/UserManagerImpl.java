package me.pineacle.chatgames.user;

import me.pineacle.chatgames.API.user.User;
import me.pineacle.chatgames.API.user.UserManager;
import me.pineacle.chatgames.ChatGamesPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserManagerImpl implements UserManager {

    private final ChatGamesPlugin plugin;

    public UserManagerImpl(ChatGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public User getUser(@NotNull UUID uuid) {
        return plugin.getDatabase().getCache().get(uuid);
    }

    @Override
    public void addWin(@NotNull UUID uuid, @NotNull int amount) {
        plugin.getDatabase().getCache().get(uuid).setWins(getWins(uuid) + 1);
    }

    @Override
    public void setWin(@NotNull UUID uuid, @NotNull int amount) {
        plugin.getDatabase().getCache().get(uuid).setWins(amount);
    }

    @Override
    public int getWins(@NotNull UUID uuid) {
        return plugin.getDatabase().getCache().get(uuid).getWins();
    }

    @Override
    public boolean isToggled(@NotNull UUID uuid) {
        return plugin.getDatabase().getCache().get(uuid).isToggled();
    }

    @Override
    public void setToggled(UUID uuid, boolean value) {
        plugin.getDatabase().getCache().get(uuid).setToggled(value);
    }
}
