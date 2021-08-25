package me.pineacle.chatgames.user;

import me.pineacle.chatgames.API.user.IUser;
import me.pineacle.chatgames.API.user.IUserManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserManager implements IUserManager {

    @Override
    public IUser getUser(@NotNull UUID uuid) {
        return null;
    }

    @Override
    public void addWin(@NotNull UUID uuid, @NotNull int amount) {

    }

    @Override
    public void setWin(@NotNull UUID uuid, @NotNull int amount) {

    }

    @Override
    public void setRecord(@NotNull UUID uuid, @NotNull int record) {

    }

    @Override
    public int getWins(@NotNull UUID uuid) {
        return 0;
    }

    @Override
    public int getRecord(@NotNull UUID uuid, @NotNull int game_id) {
        return 0;
    }

    @Override
    public boolean getToggled(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public void setToggled(UUID uuid, boolean value) {
    }
}
