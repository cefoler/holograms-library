package com.cefoler.holograms.view.task;

import com.cefoler.holograms.factory.HologramFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@AllArgsConstructor
public final class AsyncUpdateTask implements Callable<CompletableFuture<Void>> {

  private final HologramFactory factory;

  @Override
  public CompletableFuture<Void> call() {
    return CompletableFuture.runAsync(() -> Bukkit.getOnlinePlayers().forEach(
        player -> factory.getHolograms().forEach(abstractHologram -> {
          final Location hologramLocation = abstractHologram.getLocation();
          final Location playerLocation = player.getLocation();

          final boolean isShown = abstractHologram.isShownFor(player);

          final World hologramWorld = hologramLocation.getWorld();
          final World playerWorld = playerLocation.getWorld();

          if (hologramWorld != null && !hologramWorld.getName()
              .equalsIgnoreCase(playerWorld.getName()) && isShown) {
            abstractHologram.hide(player);
            return;
          }

          if (hologramWorld != null && !hologramWorld.isChunkLoaded(hologramLocation.getBlockX() >> 4, hologramLocation.getBlockZ() >> 4) && isShown) {
            abstractHologram.hide(player);
            return;
          }

          // TODO: see how to optimize the distance squared method
          final boolean inRange = hologramLocation.distanceSquared(playerLocation) <= 5;
          if (!inRange && isShown) {
            abstractHologram.hide(player);
            return;
          }

          if (inRange && !isShown) {
            abstractHologram.show(player);
          }
        })));
  }

}
