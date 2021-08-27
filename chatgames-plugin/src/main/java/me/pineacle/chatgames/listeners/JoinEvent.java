package me.pineacle.chatgames.listeners;

import lombok.RequiredArgsConstructor;
import me.pineacle.chatgames.ChatGamesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class JoinEvent implements Listener {

    private final ChatGamesPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (!plugin.getGameManager().isToggled())
            return;
        else {
            if (Bukkit.getOnlinePlayers().size() + 1 >= plugin.getConfig().getInt("game.required-players")) {
                if (plugin.getGameManager().getGameSchedulerTask() != null) {
                    plugin.getGameManager().startGames();
                }
            }
        }
    }

}
