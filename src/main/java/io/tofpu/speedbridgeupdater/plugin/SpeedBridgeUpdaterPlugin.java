package io.tofpu.speedbridgeupdater.plugin;

import io.tofpu.speedbridgeupdater.SpeedBridgeUpdater;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SpeedBridgeUpdaterPlugin extends JavaPlugin {
    private final @NotNull SpeedBridgeUpdater speedBridgeUpdater;

    public SpeedBridgeUpdaterPlugin() {
        this.speedBridgeUpdater = new SpeedBridgeUpdater(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.speedBridgeUpdater.load();
    }

    @Override
    public void onDisable() {
        // Plugin startup logic
        this.speedBridgeUpdater.disable();
    }
}
