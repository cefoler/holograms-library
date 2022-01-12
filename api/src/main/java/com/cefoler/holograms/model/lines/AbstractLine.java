package com.cefoler.holograms.model.lines;

import com.cefoler.holograms.exception.HologramException;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.cefoler.holograms.model.animation.AbstractAnimation;
import com.cefoler.holograms.model.animation.type.AnimationType;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractLine<T> {

  protected static final int VERSION;
  protected static final ProtocolManager MANAGER;

  static {
    VERSION = MinecraftVersion.getCurrentVersion().getMinor();
    MANAGER = ProtocolLibrary.getProtocolManager();
  }

  private final Plugin plugin;
  protected final int entityID;

  @Setter
  protected Location location;

  @Setter
  protected T line;

  protected Optional<AbstractAnimation> animation = Optional.empty();

  private final Collection<Player> animationPlayers;
  private int taskID = -1;

  private WrappedDataWatcher defaultDataWatcher;

  public AbstractLine(final @NotNull Collection<Player> seeingPlayers, final @NotNull Plugin plugin,
      final int entityID, final @NotNull T line) {
    this.plugin = plugin;
    this.entityID = entityID;
    this.line = line;
    this.animationPlayers = seeingPlayers; //copy rif

    if (VERSION < 9) {
      this.defaultDataWatcher = getDefaultWatcher(Bukkit.getWorlds().get(0));
    }
  }

  public abstract void update(final @NotNull Player player);

  public void hide(final @NotNull Player player) {
    final PacketContainer destroyEntity = new PacketContainer(
        PacketType.Play.Server.ENTITY_DESTROY);
    try {
      if (VERSION < 9) {
        destroyEntity.getIntegerArrays().write(0, new int[]{entityID});
        MANAGER.sendServerPacket(player, destroyEntity);
      }

      destroyEntity.getIntLists().write(0, Collections.singletonList(entityID));
      MANAGER.sendServerPacket(player, destroyEntity);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to hide hologram from Player. Exception: " + exception.getMessage());
    }
  }

  public void show(final @NotNull Player player) {
    try {
      final PacketContainer itemPacket = MANAGER.createPacket(
          PacketType.Play.Server.SPAWN_ENTITY_LIVING);

      if (VERSION < 9) {
        itemPacket.getIntegers().
            write(0, entityID).
            write(1, (int) EntityType.ARMOR_STAND.getTypeId()).
            write(2, (int) (location.getX() * 32)).
            write(3, (int) (location.getY() * 32)).
            write(4, (int) (location.getZ() * 32));
        itemPacket.getDataWatcherModifier().
            write(0, defaultDataWatcher);

        MANAGER.sendServerPacket(player, itemPacket);
        return;
      }

      itemPacket.getIntegers()
          .write(0, entityID)
          .write(1, 1) // entity type
          .write(2, 1); // extra data

      itemPacket.getUUIDs()
          .write(0, UUID.randomUUID());

      itemPacket.getDoubles()
          .write(0, location.getX())
          .write(1, location.getY())
          .write(2, location.getZ());

      MANAGER.sendServerPacket(player, itemPacket);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to show hologram to player. Exception: " + exception.getMessage());
    }
  }

  public void setAnimation(final @NotNull AnimationType animationType) {
    final AbstractAnimation abstractAnimation = animationType.cloned();
    this.animation = Optional.of(abstractAnimation);

    abstractAnimation.setEntityID(entityID);
    abstractAnimation.setManager(MANAGER);

    final BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin,
        () -> this.animationPlayers.forEach(abstractAnimation::nextFrame),
        abstractAnimation.delay(), abstractAnimation.delay());
    this.taskID = task.getTaskId();
  }

  public void removeAnimation() {
    if (taskID != -1) {
      Bukkit.getScheduler().cancelTask(taskID);
      taskID = -1;
    }
  }

  private WrappedDataWatcher getDefaultWatcher(final @NotNull World world) {
    final Entity entity = world.spawnEntity(new Location(world, 0, 256, 0), EntityType.ARMOR_STAND);
    final WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();

    entity.remove();
    return watcher;
  }

}
