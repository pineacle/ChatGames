package me.pineacle.chatgames.game;

import me.pineacle.chatgames.ChatGamesPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Base class for all game types
 */
public abstract class Game {

    /**
     * If the game requires case sensitivity
     */
    public abstract boolean caseSensitive();

    /**
     * Set of questions
     */
    public abstract List<Question> questions();

    /**
     * Time until the question expires, not the game
     */
    public abstract int limit();

    /**
     * Format for questions in the game
     *
     * @return format for game
     */
    public abstract List<String> expiredFormat(Question question);

    /**
     * Format for questions in the game
     *
     * @return format for game
     */
    public abstract List<String> format(Question question);

    /**
     * Name of the game (ex. Unscramble, Reverse)
     *
     * @return Game name
     */
    public abstract String name();

    /**
     * @return List of rewards
     */
    public abstract @NotNull void reward(Player winner, Optional<String> elapsedTime);

    /* Provided methods */

    /**
     * Registers the game
     * <br>
     * <b>Must register the game for it to be in rotation</b>
     */
    public void register() {
        ChatGamesPlugin.getInstance().getGameRegistry().register(this);
    }


}
