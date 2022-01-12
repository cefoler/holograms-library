package com.cefoler.holograms.controller;

import com.cefoler.holograms.factory.HologramFactory;
import com.cefoler.holograms.model.hologram.AbstractHologram;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class HologramController {

  private final HologramFactory factory;

  public void takeCareOf(final @NotNull AbstractHologram hologram) {
    factory.getHolograms().add(hologram);
  }

  public void startHologramTick() {
    factory.getScheduled().scheduleAtFixedRate(() -> factory.getAsyncUpdateTask().call(), 1000, 100, TimeUnit.MILLISECONDS);
  }

}
