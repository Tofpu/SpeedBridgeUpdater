package io.tofpu.speedbridgeupdater;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.speedbridgeupdater.executor.BukkitExecutor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class SpeedBridgeUpdaterPlugin extends JavaPlugin {
    private PterodactylApp pterodactylApp;

    @Override
    public void onEnable() {
        // Plugin startup logic

        CompletableFuture.runAsync(() -> {
            try {
                this.pterodactylApp = new PterodactylApp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, BukkitExecutor.INSTANCE).thenRun(() -> {
            Bukkit.getScheduler().callSyncMethod(this, () -> {
                DynamicClass.addParameters(this);
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
}
