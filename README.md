# holograms-library
Hologram Library for Spigot projects with support from 1.8 up to 1.18 using ProtocolLib as dependency.

- Support for hex colors (Only above 1.16)
- Easy to create and use
- Only needs to register one class for all the hologram architecture
- Dependent to ProtocolLib

Note: This is a **experimental** project.


## How to use

### Creating the hologram

```java
    final Hologram hologram = AbstractHologram.builder()
        .addLines("This", "Is", "A", "Test")
        .addLine(new ItemStack(Material.STONE))
        .addPlaceholder("%player%", player -> player.getDisplayName())
        .build();

```

### Other methods

```java
    hologram.hide(player);
    hologram.show(player);

    hologram.setLine(1, "Test");
    hologram.setLine(1, new ItemStack(Material.STONE));

    final boolean visible = hologram.isVisible(player);

    hologram.getVisiblePlayers().forEach(player -> {
      System.out.println("This player can see the hologram: " + player.getName());
    });
```

### Registering the Hologram architecture

This is necessary for the holograms to work. It should be used after onLoad, the recomendation is to use it after you loaded your listeners.

```java
HologramApi.getApi().register(plugin);
```
