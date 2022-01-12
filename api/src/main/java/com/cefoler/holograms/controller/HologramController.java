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
    final Location hologramLocation = hologram.getLocation();
    final Location playerLocation = player.getLocation();

    final boolean isShown = hologram.isVisible(player);

    final World hologramWorld = hologramLocation.getWorld();
    final World playerWorld = player.getWorld();

    if (hologramWorld != null && !hologramWorld.getName()
        .equalsIgnoreCase(playerWorld.getName()) && isShown) {
      hologram.hide(player);
      return;
    }

    if (hologramWorld != null && !hologramWorld.isChunkLoaded(
        hologramLocation.getBlockX() >> 4, hologramLocation.getBlockZ() >> 4) && isShown) {
      hologram.hide(player);
      return;
    }

    // TODO: see how to optimize the distance squared method
    final boolean inRange = hologramLocation.distanceSquared(playerLocation) <= 5;
    if (!inRange && isShown) {
      hologram.hide(player);
      return;
    }

    if (inRange && !isShown) {
      hologram.show(player);
    }
  }

}
