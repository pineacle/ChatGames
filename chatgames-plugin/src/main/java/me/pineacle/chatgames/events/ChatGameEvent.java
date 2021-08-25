package me.pineacle.chatgames.events;

import me.pineacle.chatgames.game.Game;
import me.pineacle.chatgames.game.Question;
import org.bukkit.event.Event;

/**
 * Base class for all ChatGame events
 */
public abstract class ChatGameEvent extends Event {

    protected Game game;
    protected Question question;

    public ChatGameEvent(Game eventGame, Question eventQuestion) {
        this.game = eventGame;
        this.question = eventQuestion;
    }

    /**
     * @return Current game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return Question asked.
     */
    public Question getQuestion() {
        return question;
    }

}
