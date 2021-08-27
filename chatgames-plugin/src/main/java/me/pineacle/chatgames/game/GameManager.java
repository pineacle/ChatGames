package me.pineacle.chatgames.game;

import lombok.Getter;
import lombok.Setter;
import me.pineacle.chatgames.API.game.IGameManager;
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

public class GameManager implements IGameManager<Game, Question>, Loadable {

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

    public GameManager(ChatGamesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Begins and repeats broadcasting the games
     */
    public void startGames() {

        if(!isToggled())
            return;

        if (Bukkit.getOnlinePlayers().size() < plugin.getConfig().getInt("game.required-players")) {
            plugin.getLogger().info("Games are disabled because player requirement is not met");
            return;
        }

        gameSchedulerTask = plugin.syncRepeating(() -> {
            Question currentQuestion = pickQuestion(Optional.empty());
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
    public Question pickQuestion(Optional<Game> game) {

        // pick random game from game pool
        Game currentGame = game.orElse(gamePool.get(ThreadLocalRandom.current().nextInt(gamePool.size())));

        // pick random question from selected game
        Question picked = currentGame.questions().get(ThreadLocalRandom.current().nextInt(currentGame.questions().size()));

        // if repeat question, reroll
        if (!active.isEmpty() && active.containsValue(picked) || active.containsKey(currentGame)) {
            return pickQuestion(Optional.empty());
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

    /**
     * Loads the games that are registered into game pool
     */
    @Override
    public void load() {

        // provided games
        new ExactGame(plugin).register();
        new UnscrambleGame(plugin).register();
        new ReverseGame(plugin).register();
        new MathGame(plugin).register();
        new RandomSequenceGame(plugin).register();

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
    public void force(Optional<Game> game) {

        Game currentGame = game.orElse(gamePool.get(ThreadLocalRandom.current().nextInt(gamePool.size())));
        Question currentQuestion = pickQuestion(Optional.of(currentGame));

        // stop the current question if active
        if (questionTask != null) {
            questionTask.cancel();
            questionTask = null;
        }

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

    }
}
