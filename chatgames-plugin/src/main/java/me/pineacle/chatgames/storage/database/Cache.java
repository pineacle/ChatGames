package me.pineacle.chatgames.storage.database;

import me.pineacle.chatgames.user.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Cache {

    private final Set<User> cache;

    public Cache() {
        cache = new HashSet<>();
    }

    public void update(UUID uuid) {

    }

    public boolean removeCachedUser(User user) {
        return cache.remove(user);
    }

    public User getCachedUser(UUID uuid) {
        for(User user : cache) {
            if(user.getUuid().equals(uuid))
                return user;
        }
        return null;
    }

}
