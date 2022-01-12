package com.cefoler.holograms.model.hologram;

import com.cefoler.holograms.model.Placeholders;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HologramImpl extends AbstractHologram {

  public HologramImpl(@NotNull final Plugin plugin,
      @NotNull final Location location,
      @Nullable final Placeholders placeholders,
      @NotNull final List<Player> seeingPlayers,
      @NotNull final Object... lines) {
    super(plugin, location, placeholders, seeingPlayers, lines);
  }

}
