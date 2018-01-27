package ru.scorpio92.authserver.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by scorpio92 on 1/22/18.
 */

public class AES {

    private final static String CRYPTO_PROVIDER = "BC";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final String AES = "AES";
    private static final String KEY_FACTORY_ALG = "PBKDF2WithHmacSHA256";
    private static final String CHARSET = "UTF-8";
    private static final int KEYGEN_ITERATIONS_COUNT = 10000;
    private static final int KEY_SIZE = 256;

    enum MODE {
        INITIAL,
        PREPARED
    }

    private MODE mode;

    private String password;
    private String salt;
    private String ivString;

    private String key;
    private String iv;

    private AES(String password, String salt, String ivString) {
        this.password = password;
        this.salt = salt;
        this.ivString = ivString;
        mode = MODE.INITIAL;
    }

    private AES(String key, String iv) {
        this.key = key;
        this.iv = iv;
        mode = MODE.PREPARED;
    }

    public static AES build(String password, String salt, String ivString) throws IllegalArgumentException {
        if(password == null || salt == null || ivString == null || password.isEmpty() || salt.isEmpty() || ivString.isEmpty())
            throw new IllegalArgumentException();
        return new AES(password, salt, ivString);
    }

    public static AES build(String key, String iv) throws IllegalArgumentException {
        if(key == null || iv == null || key.isEmpty() || iv.isEmpty())
            throw new IllegalArgumentException();
        return new AES(key, iv);
    }

    public static SecretKey getKey(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALG);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(CHARSET), KEYGEN_ITERATIONS_COUNT, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), AES);
    }

    public static String getKeyString(String password, String salt) throws IllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        if(password == null || salt == null || password.isEmpty() || salt.isEmpty())
            throw new IllegalArgumentException();
        return new String(Base64.getEncoder().encode(getKey(password, salt).getEncoded()), CHARSET);
    }

    public static SecretKey convertStringToKey(String keyStr) throws IllegalArgumentException {
        if(keyStr == null || keyStr.isEmpty())
            throw new IllegalArgumentException();
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
    }

    public static String getIV(String ivString) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return SHA.getSHA256(ivString);
    }

    public byte[] encryptToBytes(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance(AES_MODE, CRYPTO_PROVIDER);
        switch (mode) {
            case INITIAL:
                cipher.init(Cipher.ENCRYPT_MODE, getKey(password, salt), new IvParameterSpec(getIV(ivString).getBytes(CHARSET)));
                break;
            case PREPARED:
                cipher.init(Cipher.ENCRYPT_MODE, convertStringToKey(key), new IvParameterSpec(iv.getBytes(CHARSET)));
                break;
        }
        return cipher.doFinal(message.getBytes(CHARSET));
    }

    public String encryptToBase64(String message) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeySpecException, NoSuchProviderException {
        return new String(Base64.getEncoder().encode(encryptToBytes(message)));
    }

    public byte[] decryptToByte(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance(AES_MODE, CRYPTO_PROVIDER);
        switch (mode) {
            case INITIAL:
                cipher.init(Cipher.DECRYPT_MODE, getKey(password, salt), new IvParameterSpec(getIV(ivString).getBytes(CHARSET)));
                break;
            case PREPARED:
                cipher.init(Cipher.DECRYPT_MODE, convertStringToKey(key), new IvParameterSpec(iv.getBytes(CHARSET)));
                break;
        }
        return cipher.doFinal(Base64.getDecoder().decode(message));
    }

    public String decryptToString(String message) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeySpecException, NoSuchProviderException {
        return new String(decryptToByte(message), CHARSET);
    }
}