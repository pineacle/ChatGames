package me.pineacle.chatgames.storage.database;

import me.pineacle.chatgames.API.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiConsumer;

public final class Cache<user extends User> {

    private final Map<UUID, user> wrapped = new HashMap<>();

    public void put(UUID uuid, user value) {
        wrapped.put(uuid, value);
    }

    public void putIfAbsent(UUID uuid, user value) {
        wrapped.putIfAbsent(uuid, value);
    }

    public void putAll(Map<Player, user> input) {
        final Map<UUID, user> values = new HashMap<>();
        input.forEach((player, value) -> values.put(player.getUniqueId(), value));
        wrapped.putAll(values);
    }

    public user get(UUID uuid) {
        return wrapped.get(uuid);
    }

    public user getOrDefault(UUID uuid, user defaultValue) {
        return wrapped.getOrDefault(uuid, defaultValue);
    }

    public void remove(UUID uuid) {
        wrapped.remove(uuid);
    }

    public boolean containsKey(UUID uuid) {
        return wrapped.containsKey(uuid);
    }

    public Collection<user> values() {
        return wrapped.values();
    }

    public Set<Map.Entry<UUID, user>> entrySet() {
        return wrapped.entrySet();
    }

    public void forEach(BiConsumer<Player, user> action) {
        wrapped.forEach((uuid, value) -> action.accept(Bukkit.getPlayer(uuid), value));
    }

}
