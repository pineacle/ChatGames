package me.pineacle.chatgames.game.games;

import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.game.Game;
import me.pineacle.chatgames.game.GameManager;
import me.pineacle.chatgames.game.Question;
import me.pineacle.chatgames.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSequenceGame extends Game {

    private ChatGamesPlugin plugin;
    private File file;
    private FileConfiguration configuration;
    private GameManager gameManager;

    public RandomSequenceGame(ChatGamesPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder() + "/games/randomsequence-game.yml");
        if (!file.exists())
            plugin.saveResource("games/randomsequence-game.yml", false);
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.gameManager = plugin.getGameManager();
    }

    /*
    Abstract Methods
     */

    @Override
    public boolean caseSensitive() {
        return configuration.getBoolean("case-sensitive");
    }

    @Override
    public List<Question> questions() {

        List<Question> questionList = new ArrayList<>();
        String sequence = sequence();

        questionList.add(new Question(this, sequence, Collections.singletonList(sequence)));

        return questionList;

    }

    @Override
    public int limit() {
        return configuration.getInt("expires");
    }

    @Override
    public List<String> expiredFormat(Question question) {
        return configuration.getStringList("expired-format");
    }

    @Override
    public List<String> format(Question question) {
        return configuration.getStringList("format");
    }

    @Override
    public String name() {
        return "Random";
    }

    @Override
    public @NotNull void reward(Player winner, Optional<String> elapsedTime) {

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

    private String sequence() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        int bound = ThreadLocalRandom.current().nextInt(configuration.getInt("lowest"), configuration.getInt("highest"));

        StringBuilder sb = new StringBuilder(bound);
        Random random = new Random();
        for (int i = 0; i < bound; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString().trim();
    }
}
