package io.tofpu.speedbridgeupdater.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FileDownloader {
    public static Collection<File> downloadFrom(@NotNull String link,
            final @NotNull String fileName) {
        URL url;
        HttpURLConnection http;
        try {
            url = new URL(link);
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        Map<String, List<String>> header = http.getHeaderFields();
        while (isRedirected(header)) {
            link = header.get("Location").get(0);
            try {
                url = new URL(link);
                http = (HttpURLConnection) url.openConnection();
                header = http.getHeaderFields();
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }

        InputStream input = null;
        try {
            input = http.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        // TODO: improve this (don't break it!!!!)
        byte[] buffer = new byte[4096];
        int n = -1;
        OutputStream output = null;
        try {
            output = new FileOutputStream(fileName);
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }


        final File downloadedFile = new File(fileName);
        final Collection<File> files = new ArrayList<>();
        if (fileName.contains("zip")) {
            files.addAll(zipToFiles(downloadedFile));
        } else {
            files.add(downloadedFile);
        }

        return files;
    }

    private static Collection<File> zipToFiles(final File fileZip) {
        final List<File> files = new ArrayList<>();

        try (final ZipFile zipFile = new ZipFile(fileZip)) {
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                final File entryDestination = new File(entry.getName());

                files.add(entryDestination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    private static boolean isRedirected(final Map<String, List<String>> header) {
        for (String hv : header.get(null)) {
            if (hv.contains(" 301 ") || hv.contains(" 302 ")) return true;
        }
        return false;
    }
}
