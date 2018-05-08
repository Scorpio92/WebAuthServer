package ru.scorpio92.authserver.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static ru.scorpio92.authserver.Constants.BYTE_BUFFER;

public class IO {

    public static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] bytes;
        byte[] buffer = new byte[BYTE_BUFFER];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        bytes = result.toByteArray();
        result.flush();
        result.close();
        return bytes;
    }
}
