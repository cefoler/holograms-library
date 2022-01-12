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

  public StandardHologram(@NotNull final Plugin plugin,
      @NotNull final Location location,
      @Nullable final PlaceholderRegistry placeholders,
      @NotNull final List<Player> seeingPlayers,
      @NotNull final Object... lines) {
    super(plugin, location, placeholders, seeingPlayers, lines);
  }

}
