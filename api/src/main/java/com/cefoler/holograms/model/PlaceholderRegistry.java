package com.cefoler.holograms.model;

import com.celeste.library.spigot.util.message.hex.RgbUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderRegistry {

  private final Map<String, Function<Player, String>> placeholders;

  public PlaceholderRegistry() {
    this.placeholders = new ConcurrentHashMap<>();
  }

  public void add(final @NotNull String key, final @NotNull Function<Player, String> result) {
    placeholders.put(key, result);
  }

  public void add(final Map<String, Function<Player, String>> placeholders) {
    placeholders.forEach(this::add);
  }

  @NotNull
  public String parse(final @NotNull String line, final @NotNull Player player) {
    final AtomicReference<String> clonedLine = new AtomicReference<>(line);
    placeholders.forEach((key, value) -> {
      final String parsed = clonedLine.get().replaceAll(key, value.apply(player));
      clonedLine.set(RgbUtils.process(parsed));
    });

    return clonedLine.get();
  }

}


