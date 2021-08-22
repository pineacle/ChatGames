package me.pineacle.chatgames.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public abstract class User {

    @Getter private final UUID uuid;
    @Getter @Setter(AccessLevel.PRIVATE)
    private int wins;

    /**
     * New user contructor
     * @param uuid
     */
    public User(UUID uuid) {
        this.uuid = uuid;
        this.wins = 0;
    }
}
