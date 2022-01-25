package io.tofpu.speedbridgeupdater.domain;

import io.tofpu.speedbridgeupdater.util.FileDownloader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractUpdateService {
    protected static final @NotNull String DOWNLOAD_LINK = "http://localhost:3000/favicon" + ".zip";
//    protected static final @NotNull String DOWNLOAD_LINK = "https://speedbridge.tofpu" + ".me/download";
    protected static final @NotNull String FILE_NAME = "SpeedBridge-2.zip";

    protected boolean requestSent;

    public Collection<File> downloadFrom(final @NotNull String url, final @NotNull String fileName) {
        return FileDownloader.downloadFrom(url, fileName);
    }

    public void startOperation(final @NotNull CommandSender sender) {
        if (requestSent) {
            sendMessage(sender, "The operation cannot be invoked twice. You will need " + "to restart.");
            return;
        }

        uploadProcess(sender).whenComplete((aBoolean, throwable) -> {
            if (throwable != null) {
                throw new IllegalStateException(throwable);
            }

            if (aBoolean) {
                sendMessage(sender, "The operation has been uploaded successfully.");
            } else {
                sendMessage(sender, "The operation may have failed. Please " + "double-check" + " in your updates folder.");
            }

            restartProcess();
        });
    }

    public void sendMessage(final @NotNull CommandSender sender, final @NotNull String message) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(message);
        }
        Bukkit.getLogger().info(message);
    }

    public abstract @NotNull CompletableFuture<Boolean> uploadProcess(final @NotNull CommandSender sender);

    public abstract void restartProcess();

    public boolean hasRequestBeenSent() {
        return requestSent;
    }
}
