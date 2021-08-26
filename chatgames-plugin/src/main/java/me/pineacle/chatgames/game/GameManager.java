package me.pineacle.chatgames.game;

import lombok.Getter;
import lombok.Setter;
import me.pineacle.chatgames.API.game.IGameManager;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.game.games.ExactGame;
import me.pineacle.chatgames.game.games.MathGame;
import me.pineacle.chatgames.game.games.ReverseGame;
import me.pineacle.chatgames.game.games.UnscrambleGame;
import me.pineacle.chatgames.utils.Loadable;
import me.pineacle.chatgames.utils.StringUtils;
import me.pineacle.chatgames.utils.tasks.QuestionTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GameManager implements IGameManager<Game, Question>, Loadable {

    private final ChatGamesPlugin plugin;

    /**
     * List of all active game types
     */
    @Getter
    private List<Game> gamePool = new ArrayList<>();

    /**
     * Gets the active Game and Question
     */
    @Getter
    private Map<Game, Question> active = new HashMap(1);

    /**
     * Question currently being ask
     */
    @Getter @Setter
    public QuestionTask questionTask = null;

    /**
     * Game task
     */
    @Getter @Setter
    public BukkitTask gameSchedulerTask = null;

    public GameManager(ChatGamesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Begins and repeats broadcasting the games
     */
    public void startGames() {

        plugin.getLogger().info("Starting games..");

        gameSchedulerTask = plugin.syncRepeating(() -> {
            Question currentQuestion = pickQuestion();
            questionTask = new QuestionTask(currentQuestion.getGame(), currentQuestion, plugin, sec -> {
                if (sec.getCounter() == currentQuestion.getGame().limit()) {
                    currentQuestion.getGame().expiredFormat(currentQuestion)
                            .stream()
                            .map(StringUtils::format)
                            .collect(Collectors.toList())
                            .forEach(line -> Bukkit.getOnlinePlayers()
                                    .forEach(player -> player.sendMessage(line.replace("{answer}", currentQuestion.getAnswers().get(0)))));
                    plugin.cancelTask(questionTask.getAssignedTaskId());
                    questionTask = null;
                }
            });
            questionTask.ask();
            questionTask.scheduleTimer();
        }, 0L, /* plugin.getGameConfig().getConfiguration().getInt("game.game-gap") * 20 * 60 */ 500L  );

    }

    /**
     * Stops the games
     */
    public void stopGames() {
        plugin.getLogger().info("Stopping games..");
        gameSchedulerTask.cancel();
        gameSchedulerTask = null;
    }

    /**
     * Picks a random game/question from the game pool
     *
     * @return selected question
     */
    public Question pickQuestion() {

        // pick random game from game pool
        Game currentGame = gamePool.get(ThreadLocalRandom.current().nextInt(gamePool.size()));

        // pick random question from selected game
        Question picked = currentGame.questions().get(ThreadLocalRandom.current().nextInt( currentGame.questions().size()));

        // if repeat question, reroll
        if (!active.isEmpty() && active.containsValue(picked) || active.containsKey(currentGame)) {
            return pickQuestion();
        }

        // set the active Game and Question
        active.clear();
        active.put(currentGame, picked);

        return picked;
    }


    /**
     * @return Currently asked question, null if none.
     */
    public Question getActiveQuestion() {
        Optional<Game> firstKey = active.keySet().stream().findFirst();
        if (firstKey.isPresent()) {
            Game key = firstKey.get();
            return active.get(key);
        }
        return null;
    }


    @Override
    public void unload() {
        plugin.getGameRegistry().getMap().clear();
        active.clear();
        getGamePool().clear();
    }

    @Override
    public void load() {

        // provided games
        new ExactGame(plugin).register();
        new UnscrambleGame(plugin).register();
        new ReverseGame(plugin).register();
        new MathGame(plugin).register();

        // log and load registered games
        plugin.getGameRegistry().getMap().forEach((clazz, game) -> {
            gamePool.add(game);
            plugin.getLogger().info("Registered Game: " + game.name());
        });

    }

    @Override
    public void register(Game game) {
        plugin.getGameRegistry().register(game);
    }


    @Override
    public void force(Game type, Optional<Question> question) {
        // TODO
    }
}
