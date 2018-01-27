package ru.scorpio92.authserver.entity.message;

import java.security.PrivateKey;

import ru.scorpio92.authserver.entity.message.base.EncryptableMessage;
import ru.scorpio92.authserver.entity.message.base.IEncryptableMessage;
import ru.scorpio92.authserver.tools.JsonWorker;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class AuthorizeMessage extends EncryptableMessage implements IEncryptableMessage<AuthorizePayload> {

    private String IV;

    public AuthorizeMessage(String IV, AuthorizePayload payload, String publicKey) throws Exception {
        super(MessageType.AUTHORIZE, payload, publicKey);
        this.IV = IV;
    }

    @Override
    public AuthorizePayload getPayload(PrivateKey privateKey) throws Exception {
        return JsonWorker.getDeserializeJson(getDecryptedPayloadString(privateKey), AuthorizePayload.class);
    }
}
