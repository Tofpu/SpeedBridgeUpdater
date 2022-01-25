package io.tofpu.speedbridgeupdater.domain.extend;

import com.google.common.io.Files;
import io.tofpu.speedbridgeupdater.domain.AbstractUpdateService;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public final class LocalService extends AbstractUpdateService {
    private final @NotNull Server server;
    private final @NotNull File updateDirectory;

    public LocalService(final @NotNull Server server) {
        this.server = server;
        this.updateDirectory = server.getUpdateFolderFile();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> uploadProcess(final @NotNull CommandSender sender) {
        sendMessage(sender, "downloading the files now!");

        final Collection<File> files = downloadFrom(DOWNLOAD_LINK, FILE_NAME);

        if (!updateDirectory.exists()) {
            updateDirectory.mkdirs();
        }

        sendMessage(sender, "uploading the files now!");

        final AtomicBoolean moveResult = new AtomicBoolean(true);
        files.forEach(file -> {
            try {
                Files.move(file, new File(updateDirectory, file.getName()));
            } catch (IOException e) {
                moveResult.set(false);
                e.printStackTrace();
            }
        });

        return CompletableFuture.completedFuture(moveResult.get());
    }

    @Override
    public void restartProcess() {
        final CommandSender consoleSender = server.getConsoleSender();
        server.dispatchCommand(consoleSender, "restart");
    }
}
