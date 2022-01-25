package io.tofpu.speedbridgeupdater.domain.extend;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Directory;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.managers.UploadFileAction;
import io.tofpu.speedbridgeupdater.domain.AbstractUpdateService;
import io.tofpu.speedbridgeupdater.executor.BukkitExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public final class PteroService extends AbstractUpdateService {
    private final @NotNull PteroClient pteroClient;
    private final @NotNull ClientServer clientServer;
    private final @NotNull Directory pluginsDirectory;
    private final @NotNull Directory updateDirectory;

    public PteroService(final String panelUrl, final String apiKey, final String serverId) {
        this.pteroClient = PteroBuilder.createClient(panelUrl, apiKey);
        this.clientServer = pteroClient.retrieveServerByIdentifier(serverId).execute();

        this.pluginsDirectory = this.clientServer.retrieveDirectory("plugins").execute();

        this.updateDirectory = this.pluginsDirectory.getDirectoryByName("update")
                .orElseGet(() -> {
                    this.pluginsDirectory.createFolder("update").execute();
                    return this.pluginsDirectory.getDirectoryByName("update").get();
                });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> uploadProcess(final @NotNull CommandSender sender) {
        sendMessage(sender, "downloading the files now!");

        final Collection<File> files = downloadFrom(DOWNLOAD_LINK, FILE_NAME);

        sendMessage(sender, "uploading the files now!");

        final UploadFileAction uploadFileAction = this.updateDirectory.upload();
        for (final File file : files) {
            uploadFileAction.addFile(file);
        }

        return CompletableFuture.supplyAsync(() -> {
            uploadFileAction.execute();
            return true;
        }, BukkitExecutor.INSTANCE);
    }

    @Override
    public void restartProcess() {
        clientServer.sendCommand("restart").execute();
    }
}
