package ru.scorpio92.authserver.entity.message.base;

import java.security.PublicKey;

import ru.scorpio92.authserver.crypto.RSA;


/**
 * Created by scorpio92 on 1/4/18.
 */

public abstract class EncryptablePayload extends Payload {

    public String getEncryptedString(String publicKeyStr) throws Exception {
        PublicKey publicKey = RSA.convertStringToPublicKey(publicKeyStr);
        return RSA.encryptToBase64(publicKey, toString());
    }
}
