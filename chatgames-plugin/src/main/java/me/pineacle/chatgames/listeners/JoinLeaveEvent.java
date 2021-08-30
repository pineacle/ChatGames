package me.pineacle.chatgames.listeners;

import lombok.RequiredArgsConstructor;
import me.pineacle.chatgames.ChatGamesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class JoinLeaveEvent implements Listener {

    private final ChatGamesPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        // add to cache is not already
        if (plugin.getDatabase().isStored(e.getPlayer().getUniqueId())) {
            plugin.getDatabase().request(e.getPlayer().getUniqueId());
        } else {
            plugin.getDatabase().create(e.getPlayer().getUniqueId());
        }

        if (!plugin.getGameManager().isToggled())
            return;
        else {
            if (plugin.getGameManager().meetsPlayerRequirement()) {
                if (plugin.getGameManager().getGameSchedulerTask() != null) {
                    plugin.getGameManager().startGames();
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        if (plugin.getDatabase().getCache().get(e.getPlayer().getUniqueId()) != null) {
            plugin.async(() -> plugin.getDatabase().save(plugin.getDatabase().getCache().get(e.getPlayer().getUniqueId())));
        }

        if (!plugin.getGameManager().meetsPlayerRequirement()) {
            if (plugin.getGameManager().getGameSchedulerTask() != null) {
                plugin.getGameManager().stopGames();
                plugin.getLogger().info("Player requirement has been lost, stopping games.");
            }
        }
    }

}
