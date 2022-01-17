package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.animation.type.AnimationType;
import com.cefoler.holograms.model.lines.Line;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface Hologram {

  Location getLocation();

  List<Line<?>> getLines();

  List<Player> getVisiblePlayers();

  PlaceholderRegistry getPlaceholders();

  void setLine(final int index, final @NotNull ItemStack itemStack);

  void setLine(final int index, final @NotNull String text);

  void setAnimation(final Plugin plugin, final int index, final @NotNull AnimationType animationType);

  void removeAnimation(final int index);

  void show(final @NotNull Player player);

  void hide(final @NotNull Player player);

  Line<?> getLine(final int index);

  boolean isVisible(final @NotNull Player player);

}
