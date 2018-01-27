package ru.scorpio92.authserver.entity;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import ru.scorpio92.authserver.crypto.RSA;
import ru.scorpio92.authserver.crypto.SHA;

/**
 * Created by scorpio92 on 1/14/18.
 */

public class ServerKeyPair {

    private String pairId;
    private String publicKey;
    private PrivateKey privateKey;
    private long createTime;

    public ServerKeyPair(String pairId, String publicKey, PrivateKey privateKey, long createTime) {
        this.pairId = pairId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.createTime = createTime;
    }

    public static ServerKeyPair build() throws Exception {
        KeyPair keyPair = RSA.buildKeyPair(RSA.KEY_2048_BIT);
        String publicKey = RSA.covertKeyToString(keyPair.getPublic());
        String pairId = SHA.getSHA1(publicKey);
        long createTime = System.currentTimeMillis();
        return new ServerKeyPair(pairId, publicKey, keyPair.getPrivate(), createTime);
    }

    public String getPairId() {
        return pairId;
    }

    public String getPublicKeyStr() {
        return publicKey;
    }

    public PublicKey getPublicKey() throws Exception {
        return RSA.convertStringToPublicKey(publicKey);
    }

    public String getPrivateKeyStr() {
        return RSA.covertKeyToString(privateKey);
    }

    public PrivateKey getPrivateKey() throws Exception {
        return privateKey;
    }

    public long getCreateTime() {
        return createTime;
    }
}
