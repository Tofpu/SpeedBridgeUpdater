package io.tofpu.speedbridgeupdater;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.speedbridgeupdater.exception.InvalidPanelURL;
import io.tofpu.speedbridgeupdater.executor.BukkitExecutor;
import io.tofpu.speedbridgeupdater.ptero.PterodactylApp;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SpeedBridgeUpdater {
    private final @NotNull JavaPlugin plugin;
    private PterodactylApp pterodactylApp;

    public SpeedBridgeUpdater(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load(final String serviceMode, final String panelUrl, final String apiKey, final String serverId) {
        if (!panelUrl.contains("http")) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            try {
                throw new InvalidPanelURL();
            } catch (final InvalidPanelURL invalidPanelURL) {
                throw new IllegalStateException(invalidPanelURL);
            }
        }

        BukkitExecutor.INSTANCE.submit(() -> {
            try {
                this.pterodactylApp = new PterodactylApp(panelUrl, apiKey, serverId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {
            Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                DynamicClass.addParameters(plugin);
                DynamicClass.addParameters(pterodactylApp);
                try {
                    DynamicClass.alternativeScan(getClass().getClassLoader(), "io.tofpu" + ".speedbridgeupdater");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            });
        });
    }

    public void disable() {
        BukkitExecutor.INSTANCE.shutdown();
    }
}
