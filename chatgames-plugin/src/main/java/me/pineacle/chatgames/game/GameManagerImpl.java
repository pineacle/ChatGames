package me.pineacle.chatgames.game;

import lombok.Getter;
import lombok.Setter;
import me.pineacle.chatgames.API.game.Game;
import me.pineacle.chatgames.API.game.GameManager;
import me.pineacle.chatgames.API.game.Question;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.game.games.*;
import me.pineacle.chatgames.utils.Loadable;
import me.pineacle.chatgames.utils.StringUtils;
import me.pineacle.chatgames.utils.tasks.QuestionTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GameManagerImpl implements GameManager, Loadable {

    private final ChatGamesPlugin plugin;

    /**
     * List of all active game types
     */
    @Getter private List<Game> gamePool = new ArrayList<>();

    /**
     * Gets the active Game and Question
     */
    @Getter private Map<Game, Question> active = new HashMap(1);

    /**
     * Question currently being ask
     */
    @Getter @Setter public QuestionTask questionTask = null;

    /**
     * Game task
     */
    @Getter @Setter public BukkitTask gameSchedulerTask = null;

    @Getter @Setter private boolean toggled = true;

    public GameManagerImpl(ChatGamesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Begins and repeats broadcasting the games
     */
    public void startGames() {

        gameSchedulerTask = plugin.syncRepeating(() -> {
            Question currentQuestion = pickQuestion(Optional.empty());
            questionTask = new QuestionTask(currentQuestion.getGame(), currentQuestion, plugin, sec -> {
                if (sec.getCounter() == currentQuestion.getGame().getLimit()) {

                    currentQuestion.getGame().getExpiredFormat(currentQuestion)
                            .stream()
                            .map(StringUtils::format)
                            .collect(Collectors.toList())

                            .forEach(line -> Bukkit.getOnlinePlayers()
                                    .forEach(player -> {  // don't send if hidden
                                        if (!plugin.getDatabase().getCache().get(player.getUniqueId()).isToggled())
                                            return;
                                        player.sendMessage(line.replace("{answer}", currentQuestion.getAnswers().get(0)));
                                    }));
                    plugin.cancelTask(questionTask.getAssignedTaskId());
                    questionTask = null;
                }
            });
            questionTask.ask();
            questionTask.scheduleTimer();
        }, 0L, plugin.getGameConfig().getConfiguration().getInt("game.game-gap") * 20 * 60);
    }

    /**
     * Stops the games
     */
    public void stopGames() {
        gameSchedulerTask.cancel();
        gameSchedulerTask = null;

        if (questionTask != null) {
            questionTask.cancel();
            questionTask = null;
        }

    }

    /**
     * Picks a random game/question from the game pool
     *
     * @return selected question
     */
    @Override
    public Question pickQuestion(Optional<Game> game) {
        // pick random game from game pool
        Game currentGame = game.orElse(gamePool.get(ThreadLocalRandom.current().nextInt(gamePool.size())));

        // pick random question from selected game
        Question picked = currentGame.getQuestion();

        // if repeat question, reroll
        if (!active.isEmpty() && active.containsValue(picked)) {
            return pickQuestion(game);
        }

        // set the active Game and Question
        active.clear();
        active.put(currentGame, picked);

        return picked;
    }


    /**
     * @return Currently asked question, null if none.
     */
    @Override
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

    /**
     * Loads the games that are registered into game pool
     */
    @Override
    public void load() {

        List<Game> games = Arrays.asList(

                // provided games
                new ExactGame(plugin),
                new UnscrambleGame(plugin),
                new ReverseGame(plugin),
                new MathGame(plugin),
                new DecipherGame(plugin),
                new RandomSequenceGame(plugin));

        games.forEach(this::register);

    }

    @Override
    public void force(Optional<Game> game) {

        Game currentGame = game.orElse(gamePool.get(ThreadLocalRandom.current().nextInt(gamePool.size())));
        Question currentQuestion = pickQuestion(Optional.of(currentGame));

        // stop the current question if active
        if (questionTask != null) {
            questionTask.cancel();
            questionTask = null;
        }

        questionTask = new QuestionTask(currentQuestion.getGame(), currentQuestion, plugin, sec -> {
            if (sec.getCounter() == currentQuestion.getGame().getLimit()) {
                currentQuestion.getGame().getExpiredFormat(currentQuestion)
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

    }

    public boolean meetsPlayerRequirement() {
        if (Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("game.required-players")) {
            return true;
        }
        return false;
    }

    @Override
    public void register(Game type) {
        plugin.getGameRegistry().register(type);
        gamePool.add(type);
        plugin.getLogger().info("Registered Game: " + type.getGameName());
    }
}
