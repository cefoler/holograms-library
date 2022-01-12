package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.model.animation.type.AnimationType;
import com.cefoler.holograms.model.lines.AbstractLine;
import com.cefoler.holograms.model.PlaceholderRegistry;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Hologram {

  Location getLocation();

  AbstractLine<?>[] getLines();

  List<Player> getVisibleTo();

  PlaceholderRegistry getPlaceholders();

  void setLine(int index, @NotNull ItemStack itemStack);

  void setLine(int index, @NotNull String text);

  void setAnimation(int index, @NotNull AnimationType animationType);

  void removeAnimation(int index);

  void show(@NotNull Player player);

  void hide(@NotNull Player player);

  AbstractLine<?> getLine(int index);

  boolean isShownFor(@NotNull Player player);

}
