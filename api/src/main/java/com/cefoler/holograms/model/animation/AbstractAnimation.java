package com.cefoler.holograms.model.animation;

import com.cefoler.holograms.exception.HologramException;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public abstract class AbstractAnimation implements Animation, Serializable {

  protected ProtocolManager manager;
  protected int entityID;

  public abstract long getDelay();

  public abstract void nextFrame(@NotNull Player player);

  public void sendPacket(final Player player, final PacketContainer container) {
    try {
      manager.sendServerPacket(player, container);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to send packet in Hologram animation. Exception: " + exception.getMessage());
    }
  }

}
