package ru.scorpio92.authserver.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by scorpio92 on 1/14/18.
 */

public class SHA {

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        byte[] var2 = data;
        int var3 = data.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            int halfByte = b >>> 4 & 15;
            int var7 = 0;

            do {
                buf.append(0 <= halfByte && halfByte <= 9 ? (char) (48 + halfByte) : (char) (97 + (halfByte - 10)));
                halfByte = b & 15;
            } while (var7++ < 1);
        }

        return buf.toString();
    }

    private static String generateHash(String alg, String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(alg);
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        return convertToHex(md.digest());
    }

    public static String getSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return generateHash("SHA-1", text);
    }

    public static String getSHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return generateHash("SHA-256", text);
    }

    public static String getSHA512(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return generateHash("SHA-512", text);
    }
}
