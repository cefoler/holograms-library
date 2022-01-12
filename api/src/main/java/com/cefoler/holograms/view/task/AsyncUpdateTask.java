package com.cefoler.holograms.view.task;

import com.cefoler.holograms.HologramCore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class AsyncUpdateTask implements Callable<CompletableFuture<Void>> {

  private final HologramCore factory;

  @Override
  public CompletableFuture<Void> call() {
    return CompletableFuture.runAsync(() -> {
      final List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
      playerList.forEach(player -> factory.getHolograms().forEach(hologram -> factory.handle(player, hologram)));
    });
  }

}
