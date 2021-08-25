package me.pineacle.chatgames.game;

import lombok.Getter;

import java.util.Hashtable;
import java.util.Map;

public class GameRegistry {

    public GameRegistry() {}

    @Getter private Map<Class<? extends Game>, Game> map = new Hashtable<>();

    /**
     * Registers the game.
     * Only games registered will be in the game rotation.
     */
    public <S extends Game> void register(S game) {
        map.put(game.getClass(), game);
    }

    /**
     * @param type Class of the game.
     * @return Game in rotation.
     */
    public <S extends Game> S get(Class<S> type) {
        return type.cast(map.get(type));
    }


}
