package me.pineacle.chatgames.events;

import me.pineacle.chatgames.game.Game;
import me.pineacle.chatgames.game.Question;
import org.bukkit.event.Event;

public abstract class ChatGameEvent extends Event {

    protected Game game;
    protected Question question;

    public ChatGameEvent(Game eventGame, Question eventQuestion) {
        this.game = eventGame;
        this.question = eventQuestion;
    }

    /**
     * Returns event game
     *
     * @return event game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns event question
     *
     * @return event question
     */
    public Question getQuestion() {
        return question;
    }

}
