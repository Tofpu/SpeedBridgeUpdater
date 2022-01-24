package io.tofpu.speedbridgeupdater.plugin;

import io.tofpu.speedbridgeupdater.SpeedBridgeUpdater;
import org.bukkit.configuration.Configuration;
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
        saveDefaultConfig();

        final Configuration configuration = getConfig();
        // to be used later
        final String serviceMode = configuration.getString("settings.service.mode");

        final String panelUrl = configuration.getString("settings.service.panel_url");
        final String apiKey = configuration.getString("settings.service.api_key");
        final String serverId = configuration.getString("settings.service.server_id");

        this.speedBridgeUpdater.load(serviceMode, panelUrl, apiKey, serverId);
    }

    @Override
    public void onDisable() {
        // Plugin startup logic
        this.speedBridgeUpdater.disable();
    }
}
