package io.tofpu.speedbridgeupdater;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.speedbridgeupdater.domain.UpdateServiceController;
import io.tofpu.speedbridgeupdater.domain.type.ServiceModeType;
import io.tofpu.speedbridgeupdater.exception.InvalidPanelURLException;
import io.tofpu.speedbridgeupdater.executor.BukkitExecutor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SpeedBridgeUpdater {
    private final @NotNull JavaPlugin plugin;
    private UpdateServiceController serviceController;

    public SpeedBridgeUpdater(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load(final ServiceModeType serviceModeType, final String panelUrl, final String apiKey, final String serverId) {
        if (serviceModeType == ServiceModeType.PTERODACTYL && !panelUrl.contains("http")) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            try {
                throw new InvalidPanelURLException(panelUrl);
            } catch (final InvalidPanelURLException invalidPanelURLException) {
                throw new IllegalStateException(invalidPanelURLException);
            }
        }

        this.serviceController = new UpdateServiceController(serviceModeType, panelUrl, apiKey, serverId);

        DynamicClass.addParameters(plugin);
        DynamicClass.addParameters(serviceController);
        try {
            DynamicClass.alternativeScan(getClass().getClassLoader(), "io.tofpu" + ".speedbridgeupdater");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        BukkitExecutor.INSTANCE.shutdown();
    }
}
