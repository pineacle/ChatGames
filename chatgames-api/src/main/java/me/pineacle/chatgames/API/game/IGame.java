package me.pineacle.chatgames.API.game;

import me.pineacle.chatgames.API.ChatGamesAPI;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface IGame<Question> {

    boolean caseSensitive();

    List<Question> questions();

    int limit();

    List<String> expiredFormat(Question question);

    List<String> format(Question question);

    String name();

    void reward(Player player, Optional<String> elapsed);

    default void register() {
        ChatGamesAPI.getGameManager().register(this);
    }

}
