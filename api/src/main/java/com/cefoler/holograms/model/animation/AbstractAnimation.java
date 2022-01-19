package com.cefoler.holograms.model.animation;

import com.cefoler.holograms.exception.HologramException;
import com.cefoler.holograms.model.animation.impl.DefaultAnimation;
import com.cefoler.holograms.model.hologram.AbstractHologram;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import java.io.Serializable;
import lombok.Data;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Data
public abstract class AbstractAnimation implements Animation, Serializable {

  protected static final ProtocolManager MANAGER;

  static {
    MANAGER = ProtocolLibrary.getProtocolManager();
  }

  protected int entityId;

  public void sendPacket(final Player player, final PacketContainer container) {
    try {
      MANAGER.sendServerPacket(player, container);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to send packet in Hologram animation. Exception: " + exception.getMessage());
    }
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private float yaw;
    private float defaultYaw;

    private int delay;

    private Runnable runnable;

    public Builder() {
      this.defaultYaw = 0;
      this.yaw = 0;
      this.delay = 20;
    }

    @NotNull
    public Builder yawRate(final float yawRate) {
      this.yaw = yawRate;
      return this;
    }

    @NotNull
    public Builder defaultYaw(final float defaultYaw) {
      this.defaultYaw = defaultYaw;
      return this;
    }

    @NotNull
    public Builder delay(final int delay) {
      this.delay = delay;
      return this;
    }

    @NotNull
    public Builder runnable(final Runnable runnable) {
      this.runnable = runnable;
      return this;
    }

    @NotNull
    public Animation build() {
      return new DefaultAnimation(yaw, defaultYaw, delay, runnable);
    }

  }

}
