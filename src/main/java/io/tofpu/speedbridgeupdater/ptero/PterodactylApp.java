package io.tofpu.speedbridgeupdater.ptero;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Directory;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.managers.UploadFileAction;
import io.tofpu.speedbridgeupdater.executor.BukkitExecutor;
import io.tofpu.speedbridgeupdater.util.FileDownloader;
import io.tofpu.speedbridgeupdater.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class PterodactylApp {
    private static final String TEST_LINK = "http://localhost:3000/favicon.zip";
    private static final String DOWNLOAD_LINK = "https://speedbridge.tofpu.me/download";

    private final @NotNull PteroClient pteroClient;
    private final @NotNull ClientServer clientServer;
    private final @NotNull Directory pluginsDirectory;
    private final @NotNull Directory updateDirectory;

    private final Map<String, File> fileMap;

    private boolean sentRequest = false;

    public PterodactylApp(final String panelUrl, final String apiKey, final String serverId) throws IOException {
        this.pteroClient = PteroBuilder.createClient(panelUrl, apiKey);
        this.clientServer = pteroClient.retrieveServerByIdentifier(serverId).execute();

        this.pluginsDirectory = this.clientServer.retrieveDirectory("plugins").execute();

        this.updateDirectory = this.pluginsDirectory.getDirectoryByName("update")
                .orElseGet(() -> {
                    this.pluginsDirectory.createFolder("update").execute();
                    return this.pluginsDirectory.getDirectoryByName("update").get();
                });

        this.fileMap = new ConcurrentHashMap<>();
    }

    public void sendUpdateRequest(final CommandSender sender) {
        this.sentRequest = true;
        BukkitExecutor.INSTANCE.execute(() -> {
            final File downloadFile = FileDownloader.downloadFrom(DOWNLOAD_LINK,
                    "SpeedBridge-2.jar");
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (final ZipFile zipFile = new ZipFile(downloadFile)) {
                final Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    final ZipEntry entry = entries.nextElement();
                    final File entryDestination = new File(entry.getName());

                    fileMap.put(entry.getName(), entryDestination);

                    if (entry.isDirectory()) {
                        updateDirectory.createFolder(entry.getName()).execute();
                        entryDestination.mkdirs();
                    } else {
                        try (InputStream in = zipFile.getInputStream(entry); OutputStream out = new FileOutputStream(entryDestination)) {

                            FileUtil.copy(in, out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            final UploadFileAction uploadFileAction = this.updateDirectory.upload();
            for (final File file : fileMap.values()) {
                uploadFileAction.addFile(file);
            }

            Bukkit.getLogger().info("Uploading the files now!");
            sender.sendMessage("Uploading the files now!");
            uploadFileAction.execute();

            Bukkit.getLogger().info("Restarting the server now");
            sender.sendMessage("Restarting the server now");
            clientServer.sendCommand("restart").execute();
        });
    }

    public boolean hasSentRequest() {
        return sentRequest;
    }
}
