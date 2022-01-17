package com.cefoler.holograms.model.lines.impl;

import com.cefoler.holograms.exception.HologramException;
import com.cefoler.holograms.model.lines.AbstractLine;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemLine extends AbstractLine<ItemStack> {

  public ItemLine(final @NotNull Collection<Player> seeingPlayers,
      final int entityID, final @NotNull ItemStack itemStack) {
    super(seeingPlayers, entityID, itemStack);
  }

  @Override
  public void show(final @NotNull Player player) {
    super.show(player);
    try {
      final PacketContainer container = MANAGER.createPacket(
          PacketType.Play.Server.ENTITY_METADATA);
      container.getIntegers()
          .write(0, entityID);

      final WrappedDataWatcher watcher = new WrappedDataWatcher();

      if (VERSION < 9) {
        watcher.setObject(0, (byte) 0x20);
      } else {
        final WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(
            0, WrappedDataWatcher.Registry.get(Byte.class));
        watcher.setObject(visible, (byte) 0x20);
      }

      container.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
      MANAGER.sendServerPacket(player, container);

      update(player);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to show hologram to player. Exception: " + exception.getMessage());
    }
  }

  @Override
  public void update(final @NotNull Player player) {
    try {
      final PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
      packet.getIntegers()
          .write(0, entityID);

      if (VERSION > 9) {
        final List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, line));

        packet.getSlotStackPairLists().write(0, pairList);
        MANAGER.sendServerPacket(player, packet);
        return;
      }

      packet.getIntegers()
          .write(1, 4);
      packet.getItemModifier()
          .write(0, line);

      MANAGER.sendServerPacket(player, packet);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to update hologram to player. Exception: " + exception.getMessage());
    }
  }

}
