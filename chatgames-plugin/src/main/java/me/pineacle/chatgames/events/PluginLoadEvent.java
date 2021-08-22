package me.pineacle.chatgames.events;

import lombok.Getter;
import me.pineacle.chatgames.ChatGamesPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PluginLoadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter private final ChatGamesPlugin plugin;

    public PluginLoadEvent(ChatGamesPlugin chatGamesPlugin) {
        this.plugin = chatGamesPlugin;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
