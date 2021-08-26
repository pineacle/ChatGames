package me.pineacle.chatgames.API.game;

import java.util.Optional;

public interface IGameManager<Game, Question> {

    void register(Game type);

    /**
     * Forces a game with optional params
     * @param type
     */
    void force(Optional<Game> type);

}
