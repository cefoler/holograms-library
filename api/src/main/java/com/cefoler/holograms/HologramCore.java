package com.cefoler.holograms;

import com.cefoler.holograms.controller.HologramController;
import com.cefoler.holograms.model.hologram.Hologram;
import com.cefoler.holograms.view.listener.HologramListener;
import com.cefoler.holograms.view.task.AsyncUpdateTask;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Getter(AccessLevel.PUBLIC)
public final class HologramCore implements HologramApi {

  private static final HologramApi API;
  private static final ScheduledExecutorService SCHEDULED;

  static {
    SCHEDULED = Executors.newScheduledThreadPool(1);
    API = new HologramCore();
  }

  private final List<Hologram> holograms;
  private final AsyncUpdateTask asyncUpdateTask;

  @Getter(AccessLevel.PRIVATE)
  private final HologramController controller;

  public HologramCore() {
    this.holograms = new ArrayList<>(0);
    this.asyncUpdateTask = new AsyncUpdateTask(this);

    this.controller = new HologramController(this);
  }

  @Override
  public void register(final @NotNull Plugin plugin) {
    new HologramListener(plugin, this);
    controller.startHologramTick();
  }

  @Override
  public void registerHologram(final @NotNull Hologram hologram) {
    controller.register(hologram);
  }

  @Override
  public void removeHologram(final @NotNull Hologram hologram) {
    controller.remove(hologram);
  }

  public void handle(final @NotNull Player player, final @NotNull Hologram hologram) {
    controller.handle(player, hologram);
  }

  public ScheduledExecutorService getScheduled() {
    return SCHEDULED;
  }

  public static HologramApi getApi() {
    return API;
  }

}
