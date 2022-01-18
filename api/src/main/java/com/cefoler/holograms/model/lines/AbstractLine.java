package com.cefoler.holograms.model.lines;

import com.cefoler.holograms.exception.HologramException;
import com.cefoler.holograms.model.animation.Animation;
import com.cefoler.holograms.model.animation.type.AnimationType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
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

public abstract class AbstractLine<T> implements Line<T>{

  protected static final int VERSION;
  protected static final ProtocolManager MANAGER;

  static {
    VERSION = MinecraftVersion.getCurrentVersion().getMinor();
    MANAGER = ProtocolLibrary.getProtocolManager();
  }

  protected final int entityID;
  private final Collection<Player> animationPlayers;

  @Setter
  protected Location location;

  @Getter
  @Setter
  protected T line;

  protected Optional<Animation> animation;
  private int taskID = -1;

  private WrappedDataWatcher defaultDataWatcher;

  protected AbstractLine(final @NotNull Collection<Player> seeingPlayers, final int entityID,
      final @NotNull T line) {
    this.entityID = entityID;
    this.line = line;
    this.animationPlayers = seeingPlayers;
    this.animation = Optional.empty();

    if (VERSION < 9) {
      this.defaultDataWatcher = getDefaultWatcher(Bukkit.getWorlds().get(0));
    }
  }

  public void hide(final @NotNull Player player) {
    final PacketContainer destroyEntity = new PacketContainer(
        PacketType.Play.Server.ENTITY_DESTROY);
    try {
      if (VERSION < 9) {
        destroyEntity.getIntegerArrays()
            .write(0, new int[]{entityID});
        MANAGER.sendServerPacket(player, destroyEntity);
        return;
      }

      destroyEntity.getIntLists()
          .write(0, Collections.singletonList(entityID));

      MANAGER.sendServerPacket(player, destroyEntity);
    } catch (Exception exception) {
      throw new HologramException(
          "Unable to hide hologram from Player. Exception: " + exception.getMessage());
    }
  }

  @SuppressWarnings("deprecation")
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

  public void setAnimation(final Plugin plugin, final @NotNull AnimationType animationType) {
    setAnimation(plugin, animationType.getAnimation());
  }

  public void setAnimation(final Plugin plugin, final @NotNull Animation animation) {
    this.animation = Optional.of(animation);
    animation.setEntityId(entityID);

    // Sync update because of entity packet
    final BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin,
        () -> animationPlayers.forEach(animation::nextFrame),
        animation.getDelay(), animation.getDelay());

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
