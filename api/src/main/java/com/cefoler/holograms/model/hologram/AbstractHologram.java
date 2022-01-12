package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.exception.HologramException;
import com.cefoler.holograms.factory.HologramFactory;
import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.animation.type.AnimationType;
import com.cefoler.holograms.model.hologram.impl.StandardHologram;
import com.cefoler.holograms.model.lines.AbstractLine;
import com.cefoler.holograms.model.lines.Line;
import com.cefoler.holograms.model.lines.impl.ItemLine;
import com.cefoler.holograms.model.lines.impl.TextLine;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class AbstractHologram implements Hologram, Serializable {

  private static final ThreadLocalRandom RANDOM;

  static {
    RANDOM = ThreadLocalRandom.current();
  }

  protected final Line<?>[] lines;
  private final Location location;
  private final List<Player> visibleTo;

  private final PlaceholderRegistry placeholders;

  public AbstractHologram(
      final @NotNull Plugin plugin,
      final @NotNull Location location,
      final @Nullable PlaceholderRegistry placeholderRegistry,
      final @NotNull List<Player> visibleTo,
      final @NotNull Object... lines
  ) {
    this.location = location;
    this.placeholders = placeholderRegistry
        == null ? new PlaceholderRegistry() : placeholderRegistry;
    this.visibleTo = visibleTo;
    this.lines = new AbstractLine[lines.length];

    final Location cloned = location.clone().subtract(0, 0.28, 0);

    int count = 0;
    for (final Object line : lines) {
      final double up = line instanceof ItemStack
          ? 0.0D
          : 0.28D;

      if (line instanceof String) {
        final Line<?> tempLine = new TextLine(visibleTo, plugin, RANDOM.nextInt(), (String) line,
            this.placeholders);
        tempLine.setLocation(cloned.add(0.0, up, 0).clone());

        this.lines[count++] = tempLine;
        return;
      }

      if (!(line instanceof ItemStack)) {
        return;
      }

      final Line<?> tempLine = new ItemLine(visibleTo, plugin, RANDOM.nextInt(), (ItemStack) line);
      tempLine.setLocation(cloned.add(0.0, 0.60D, 0).clone());

      this.lines[count++] = tempLine;
    }
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  public void setLine(final int index, final @NotNull ItemStack itemStack) {
    final Line<ItemStack> line = (ItemLine) getLine(index);
    line.setLine(itemStack);

    visibleTo.forEach(line::update);
  }

  public void setLine(final int index, final @NotNull String text) {
    final Line<String> line = (TextLine) getLine(index);
    line.setLine(text);

    visibleTo.forEach(line::update);
  }

  public void setAnimation(final int index, final @NotNull AnimationType animationType) {
    getLine(index).setAnimation(animationType);
  }

  public void removeAnimation(final int index) {
    getLine(index).removeAnimation();
  }

  public void show(final @NotNull Player player) {
    visibleTo.add(player);
    for (final Line<?> line : lines) {
      line.show(player);
    }
  }

  public void hide(final @NotNull Player player) {
    for (final Line<?> line : lines) {
      line.hide(player);
    }

    visibleTo.remove(player);
  }

  public Line<?> getLine(final int index) {
    return lines[Math.abs(index - lines.length + 1)];
  }

  public boolean isShownFor(final @NotNull Player player) {
    return visibleTo.contains(player);
  }

  public static class Builder {

    private final ConcurrentLinkedDeque<Object> lines;
    private final PlaceholderRegistry placeholderRegistry;
    private Location location;

    {
      lines = new ConcurrentLinkedDeque<>();
      placeholderRegistry = new PlaceholderRegistry();
    }

    @NotNull
    public Builder addLine(final @NotNull String line) {
      lines.addFirst(line);
      return this;
    }

    @NotNull
    public Builder addLines(final @NotNull String... linesString) {
      for (final String s : linesString) {
        lines.addFirst(s);
      }

      return this;
    }

    @NotNull
    public Builder addLine(final @NotNull ItemStack item) {
      lines.addFirst(item);
      return this;
    }

    @NotNull
    public Builder addLines(final @NotNull ItemStack... items) {
      for (final ItemStack item : items) {
        lines.addFirst(item);
      }

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
    public Hologram build(final Plugin plugin) {
      if (location == null || lines.isEmpty()) {
        throw new HologramException("No location given or not completed");
      }

      final Hologram hologram = new StandardHologram(plugin, location,
          placeholderRegistry, new ArrayList<>(), lines.toArray());

      HologramFactory.getFactory().getController().register(hologram);
      return hologram;
    }
  }

}
