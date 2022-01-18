package com.cefoler.holograms;

import com.cefoler.holograms.model.hologram.Hologram;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface HologramApi {

  /**
   * Registers all the functions
   * needed for the API to work.
   *
   * This should only be used ONCE
   * in a server.
   *
   * @param plugin Plugin
   */
  void register(final Plugin plugin);

  /**
   * Registers a hologram into the registry.
   * @param hologram Hologram
   */
  void registerHologram(final @NotNull Hologram hologram);

  /**
   * Removes a hologram from the registry.
   * @param hologram Hologram
   */
  void removeHologram(final @NotNull Hologram hologram);

}
