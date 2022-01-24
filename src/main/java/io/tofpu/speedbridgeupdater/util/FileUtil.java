package io.tofpu.speedbridgeupdater.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUtil {
    private static final byte[] buffer = new byte[1024];

    public static void copy(final InputStream inputStream,
            final OutputStream outputStream) throws IOException {
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) >= 0) {
            //Do whatever you need with the bytes here
            outputStream.write(buffer, 0, bytesRead);
        }
    }
}
