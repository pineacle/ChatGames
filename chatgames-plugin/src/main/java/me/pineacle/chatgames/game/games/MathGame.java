package me.pineacle.chatgames.game.games;

import me.pineacle.chatgames.API.game.Game;
import me.pineacle.chatgames.API.game.Question;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.game.GameManager;
import me.pineacle.chatgames.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class MathGame implements Game {

    private final ChatGamesPlugin plugin;
    private final File file;
    private final FileConfiguration configuration;
    private final GameManager gameManager;

    public MathGame(ChatGamesPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder() + "/games/math-game.yml");
        if (!file.exists())
            plugin.saveResource("games/math-game.yml", false);
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.gameManager = plugin.getGameManager();
    }

    /*
    Abstract Methods
     */


    // not necessary
    @Override
    public boolean getCaseSensitive() {
        return false;
    }

    @Override
    public Question getQuestion() {

        return new Question() {

            final List<String> exp = equation();

            @Override
            public Game getGame() {
                return MathGame.this;
            }

            @Override
            public String getQuestion() {
                return exp.get(0);
            }

            @Override
            public List<String> getAnswers() {
                return Collections.singletonList(exp.get(1));
            }
        };

    }

    @Override
    public int getLimit() {
        return configuration.getInt("expires");
    }

    @Override
    public List<String> getExpiredFormat(Question question) {
        return configuration.getStringList("expired-format");
    }

    @Override
    public List<String> getFormat(Question question) {
        return configuration.getStringList("format");
    }

    @Override
    public String getGameName() {
        return "Math";
    }

    @Override
    public @NotNull void giveReward(Player winner, Optional<String> elapsedTime) {

        /*
        Take from the games .yml
         */

        if (configuration.isConfigurationSection("reward-pool")) {

            ArrayList<String> rewards = new ArrayList<>();

            configuration.getConfigurationSection("reward-pool").getKeys(false).forEach(key -> {
                if (plugin.isUsingVault())
                    plugin.getEconomy().depositPlayer(winner, configuration.getInt("reward-pool." + key + ".amount"));
                rewards.add(key);
            });

            String reward = rewards.get(ThreadLocalRandom.current().nextInt(0, rewards.size()));

            configuration.getStringList("reward-pool." + reward + ".commands").forEach(command -> {
                if (command.startsWith("{msg}"))
                    winner.sendMessage(replace(command, "{msg}", winner, elapsedTime));
                else if (command.startsWith("{bc}"))
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(replace(command, "{bc}", winner, elapsedTime)));
                else
                    plugin.sync(() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), replace(command, "{bc}", winner, elapsedTime)));
            });


        }

    }

    private String replace(String command, String replacement, Player winner, Optional<String> elapsedTime) {
        return StringUtils.format(command.replace(replacement, "")
                .replace("{player}", winner.getName())
                .replace("{display_name}", winner.getDisplayName())
                .replace("{answer}", gameManager.getActiveQuestion().getAnswers().get(0))
                .replace("{time}", elapsedTime.orElse("")));
    }

    private List<String> equation() {
        List<String> exp = new ArrayList<>();

        char[] operators = {'+', '-', '*'};

        int first = ThreadLocalRandom.current().nextInt(configuration.getInt("lowest"), configuration.getInt("highest"));
        int second = ThreadLocalRandom.current().nextInt(configuration.getInt("lowest"), configuration.getInt("highest"));

        char operator = operators[ThreadLocalRandom.current().nextInt(operators.length)];

        if (operator == '+') {

            exp.add(first + " + " + second);
            int answer = first + second;

            exp.add(String.valueOf(answer));

        } else if (operator == '*') {

            exp.add(first + " * " + second);
            int answer = first * second;

            exp.add(String.valueOf(answer));

        } else {

            exp.add(first + " - " + second);
            int answer = first - second;
            exp.add(String.valueOf(answer));

        }
        return exp;
    }

}
