package ru.scorpio92.authserver.tools.security;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;

import ru.scorpio92.authserver.Constants;


public class SecRandom {

    private static final int SIZE = 64;

    public static byte[] getRandomBytes() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[SIZE];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String getRandomString() throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(getRandomBytes()), Constants.CHARSET);
    }
}