package ru.scorpio92.authserver.crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import ru.scorpio92.authserver.ServerConfigStore;

/**
 * Created by scorpio92 on 1/13/18.
 */

public class RSA {

    private final static String KEYGEN_ALG = "RSA";
    private final static String ALG = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    private final static String CRYPTO_PROVIDER = "BC";
    private final static String CHARSET = "ISO-8859-1";

    public static final int KEY_1024_BIT = 1024;
    public static final int KEY_2048_BIT = 2048;
    public static final int KEY_4096_BIT = 4096;

    public static KeyPair buildKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEYGEN_ALG);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static String encryptToBase64(PublicKey publicKey, String message) throws Exception {
        if(!ServerConfigStore.ENCRYPTION_ENABLED)
            return message;
        Cipher cipher = Cipher.getInstance(ALG, CRYPTO_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return new String(Base64.getEncoder().encode(cipher.doFinal(message.getBytes(CHARSET))));
    }

    public static byte[] encryptToBytes(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG, CRYPTO_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes(CHARSET));
    }

    public static String decryptFromBase64(PrivateKey privateKey, String encryptedBase64) throws Exception {
        if(!ServerConfigStore.ENCRYPTION_ENABLED)
            return encryptedBase64;
        Cipher cipher = Cipher.getInstance(ALG, CRYPTO_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedBase64)), CHARSET);
    }

    public static String decryptFromBytes(PrivateKey privateKey, byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG, CRYPTO_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(bytes), CHARSET);
    }

    public static String covertKeyToString(Key key) {
        return new String(Base64.getEncoder().encode(key.getEncoded()));
    }

    public static PublicKey convertStringToPublicKey(String key) throws Exception {
        byte[] byteKey = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance(KEYGEN_ALG);
        return kf.generatePublic(keySpec);
    }

    public static PrivateKey convertStringToPrivateKey(String key) throws Exception {
        byte[] byteKey = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance(KEYGEN_ALG);
        return kf.generatePrivate(keySpec);
    }
}
