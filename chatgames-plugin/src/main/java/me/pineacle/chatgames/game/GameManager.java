package me.pineacle.chatgames.game;

import me.pineacle.chatgames.API.game.IGame;
import me.pineacle.chatgames.API.game.IGameManager;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.utils.Loadable;

import java.util.List;

public class GameManager implements IGameManager, Loadable {

    private final ChatGamesPlugin plugin;
    private List<Game> games;

    public GameManager(ChatGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void unload() {

    }

    @Override
    public void load() {

    }

    @Override
    public void register(IGame game) {
        plugin.getGameRegistry().register(game);
    }
}
