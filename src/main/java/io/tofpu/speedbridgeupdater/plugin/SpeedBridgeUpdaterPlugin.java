package io.tofpu.speedbridgeupdater.plugin;

import io.tofpu.speedbridgeupdater.SpeedBridgeUpdater;
import io.tofpu.speedbridgeupdater.domain.type.ServiceModeType;
import io.tofpu.speedbridgeupdater.exception.InvalidServiceModeException;
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
        final ServiceModeType serviceModeType = ServiceModeType.match(serviceMode);

        if (serviceModeType == null) {
            try {
                throw new InvalidServiceModeException(serviceMode);
            } catch (InvalidServiceModeException e) {
                throw new IllegalStateException(e);
            }
        }

        final String panelUrl = configuration.getString("settings.service.panel_url");
        final String apiKey = configuration.getString("settings.service.api_key");
        final String serverId = configuration.getString("settings.service.server_id");

        this.speedBridgeUpdater.load(serviceModeType, panelUrl, apiKey, serverId);
    }

    @Override
    public void onDisable() {
        // Plugin startup logic
        this.speedBridgeUpdater.disable();
    }
}
