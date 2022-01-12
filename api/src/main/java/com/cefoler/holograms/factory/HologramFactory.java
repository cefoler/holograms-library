package com.cefoler.holograms.factory;

import com.cefoler.holograms.controller.HologramController;
import com.cefoler.holograms.model.hologram.AbstractHologram;
import com.cefoler.holograms.view.listener.HologramListener;
import com.cefoler.holograms.view.task.AsyncUpdateTask;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public final class HologramFactory {

  private static final HologramFactory FACTORY;
  private static final ScheduledExecutorService SCHEDULED;

  static {
    SCHEDULED = Executors.newScheduledThreadPool(1);
    FACTORY = new HologramFactory();
  }

  private final List<AbstractHologram> holograms;
  private final AsyncUpdateTask asyncUpdateTask;

  private final HologramController controller;

  public HologramFactory() {
    this.holograms = new ArrayList<>(0);
    this.asyncUpdateTask = new AsyncUpdateTask(this);

    this.controller = new HologramController(this);
  }

  public void register(final Plugin plugin) {
    new HologramListener(plugin, this);
  }

  public ScheduledExecutorService getScheduled() {
    return SCHEDULED;
  }

  public static HologramFactory getFactory() {
    return FACTORY;
  }

}
