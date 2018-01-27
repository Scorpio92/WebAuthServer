package ru.scorpio92.authserver.entity.message.base;

import java.security.PrivateKey;

import ru.scorpio92.authserver.crypto.RSA;


/**
 * Created by scorpio92 on 1/20/18.
 */

public abstract class EncryptableMessage extends BaseMessage {

    protected String serverPublicKeyId;
    protected String clientPublicKey;

    public EncryptableMessage(MessageType type, EncryptablePayload payload, String clientPublicKey) throws Exception {
        super(type, payload.getEncryptedString(clientPublicKey));
    }

    public String getServerPublicKeyId() {
        return serverPublicKeyId;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    protected String getDecryptedPayloadString(PrivateKey privateKey) throws Exception {
        return RSA.decryptFromBase64(privateKey, payload);
    }
}
