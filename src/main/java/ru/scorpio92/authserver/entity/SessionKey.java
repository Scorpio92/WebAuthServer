package ru.scorpio92.authserver.entity;

import ru.scorpio92.authserver.crypto.AES;
import ru.scorpio92.authserver.crypto.SHA;
import ru.scorpio92.authserver.crypto.SecRandom;

/**
 * Created by scorpio92 on 1/22/18.
 */

public class SessionKey {

    private String sessionKeyId;
    private String sessionKey;
    private String IV;

    public SessionKey(String sessionKeyId, String sessionKey, String IV) {
        this.sessionKeyId = sessionKeyId;
        this.sessionKey = sessionKey;
        this.IV = IV;
    }

    public static SessionKey build(String authToken, String clientPublicKey) throws Exception {
        String key = AES.getKeyString(SHA.getSHA256(SHA.getSHA256(authToken) + SecRandom.getRandomString()), clientPublicKey);
        String keyId = SHA.getSHA1(key);
        String IV = AES.getIV(clientPublicKey);
        return new SessionKey(keyId, key, IV);
    }

    public String getSessionKeyId() {
        return sessionKeyId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getIV() {
        return IV;
    }
}
