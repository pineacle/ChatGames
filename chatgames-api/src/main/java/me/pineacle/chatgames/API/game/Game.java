package me.pineacle.chatgames.API.game;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface Game {

    boolean getCaseSensitive();

    Question getQuestion();

    int getLimit();

    List<String> getExpiredFormat(Question question);

    List<String> getFormat(Question question);

    String getGameName();

    void giveReward(Player player, Optional<String> elapsed);

}
