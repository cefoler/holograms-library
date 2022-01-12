package com.cefoler.holograms.model.animation;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Animation {

  void setEntityId(final int entityID);

  long getDelay();

  void nextFrame(final @NotNull Player player);

}
