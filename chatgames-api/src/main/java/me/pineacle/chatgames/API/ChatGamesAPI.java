package me.pineacle.chatgames.API;

import lombok.Getter;
import me.pineacle.chatgames.API.game.IGameManager;
import me.pineacle.chatgames.API.user.IUser;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ChatGamesAPI {

    @Getter private static IChatGames plugin;

    /**
     * Gets the User from the player.
     *
     * @param player Requested player
     * @return User object
     */
    public static IUser getUser(Player player) {
       return plugin.getUserManager().getUser(player.getUniqueId());
    }

    /**
     * Gets the game manager
     *
     * @return GameManager singleton
     */
    public static IGameManager getGameManager() { return plugin.getGameManager();}

}
