package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.model.Placeholders;
import com.cefoler.holograms.model.animation.type.AnimationType;
import com.cefoler.holograms.model.lines.AbstractLine;
import com.cefoler.holograms.model.lines.impl.ItemLine;
import com.cefoler.holograms.model.lines.impl.TextLine;
import com.cefoler.holograms.view.listener.HologramListener;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;
import java.util.function.Function;

@Getter
public abstract class AbstractHologram implements Hologram {

  private static final ThreadLocalRandom RANDOM;

  static {
    RANDOM = ThreadLocalRandom.current();
  }

  private final Plugin plugin;
  private final Location location;

  protected final AbstractLine<?>[] lines;
  private final List<Player> visibleTo;

  private final Placeholders placeholders;

  public AbstractHologram(
      final @NotNull Plugin plugin,
      final @NotNull Location location,
      final @Nullable Placeholders placeholders,
      final @NotNull List<Player> visibleTo,
      final @NotNull Object... lines
  ) {
    this.plugin = plugin;
    this.location = location;
    this.placeholders = placeholders == null ? new Placeholders() : placeholders;
    this.visibleTo = visibleTo;
    this.lines = new AbstractLine[lines.length];

    final Location cloned = location.clone().subtract(0, 0.28, 0);

    int count = 0;
    for (final Object line : lines) {
      final double up = line instanceof ItemStack
        ? 0.0D
        : 0.28D;

      if (line instanceof String) {
        final AbstractLine<?> tempLine = new TextLine(visibleTo, plugin, RANDOM.nextInt(), (String) line, placeholders);
        tempLine.setLocation(cloned.add(0.0, up, 0).clone());

        this.lines[count++] = tempLine;
        return;
      }

      if (!(line instanceof ItemStack)) {
        return;
      }

      final AbstractLine<?> tempLine = new ItemLine(visibleTo, plugin, RANDOM.nextInt(), (ItemStack) line);
      tempLine.setLocation(cloned.add(0.0, 0.60D, 0).clone());

      this.lines[count++] = tempLine;
    }
  }

  public void setLine(final int index, final @NotNull ItemStack itemStack) {
    final AbstractLine<ItemStack> line = (ItemLine) getLine(index);
    line.setLine(itemStack);

    visibleTo.forEach(line::update);
  }

  public void setLine(final int index, final @NotNull String text) {
    final AbstractLine<String> line = (TextLine) getLine(index);
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
    for (final AbstractLine<?> line : lines) {
      line.show(player);
    }
  }

  public void hide(final @NotNull Player player) {
    for (final AbstractLine<?> line : lines) {
      line.hide(player);
    }

    visibleTo.remove(player);
  }

  public AbstractLine<?> getLine(final int index) {
    return lines[Math.abs(index - lines.length + 1)];
  }

  public boolean isShownFor(final @NotNull Player player) {
    return visibleTo.contains(player);
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Location location;

    private final ConcurrentLinkedDeque<Object> lines;
    private final Placeholders placeholders;

    {
      lines = new ConcurrentLinkedDeque<>();
      placeholders = new Placeholders();
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
    public Builder addPlaceholder(final @NotNull String key, final @NotNull Function<Player, String> result) {
      this.placeholders.add(key, result);
      return this;
    }

    @NotNull
    public AbstractHologram build(final @NotNull HologramListener pool) {
      if (location == null || lines.isEmpty()) {
        throw new IllegalArgumentException("No location given or not completed");
      }

      final AbstractHologram hologram = new HologramImpl(pool.getPlugin(), location,
          placeholders, new ArrayList<>(), lines.toArray());

      pool.takeCareOf(hologram);
      return hologram;
    }
  }

}
