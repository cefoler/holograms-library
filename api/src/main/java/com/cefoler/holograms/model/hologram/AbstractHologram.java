package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.exception.HologramException;
import com.cefoler.holograms.HologramCore;
import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.animation.type.AnimationType;
import com.cefoler.holograms.model.hologram.impl.StandardHologram;
import com.cefoler.holograms.model.lines.Line;
import com.cefoler.holograms.model.lines.impl.ItemLine;
import com.cefoler.holograms.model.lines.impl.TextLine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class AbstractHologram implements Hologram {

  protected final List<Line<?>> lines;

  private final Location location;
  private final List<Player> visiblePlayers;

  private final PlaceholderRegistry placeholders;

  protected AbstractHologram(
      final @NotNull Location location,
      final @Nullable PlaceholderRegistry placeholderRegistry,
      final @NotNull List<Player> visiblePlayers,
      final @NotNull Object... linesObjects
  ) {
    this.location = location;
    this.placeholders = placeholderRegistry
        == null ? new PlaceholderRegistry() : placeholderRegistry;
    this.visiblePlayers = visiblePlayers;
    this.lines = new ArrayList<>(linesObjects.length);

    final Location hologramLocation = location.clone().subtract(0, 0.28, 0);

    int count = 0;
    for (final Object line : linesObjects) {
      count++;
      final double up = line instanceof ItemStack
          ? 0.60D
          : 0.28D;

      if (line instanceof String) {
        final Line<?> textLine = new TextLine(visiblePlayers, count, (String) line, placeholders);
        textLine.setLocation(hologramLocation.clone().add(0.0, up, 0));

        lines.add(count, textLine);
        return;
      }

      if (!(line instanceof ItemStack)) {
        return;
      }

      final Line<?> itemLine = new ItemLine(visiblePlayers, count, (ItemStack) line);
      itemLine.setLocation(hologramLocation.clone().add(0.0, up, 0));

      lines.add(count, itemLine);
    }
  }

  public void setLine(final int index, final @NotNull ItemStack itemStack) {
    final Line<ItemStack> line = (ItemLine) getLine(index);
    line.setLine(itemStack);

    visiblePlayers.forEach(line::update);
  }

  public void setLine(final int index, final @NotNull String text) {
    final Line<String> line = (TextLine) getLine(index);
    line.setLine(text);

    visiblePlayers.forEach(line::update);
  }

  public void setAnimation(final Plugin plugin, final int index, final @NotNull AnimationType animationType) {
    getLine(index).setAnimation(plugin, animationType);
  }

  public void removeAnimation(final int index) {
    getLine(index).removeAnimation();
  }

  public void show(final @NotNull Player player) {
    visiblePlayers.add(player);
    for (final Line<?> line : lines) {
      line.show(player);
    }
  }

  public void hide(final @NotNull Player player) {
    for (final Line<?> line : lines) {
      line.hide(player);
    }

    visiblePlayers.remove(player);
  }

  public Line<?> getLine(final int index) {
    return lines.get(Math.abs(index - lines.size() + 1));
  }

  public boolean isVisible(final @NotNull Player player) {
    return visiblePlayers.contains(player);
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final LinkedList<Object> lines;
    private final PlaceholderRegistry placeholderRegistry;
    private Location location;

    public Builder() {
      this.lines = new LinkedList<>();
      this.placeholderRegistry = new PlaceholderRegistry();
    }

    @NotNull
    public Builder addLine(final @NotNull String line) {
      lines.add(line);
      return this;
    }

    @NotNull
    public Builder addLines(final @NotNull String... linesString) {
      lines.addAll(Arrays.asList(linesString));
      return this;
    }

    @NotNull
    public Builder addLine(final @NotNull ItemStack item) {
      lines.add(item);
      return this;
    }

    @NotNull
    public Builder addLines(final @NotNull ItemStack... items) {
      lines.addAll(Arrays.asList(items));
      return this;
    }

    @NotNull
    public Builder location(final @NotNull Location location) {
      this.location = location;
      return this;
    }

    @NotNull
    public Builder addPlaceholder(final @NotNull String key,
        final @NotNull Function<Player, String> result) {
      placeholderRegistry.add(key, result);
      return this;
    }

    @NotNull
    public Builder addPlaceholders(final Map<String, Function<Player, String>> result) {
      placeholderRegistry.add(result);
      return this;
    }

    @NotNull
    public Hologram build() {
      if (location == null || lines.isEmpty()) {
        throw new HologramException("No location given or not completed");
      }

      final Hologram hologram = new StandardHologram(location,
          placeholderRegistry, new ArrayList<>(), lines.toArray());

      HologramCore.getApi().registerHologram(hologram);
      return hologram;
    }
  }

}
