package com.cefoler.holograms.model.animation.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.cefoler.holograms.model.animation.AbstractAnimation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public final class CircleAnimation extends AbstractAnimation {

  private float yaw = 0;

  @Override
  public long delay() {
    return 2L;
  }

  @Override
  public void nextFrame(@NotNull Player player) {
    this.yaw += 10L;
    final PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);

    container.getIntegers().write(0, this.entityID);
    container.getBytes()
        .write(0, (byte) getCompressedAngle(yaw))
        .write(1, (byte) 0);
    container.getBooleans().write(0, true);

    try {
      manager.sendServerPacket(player, container);
    } catch (InvocationTargetException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public AbstractAnimation clone() {
    return new CircleAnimation();
  }

  private int getCompressedAngle(float value) {
    return (int) (value * 256.0F / 360.0F);
  }

}
