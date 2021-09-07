package me.pineacle.chatgames.storage.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiConsumer;

public final class Cache<O> {

    private final Map<UUID, O> wrapped = new HashMap<>();

    public void put(UUID uuid, O value) {
        wrapped.put(uuid, value);
    }

    public void putIfAbsent(UUID uuid, O value) {
        wrapped.putIfAbsent(uuid, value);
    }

    public void putAll(Map<Player, O> input) {
        final Map<UUID, O> values = new HashMap<>();
        input.forEach((player, value) -> values.put(player.getUniqueId(), value));
        wrapped.putAll(values);
    }

    public O get(UUID uuid) {
        return wrapped.get(uuid);
    }

    public O getOrDefault(UUID uuid, O defaultValue) {
        return wrapped.getOrDefault(uuid, defaultValue);
    }

    public void remove(UUID uuid) {
        wrapped.remove(uuid);
    }

    public boolean containsKey(UUID uuid) {
        return wrapped.containsKey(uuid);
    }

    public Collection<O> values() {
        return wrapped.values();
    }

    public Set<Map.Entry<UUID, O>> entrySet() {
        return wrapped.entrySet();
    }

    public void forEach(BiConsumer<Player, O> action) {
        wrapped.forEach((uuid, value) -> action.accept(Bukkit.getPlayer(uuid), value));
    }

}
