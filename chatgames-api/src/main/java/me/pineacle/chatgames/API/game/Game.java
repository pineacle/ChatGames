package me.pineacle.chatgames.API.game;

import org.bukkit.entity.Player;

import java.util.List;

public interface Game {

    boolean getCaseSensitive();

    Question getQuestion();

    int getLimit();

    List<String> getExpiredFormat(Question question);

    List<String> getFormat(Question question);

    String getGameName();

    void giveReward(Player player, String elapsed, String answer);

}
