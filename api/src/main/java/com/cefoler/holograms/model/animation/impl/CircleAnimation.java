package com.cefoler.holograms.model.animation.impl;

import com.cefoler.holograms.model.animation.AbstractAnimation;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CircleAnimation extends AbstractAnimation {

  private float yaw;

  @Override
  public void nextFrame(final @NotNull Player player) {
    this.yaw += 10L;
    final PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);

    container.getIntegers()
        .write(0, entityId);

    container.getBytes()
        .write(0, (byte) getCompressedAngle(yaw))
        .write(1, (byte) 0);

    container.getBooleans()
        .write(0, true);

    sendPacket(player, container);
  }

  @Override
  public long getDelay() {
    return 2;
  }

  private int getCompressedAngle(final float value) {
    return (int) (value * 256.0F / 360.0F);
  }

}
