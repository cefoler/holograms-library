package com.cefoler.holograms.model.animation.impl;

import com.cefoler.holograms.model.animation.AbstractAnimation;
import com.cefoler.holograms.model.hologram.Hologram;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class DefaultAnimation extends AbstractAnimation {

  private float defaultYaw;
  private final float yaw;

  private final int delay;

  private final Runnable runnable;

  @Override
  public void nextFrame(final @NotNull Player player) {
    defaultYaw = defaultYaw + yaw;
    final PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);

    container.getIntegers()
        .write(0, entityId);

    container.getBytes()
        .write(0, (byte) getCompressedAngle(yaw))
        .write(1, (byte) 0);

    container.getBooleans()
        .write(0, true);

    sendPacket(player, container);

    if (runnable != null) {
      runnable.run();
    }
  }

  @Override
  public long getDelay() {
    return delay;
  }

  private int getCompressedAngle(final float value) {
    return (int) (value * 256.0F / 360.0F);
  }

}
