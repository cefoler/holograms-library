package com.cefoler.holograms.model.lines;

import com.cefoler.holograms.model.animation.Animation;
import com.cefoler.holograms.model.animation.type.AnimationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface Line<T> {

  T getLine();

  void setLine(final T line);

  void setLocation(final Location location);

  void update(final @NotNull Player player);

  void hide(final @NotNull Player player);

  void show(final @NotNull Player player);

  void setAnimation(final Plugin plugin, final @NotNull AnimationType animationType);

  void setAnimation(final Plugin plugin, final @NotNull Animation animation);

  void removeAnimation();

  boolean hasAnimation();

}
