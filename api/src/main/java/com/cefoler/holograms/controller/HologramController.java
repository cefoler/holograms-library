package com.cefoler.holograms.controller;

import com.cefoler.holograms.HologramCore;
import com.cefoler.holograms.model.hologram.Hologram;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
@AllArgsConstructor
public final class HologramController {

  private final HologramCore factory;

  public void register(final @NotNull Hologram hologram) {
    factory.getHolograms().add(hologram);
  }

  public void remove(final @NotNull Hologram hologram) {
    factory.getHolograms().remove(hologram);
  }

  public void startHologramTick() {
    factory.getScheduled().scheduleAtFixedRate(() -> factory.getAsyncUpdateTask().call(), 1000, 100,
        TimeUnit.MILLISECONDS);
  }

  public void handle(final Player player, final Hologram hologram) {
    final Location location = hologram.getLocation();

    final boolean isShown = hologram.isVisible(player);

    final World world = location.getWorld();
    final World playerWorld = player.getWorld();

    if (world != null && isShown) {
      if (!world.getName().equalsIgnoreCase(playerWorld.getName())) {
        hologram.hide(player);
        return;
      }

      if (!world.isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
        hologram.hide(player);
        return;
      }
    }

    final boolean inRange = location.distanceSquared(player.getLocation()) <= 15;
    if (!inRange && isShown) {
      hologram.hide(player);
      return;
    }

    if (inRange && !isShown) {
      hologram.show(player);
    }
  }

}
