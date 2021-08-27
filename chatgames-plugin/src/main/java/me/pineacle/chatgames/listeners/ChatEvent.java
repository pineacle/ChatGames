package me.pineacle.chatgames.listeners;

import me.pineacle.chatgames.API.game.Game;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.events.AsyncQuestionAnswerEvent;
import me.pineacle.chatgames.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class ChatEvent implements Listener {

    private final ChatGamesPlugin plugin;
    private final GameManager gameManager;

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
                    if (message.equals(questionAnswerEvent.getQuestion().getAnswers().get(0))) {

                        handleCorrect(winner, e, questionAnswerEvent);
                    }
                } else {
                    if (message.equalsIgnoreCase(questionAnswerEvent.getQuestion().getAnswers().get(0))) {

                        handleCorrect(winner, e, questionAnswerEvent);

                    }
                }
            }
        }
    }

    private void handleCorrect(Player winner, AsyncPlayerChatEvent e, AsyncQuestionAnswerEvent questionAnswerEvent) {
        plugin.sync(() -> Bukkit.getPluginManager().callEvent(questionAnswerEvent));
        if (questionAnswerEvent.isCancelled()) return;

        long start = gameManager.getQuestionTask().getStart();
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;

        long millis = elapsed % 1000;
        long second = (elapsed / 1000) % 60;

        String elapsedString = second + "." + millis;

        questionAnswerEvent.getGame().giveReward(winner, Optional.of(elapsedString));

        gameManager.getQuestionTask().cancel();
        gameManager.setQuestionTask(null);


        e.setCancelled(true);
    }

}
