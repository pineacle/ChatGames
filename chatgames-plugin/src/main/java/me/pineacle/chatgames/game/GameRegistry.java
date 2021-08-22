package me.pineacle.chatgames.game;

import lombok.Getter;
import me.pineacle.chatgames.API.game.IGame;

import java.util.IdentityHashMap;

public class GameRegistry {

    public GameRegistry() {}

    @Getter private IdentityHashMap<Class<? extends IGame>, IGame> map = new IdentityHashMap<>();

    public <S extends IGame> void register(S game) {
        map.put(game.getClass(), game);
    }

    public <S extends IGame> S get(Class<S> type) {
        return type.cast(map.get(type));
    }


}
