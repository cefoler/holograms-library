package com.cefoler.holograms.model.lines.impl;

import com.cefoler.holograms.exception.HologramException;
import com.cefoler.holograms.model.PlaceholderRegistry;
import com.cefoler.holograms.model.lines.AbstractLine;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class TextLine extends AbstractLine<String> {

  private final PlaceholderRegistry placeholders;

  public TextLine(final @NotNull Collection<Player> seeingPlayers, final int hologramId,
      final @NotNull String obj, final @NotNull PlaceholderRegistry placeholderRegistry) {
    super(seeingPlayers, hologramId, obj);
    this.placeholders = placeholderRegistry;
  }

  @Override
  public void show(final @NotNull Player player) {
    super.show(player);
    try {
      final PacketContainer packet = MANAGER.createPacket(PacketType.Play.Server.ENTITY_METADATA);
      packet.getIntegers()
          .write(0, entityID);

      final WrappedDataWatcher watcher = new WrappedDataWatcher();

      if (VERSION < 9) {
        watcher.setObject(0, (byte) 0x20);
        watcher.setObject(2, placeholders.parse(line, player));
        watcher.setObject(3, (byte) 1);

        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        MANAGER.sendServerPacket(player, packet);
        return;
      }

      final WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(
          0, WrappedDataWatcher.Registry.get(Byte.class));
      watcher.setObject(visible, (byte) 0x20);

      final WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(
          3, WrappedDataWatcher.Registry.get(Boolean.class));
      watcher.setObject(nameVisible, true);

      packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
      MANAGER.sendServerPacket(player, packet);

      update(player);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to show hologram to player. Exception: " + exception.getMessage());
    }
  }

  @Override
  public void update(final @NotNull Player player) {
    try {
      final PacketContainer packet = MANAGER.createPacket(PacketType.Play.Server.ENTITY_METADATA);
      packet.getIntegers().write(0, entityID);

      final WrappedDataWatcher watcher = new WrappedDataWatcher();

      if (VERSION < 9) {
        watcher.setObject(2, placeholders.parse(line, player));

        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        MANAGER.sendServerPacket(player, packet);
        return;
      }

      final WrappedChatComponent chatComponent = WrappedChatComponent.fromChatMessage(
          placeholders.parse(line, player))[0];

      final WrappedDataWatcher.WrappedDataWatcherObject chatComponentSerializer = new WrappedDataWatcher.WrappedDataWatcherObject(
          2, WrappedDataWatcher.Registry.getChatComponentSerializer(true));

      watcher.setObject(chatComponentSerializer, chatComponent.getHandle());

      packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
      MANAGER.sendServerPacket(player, packet);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to update hologram to player. Exception: " + exception.getMessage());
    }
  }

}
