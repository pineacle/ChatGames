package me.pineacle.chatgames.events;

import me.pineacle.chatgames.game.Game;
import me.pineacle.chatgames.game.Question;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestionAnswerEvent extends ChatGameEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;

    public QuestionAnswerEvent(Game eventGame, Question eventQuestion) {
        super(eventGame, eventQuestion);
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
