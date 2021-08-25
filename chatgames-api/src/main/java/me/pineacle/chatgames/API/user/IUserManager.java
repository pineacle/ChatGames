package me.pineacle.chatgames.API.user;


import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public interface IUserManager {

    /**
     * @return User object
     *
     * @param uuid   UUID of player
     * @since 1.0.0
     */
    IUser getUser(@NotNull UUID uuid);

    /**
     * Adds amount to the current total of the user
     * <br>
     * <b>Saves to the cache until a save to database is requested.</b>
     *
     * @param uuid   UUID of player
     * @param amount amount of wins to add to player
     * @since 1.0.0
     */
    void addWin(@NotNull UUID uuid, @NotNull int amount);

    /**
     * Sets total wins of the user
     *
     * @param uuid   UUID of player
     * @param amount amount of wins to add to player
     * @since 1.0.0
     */
    void setWin(@NotNull UUID uuid, @NotNull int amount);

    /**
     * Sets the record of user
     *
     * @param uuid   UUID of player
     * @param record amount of wins to add to player
     * @since 1.0.0
     */
    void setRecord(@NotNull UUID uuid, @NotNull int record);

    /**
     * Gets the total wins of user
     *
     * @param uuid UUID of player
     * @since 1.0.0
     */
    int getWins(@NotNull UUID uuid);

    /**
     * Gets the record of user with {@link UUID} of uuid
     *
     * @param uuid UUID of player
     * @since 1.0.0
     */
    int getRecord(@NotNull UUID uuid, @NotNull int game_id);

    /**
     * Gets if the player has games toggled or not
     *
     * @param uuid UUID of player
     * @return if player has games toggled
     */
    boolean getToggled(@NotNull UUID uuid);

    /**
     * Sets players chat games visibility
     *
     * @param uuid UUID of player
     * @param value true / false
     */
    void setToggled(UUID uuid, boolean value);

}
