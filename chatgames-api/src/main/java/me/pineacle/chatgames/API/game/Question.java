package me.pineacle.chatgames.API.game;

import java.util.List;

public interface Question {

    Game getGame();

    String getQuestion();

    List<String> getAnswers();

}
