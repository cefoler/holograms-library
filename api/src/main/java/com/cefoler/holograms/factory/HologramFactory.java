package com.cefoler.holograms.factory;

import com.cefoler.holograms.model.hologram.AbstractHologram;
import com.cefoler.holograms.view.task.AsyncUpdateTask;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;

@Getter
public final class HologramFactory {

  private static final ScheduledExecutorService SCHEDULED;

  static {
    SCHEDULED = Executors.newScheduledThreadPool(1);
  }

  private final List<AbstractHologram> holograms;
  private final AsyncUpdateTask asyncUpdateTask;

  public HologramFactory() {
    this.holograms = new ArrayList<>(0);
    this.asyncUpdateTask = new AsyncUpdateTask(this);
  }

  public ScheduledExecutorService getScheduled() {
    return SCHEDULED;
  }

}
