package com.cefoler.holograms.model.animation;

import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public abstract class AbstractAnimation {

    protected ProtocolManager manager;
    protected int entityID;

    public abstract long delay();

    public abstract void nextFrame(@NotNull Player player);

    public abstract AbstractAnimation clone();

}
