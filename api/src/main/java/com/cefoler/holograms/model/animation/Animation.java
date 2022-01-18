package com.cefoler.holograms.model.animation;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Animation {

  void setEntityId(final int entityID);

  /**
   * Delay in ticks of the
   * animation task.
   *
   * @return Long
   */
  long getDelay();

  void nextFrame(final @NotNull Player player);

}
