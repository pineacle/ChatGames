package me.pineacle.chatgames.API.game;

import java.util.Optional;

public interface GameManager {

    /**
     * <strong>Only registered games, will be in game pool</strong>
     *
     * @param type game object
     */
    void register(Game type);

    /**
     * Forces a new question to be asked
     *
     * @param game If no game given, will force a random question
     */
    void force(Optional<Game> game);

    /**
     * Picks a question object, <strong>does not ask question </strong>
     *
     * @param game If no game given, will provide a random question
     * @return question object
     */
    Question pickQuestion(Optional<Game> game);

    /**
     * @return active question, null if none
     */
    Question getActiveQuestion();

}
