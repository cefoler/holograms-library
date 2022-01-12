package com.cefoler.holograms.model.hologram.impl;

import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.hologram.AbstractHologram;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardHologram extends AbstractHologram {

  public StandardHologram(final @NotNull Plugin plugin,
      final @NotNull Location location,
      final @Nullable PlaceholderRegistry placeholders,
      final @NotNull List<Player> seeingPlayers,
      final @NotNull Object... lines) {
    super(plugin, location, placeholders, seeingPlayers, lines);
  }

}
