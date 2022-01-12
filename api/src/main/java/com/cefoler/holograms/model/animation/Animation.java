package com.cefoler.holograms.model.animation;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Animation {

  long getDelay();

  void nextFrame(@NotNull Player player);

}
