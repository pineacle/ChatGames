package me.pineacle.chatgames.utils.tasks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.pineacle.chatgames.ChatGamesPlugin;
import me.pineacle.chatgames.game.Game;
import me.pineacle.chatgames.game.Question;
import me.pineacle.chatgames.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QuestionTask extends BukkitRunnable {

    @Getter private final Game game;
    @Getter private final Question question;
    @Getter private final ChatGamesPlugin plugin;
    @Getter private long start;

    @Getter private BukkitTask assignedTaskId;

    @Getter private int counter = 0;
    private final Consumer<QuestionTask> everySecond;

    @Override
    public void run() {
        this.counter++;
        this.everySecond.accept(this);
    }

    public void ask() {
        this.game.format(question)
                .stream()
                .map(StringUtils::format)
                .collect(Collectors.toList())
                .forEach(line -> Bukkit.getOnlinePlayers().forEach(player -> {
                    if (line.startsWith("{center}"))
                        StringUtils.sendCenteredMessage(player, line.replace("{question}", question.getQuestion()).replace("{center}", ""));
                    else
                        player.sendMessage(line.replace("{question}", question.getQuestion()).replace("{center}", ""));
                }));
    }

    public void scheduleTimer() {
        this.start = System.currentTimeMillis();
        this.assignedTaskId = this.runTaskTimer(plugin, 0L, 20L);
    }
}
