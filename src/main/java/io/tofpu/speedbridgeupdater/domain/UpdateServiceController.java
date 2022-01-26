package io.tofpu.speedbridgeupdater.domain;

import io.tofpu.speedbridgeupdater.domain.extend.LocalService;
import io.tofpu.speedbridgeupdater.domain.extend.PteroService;
import io.tofpu.speedbridgeupdater.domain.type.ServiceModeType;
import io.tofpu.speedbridgeupdater.executor.BukkitExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class UpdateServiceController {
    private final @NotNull ServiceModeType serviceModeType;
    private final @NotNull AbstractUpdateService abstractUpdateService;

    public UpdateServiceController(final @NotNull ServiceModeType serviceModeType, final String panelUrl, final String apiKey, final String serverId) {
        this.serviceModeType = serviceModeType;

        if (serviceModeType == ServiceModeType.LOCALLY) {
            this.abstractUpdateService = new LocalService(Bukkit.getServer());
        } else {
            // running the operation async due to the service blocking upon being invoked
            this.abstractUpdateService = new PteroService(panelUrl, apiKey, serverId);
        }
    }

    public void startOperation(final @NotNull CommandSender sender) {
        this.abstractUpdateService.startOperation(sender);
    }

    public boolean hasRequestBeenSent() {
        return this.abstractUpdateService.hasRequestBeenSent();
    }

    public ServiceModeType getServiceModeType() {
        return this.serviceModeType;
    }
}
