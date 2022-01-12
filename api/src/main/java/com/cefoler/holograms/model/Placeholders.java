package com.cefoler.holograms.model;

import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class Placeholders {

    private final Map<String, Function<Player, String>> placeholders;

    public Placeholders() {
        this.placeholders = new ConcurrentHashMap<>();
    }

    public void add(final @NotNull String key, final @NotNull Function<Player, String> result) {
        placeholders.put(key, result);
    }

    public void add(final Map<String, Function<Player, String>> placeholders) {
        placeholders.forEach(this::add);
    }

    @NotNull
    public String parse(@NotNull String line, @NotNull Player player) {
        final AtomicReference<String> clonedLine = new AtomicReference<>(line);
        placeholders.forEach((key, value) -> clonedLine.set(clonedLine.get().replaceAll(key, value.apply(player))));

        return clonedLine.get();
    }

}


