package me.pineacle.chatgames.API.user;


import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public interface IUserManager {

    /**
     * Adds amount to the current total of the user with {@link UUID} of uuid.
     *
     * <p><b>Saves to the cache until a save is requested.</b></p>
     *
     * @param uuid UUID of player
     * @param amount amount of wins to add to player
     * @since 1.0.0
     */
    void addWin(@NotNull UUID uuid, @NotNull int amount);

    /**
     * Sets total wins of the user with {@link UUID} of uuid
     *
     * @param uuid UUID of player
     * @param amount amount of wins to add to player
     * @since 1.0.0
     */
    void setWin(@NotNull UUID uuid, @NotNull int amount);

    /**
     * Sets the record of user with {@link UUID} of uuid
     *
     * @param uuid UUID of player
     * @param record amount of wins to add to player
     * @since 1.0.0
     */
    void setRecord(@NotNull UUID uuid, @NotNull int record);

    /**
     * Gets the total wins of user with {@link UUID} of uuid
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
    int getRecord(@NotNull UUID uuid, @NotNull int game_id  );

}
