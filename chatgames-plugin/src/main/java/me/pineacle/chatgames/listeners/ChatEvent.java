package me.pineacle.chatgames.listeners;

import me.pineacle.chatgames.API.game.Game;
import me.pineacle.chatgames.API.user.User;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.events.AsyncQuestionAnswerEvent;
import me.pineacle.chatgames.game.GameManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class ChatEvent implements Listener {

    private final ChatGamesPlugin plugin;
    private final GameManagerImpl gameManager;

    public ChatEvent(ChatGamesPlugin plugin) {
        this.plugin = plugin;
        gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        Player winner = e.getPlayer();
        String message = e.getMessage();

        // no active games
        if (gameManager.getActive().isEmpty()) return;

        if (!(gameManager.getQuestionTask() == null)) {

            Optional<Game> optionalGame = gameManager.getActive().keySet().stream().findFirst();

            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();

                // prepare events
                AsyncQuestionAnswerEvent questionAnswerEvent = new AsyncQuestionAnswerEvent(game, gameManager.getActive().get(game));

                if (game.getCaseSensitive()) {
                    if (questionAnswerEvent.getQuestion().getAnswers().stream().anyMatch(s -> s.equals(message))) {
                        handleCorrect(winner, e, questionAnswerEvent);
                    }
                } else {
                    if (questionAnswerEvent.getQuestion().getAnswers().stream().anyMatch(s -> s.equalsIgnoreCase(message))) {
                        handleCorrect(winner, e, questionAnswerEvent);
                    }
                }
            }
        }
    }

    private void handleCorrect(Player winner, AsyncPlayerChatEvent e, AsyncQuestionAnswerEvent questionAnswerEvent) {
        Bukkit.getPluginManager().callEvent(questionAnswerEvent);
        if (questionAnswerEvent.isCancelled()) return;

        plugin.getUserManager().addWin(winner.getUniqueId(), 1);

        long start = gameManager.getQuestionTask().getStart();
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;

        String time = String.valueOf((elapsed / 10L) / 100D);

        questionAnswerEvent.getGame().giveReward(winner, time, e.getMessage());

        gameManager.getQuestionTask().cancel();
        gameManager.setQuestionTask(null);

        if (!plugin.getConfig().getBoolean("show-players-message")) e.setCancelled(true);
    }

}
