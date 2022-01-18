package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.exception.HologramException;
import com.cefoler.holograms.HologramCore;
import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.animation.Animation;
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
import lombok.Setter;
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

  @Setter
  private int range;

  protected AbstractHologram(
      final @NotNull Location location,
      final @Nullable PlaceholderRegistry placeholderRegistry,
      final @NotNull List<Player> visiblePlayers,
      final @NotNull LinkedList<Object> linesObjects,
      final int range
  ) {
    this.location = location;
    this.placeholders = placeholderRegistry
        == null ? new PlaceholderRegistry() : placeholderRegistry;
    this.visiblePlayers = visiblePlayers;
    this.lines = new ArrayList<>(linesObjects.size());
    this.range = range;

    final Location hologramLocation = location.clone().add(0, 0.28, 0);

    double height = 0.0D;
    int entityId = 10;

    for (final Object line : linesObjects) {
      entityId = entityId + 10;

      final double currentHeight = height;
      final double up = line instanceof ItemStack
          ? 0.60D + currentHeight
          : 0.28D + currentHeight;

      height = up;

      final Location lineLocation = hologramLocation.clone().subtract(0.0, up, 0);
      if (line instanceof String) {
        final Line<?> textLine = new TextLine(visiblePlayers, entityId, (String) line, placeholders);
        textLine.setLocation(lineLocation);

        lines.add(textLine);
        continue;
      }

      if (!(line instanceof ItemStack)) {
        continue;
      }

      final Line<?> itemLine = new ItemLine(visiblePlayers, entityId, (ItemStack) line);
      itemLine.setLocation(lineLocation);

      lines.add(itemLine);
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

  public void setAnimation(final Plugin plugin, final int index, final @NotNull Animation animation) {
    getLine(index).setAnimation(plugin, animation);
  }

  public void removeAnimation(final int index) {
    getLine(index).removeAnimation();
  }

  public void show(final @NotNull Player player) {
    visiblePlayers.add(player);
    lines.forEach(line -> line.show(player));
  }

  public void hide(final @NotNull Player player) {
    lines.forEach(line -> line.hide(player));
    visiblePlayers.remove(player);
  }

  public Line<?> getLine(final int index) {
    if (lines.size() < index) {
      throw new HologramException("Index is higher than the hologram size.");
    }

    return lines.get(index);
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
    private int range;

    public Builder() {
      this.lines = new LinkedList<>();
      this.placeholderRegistry = new PlaceholderRegistry();
      this.range = 50;
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
    public Builder range(final int range) {
      this.range = range;
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
        throw new HologramException("No location was given in the builder. Cancelling Hologram creation.");
      }

      final Hologram hologram = new StandardHologram(location,
          placeholderRegistry, new ArrayList<>(), lines, range);

      HologramCore.getApi().registerHologram(hologram);
      return hologram;
    }
  }

}
