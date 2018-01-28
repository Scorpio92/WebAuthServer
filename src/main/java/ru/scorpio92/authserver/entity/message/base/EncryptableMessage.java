package ru.scorpio92.authserver.entity.message.base;

import java.security.PrivateKey;

import ru.scorpio92.authserver.crypto.RSA;


/**
 * Created by scorpio92 on 1/20/18.
 */

public abstract class EncryptableMessage extends BaseMessage {

    protected String ServerPublicKeyId;
    protected String ClientPublicKey;

    public EncryptableMessage(MessageType type, EncryptablePayload payload, String clientPublicKey) throws Exception {
        super(type, payload.getEncryptedString(clientPublicKey));
    }

    public String getServerPublicKeyId() {
        return ServerPublicKeyId;
    }

    public String getClientPublicKey() {
        return ClientPublicKey;
    }

    protected String getDecryptedPayloadString(PrivateKey privateKey) throws Exception {
        return RSA.decryptFromBase64(privateKey, Payload);
    }
}
