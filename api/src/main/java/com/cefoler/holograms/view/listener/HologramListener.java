package com.cefoler.holograms.view.listener;

import com.cefoler.holograms.factory.HologramFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class HologramListener implements Listener {

  private static boolean REGISTERED;

  static {
    REGISTERED = false;
  }

  private final HologramFactory factory;

  public HologramListener(final @NotNull Plugin plugin, final HologramFactory hologramFactory) {
    this.factory = hologramFactory;

    if (!REGISTERED) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
      REGISTERED = true;
    }
  }

  @EventHandler
  public void onRespawn(final PlayerRespawnEvent event) {
    final Player player = event.getPlayer();
    factory.getHolograms().stream()
        .filter(hologram -> hologram.isShownFor(player))
        .forEach(hologram -> hologram.hide(player));
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    final Player player = event.getPlayer();
    factory.getHolograms().stream()
        .filter(hologram -> hologram.isShownFor(player))
        .forEach(hologram -> hologram.hide(player));
  }

}
