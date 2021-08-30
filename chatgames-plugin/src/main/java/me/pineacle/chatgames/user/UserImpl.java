package me.pineacle.chatgames.user;

import lombok.Getter;
import lombok.Setter;
import me.pineacle.chatgames.API.user.User;

import java.util.UUID;

/**
 * User object
 */
public class UserImpl implements User {

    @Getter private final UUID uuid;
    @Getter @Setter private int wins;
    @Getter @Setter private boolean toggled;

    /**
     * User constructor
     */
    public UserImpl(UUID uuid, int wins, boolean toggled) {
        this.uuid = uuid;
        this.wins = wins;
        this.toggled = toggled;
    }

}
