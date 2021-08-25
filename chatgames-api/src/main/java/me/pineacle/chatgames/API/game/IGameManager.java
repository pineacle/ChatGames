package me.pineacle.chatgames.API.game;

import java.util.Optional;

public interface IGameManager<Game, Question> {

    void register(Game type);

    void force(Game type, Optional<Question> question);

}
