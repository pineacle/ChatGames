package me.pineacle.chatgames.user;

import lombok.Getter;
import lombok.Setter;
import me.pineacle.chatgames.API.user.IUser;

import java.util.UUID;

/**
 * User object
 */
public class User implements IUser {

    @Getter private final UUID uuid;
    @Getter @Setter private int wins;
    @Getter @Setter private boolean toggled;

    /**
     * New user constructor
     */
    public User(UUID uuid) {
        this.uuid = uuid;
        this.wins = 0;
        this.toggled = true;
    }

    /**
     * Existing user constructor
     */
    public User(UUID uuid, int wins, boolean toggled) {
        this.uuid = uuid;
        this.wins = wins;
        this.toggled = toggled;
    }

}
