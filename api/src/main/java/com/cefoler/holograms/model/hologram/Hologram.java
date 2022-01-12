package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.animation.type.AnimationType;
import com.cefoler.holograms.model.lines.Line;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Hologram {

  Location getLocation();

  Line<?>[] getLines();

  List<Player> getVisiblePlayers();

  PlaceholderRegistry getPlaceholders();

  void setLine(int index, @NotNull ItemStack itemStack);

  void setLine(int index, @NotNull String text);

  void setAnimation(int index, @NotNull AnimationType animationType);

  void removeAnimation(int index);

  void show(@NotNull Player player);

  void hide(@NotNull Player player);

  Line<?> getLine(int index);

  boolean isVisible(@NotNull Player player);

}
