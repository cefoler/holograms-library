package com.cefoler.holograms.model.animation.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.cefoler.holograms.model.animation.AbstractAnimation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CircleAnimation extends AbstractAnimation {

  private float yaw = 0;

  @Override
  public long getDelay() {
    return 2;
  }

  @Override
  public void nextFrame(@NotNull Player player) {
    this.yaw += 10L;
    final PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);

    container.getIntegers()
        .write(0, entityID);

    container.getBytes()
        .write(0, (byte) getCompressedAngle(yaw))
        .write(1, (byte) 0);

    container.getBooleans()
        .write(0, true);

    sendPacket(player, container);
  }

  private int getCompressedAngle(final float value) {
    return (int) (value * 256.0F / 360.0F);
  }

}
