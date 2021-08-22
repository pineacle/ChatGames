package me.pineacle.chatgames.game.games;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.events.QuestionAnswerEvent;
import me.pineacle.chatgames.game.Game;
import org.jetbrains.annotations.NotNull;

public class ExactGame extends Game {

    private ChatGamesPlugin plugin;

    public ExactGame(ChatGamesPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public void register() {
        plugin.getGameRegistry().register(this);
    }

    @Override
    public String format(@NotNull Game game) {
        return "Question! " + game.getCurrentQuestion();
    }

    @Override
    public void onWin(QuestionAnswerEvent questionAnswerEvent) {
    }

    @Override
    public String getName() {
        return "Exact";
    }
}
