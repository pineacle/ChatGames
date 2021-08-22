package me.pineacle.chatgames.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.pineacle.chatgames.API.game.IGame;
import me.pineacle.chatgames.events.QuestionAnswerEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for all game types
 */
public abstract class Game<QUESTION extends Question> implements IGame {

    /**
     * UUID of the game
     */
    protected UUID uuid;


    /**
     * Category/Type of game
     */
    @Getter
    @Setter(AccessLevel.PUBLIC)
    private String category;

    /**
     * The state of the game
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean state;

    /**
     * If the game requires case sensitivity
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean caseSensitive;

    /**
     * Set of questions
     */
    @NotNull
    @Getter
    @Setter(AccessLevel.PUBLIC)
    protected List<QUESTION> questions;

    /**
     * Question next in queue for this game
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private @NotNull QUESTION currentQuestion;

    /**
     * Constructs a new default game
     *
     */
    public @NotNull Game() {
        this.uuid = UUID.randomUUID();
    }


    /**
     * Constructs a new default game with all params
     *
     * @param questions     Questions that should be asked in the game
     * @param state         Whether this game is active
     * @param caseSensitive Require case sensitivity when answering
     */


    /**
     * @return {@link UUID} of the game
     */
    @NotNull
    @Contract(pure = true)
    public UUID getUuid() {
        return uuid;
    }


    /* Abstract methods */

    /**
     * Format for questions in the game
     *
     * @param game Game class
     * @return format for game
     */
    public abstract String format(@NotNull Game game);

    /**
     * Event that triggers after player wins
     *
     * @param questionAnswerEvent
     */
    public abstract void onWin(QuestionAnswerEvent questionAnswerEvent);

    /**
     * Registers the game
     */
    public abstract void register();


}
