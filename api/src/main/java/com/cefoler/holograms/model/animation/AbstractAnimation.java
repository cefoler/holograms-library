package com.cefoler.holograms.model.animation;

import com.cefoler.holograms.exception.HologramException;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAnimation implements Animation, Serializable {

  protected static final ProtocolManager MANAGER;

  static {
    MANAGER = ProtocolLibrary.getProtocolManager();
  }

  @Getter
  @Setter
  protected int entityId;

  public abstract long getDelay();

  public abstract void nextFrame(final @NotNull Player player);

  public void sendPacket(final Player player, final PacketContainer container) {
    try {
      MANAGER.sendServerPacket(player, container);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to send packet in Hologram animation. Exception: " + exception.getMessage());
    }
  }

}
