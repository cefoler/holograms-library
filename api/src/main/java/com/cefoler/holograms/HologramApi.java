package com.cefoler.holograms;

import com.cefoler.holograms.model.hologram.Hologram;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface HologramApi {

  void register(final Plugin plugin);

  void registerHologram(final @NotNull Hologram hologram);

  void removeHologram(final @NotNull Hologram hologram);

}
