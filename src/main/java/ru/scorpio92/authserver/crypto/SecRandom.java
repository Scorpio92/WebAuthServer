package ru.scorpio92.authserver.crypto;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by scorpio92 on 1/23/18.
 */

public class SecRandom {

    private static final int SIZE = 64;
    private static final String CHARSET = "UTF-8";

    public static byte[] getRandomBytes() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[SIZE];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String getRandomString() throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(getRandomBytes()), CHARSET);
    }
}
