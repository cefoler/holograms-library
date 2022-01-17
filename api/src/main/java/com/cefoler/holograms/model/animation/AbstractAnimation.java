package com.cefoler.holograms.model.animation;

import com.cefoler.holograms.exception.HologramException;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import java.io.Serializable;
import lombok.Data;
import org.bukkit.entity.Player;

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

}
