package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.animation.Animation;
import com.cefoler.holograms.model.animation.type.AnimationType;
import com.cefoler.holograms.model.lines.Line;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * The hologram interface has the
 * required and needed methods to handle
 * with the Hologram created.
 */
public interface Hologram {

  /**
   * Sets the range of the hologram.
   *
   * The range is the distance of
   * blocks from the player to the
   * hologram. If the range is exceeded,
   * the holograms no longer show to the player.
   *
   * @param range Integer
   */
  void setRange(final int range);

  /**
   * Gets the range of the hologram.
   *
   * The range is the distance of
   * blocks from the player to the
   * hologram. If the range is exceeded,
   * the holograms no longer show to the player.
   */
  int getRange();

  /**
   * @return location of the Hologram
   */
  Location getLocation();

  /**
   * Returns the sorted list of lines
   * @return List
   */
  List<Line<?>> getLines();

  List<Player> getVisiblePlayers();

  PlaceholderRegistry getPlaceholders();

  void setLine(final int index, final @NotNull ItemStack itemStack);

  void setLine(final int index, final @NotNull String text);

  void setAnimation(final Plugin plugin, final int index, final @NotNull AnimationType animationType);

  void setAnimation(final Plugin plugin, final int index, final @NotNull Animation animation);

  void removeAnimation(final int index);

  void show(final @NotNull Player player);

  void hide(final @NotNull Player player);

  Line<?> getLine(final int index);

  boolean isVisible(final @NotNull Player player);

}
