package me.pineacle.chatgames.events;

import me.pineacle.chatgames.game.Game;
import me.pineacle.chatgames.game.Question;
import me.pineacle.chatgames.game.reward.Reward;
import org.bukkit.event.HandlerList;

public class QuestionAnswerEvent extends ChatGameEvent {

    private static final HandlerList handlers = new HandlerList();
    private Reward reward;

    public QuestionAnswerEvent(Game eventGame, Question eventQuestion, Reward reward) {
        super(eventGame, eventQuestion);
        this.reward = reward;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
