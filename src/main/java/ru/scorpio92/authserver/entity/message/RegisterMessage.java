package ru.scorpio92.authserver.entity.message;

import java.security.PrivateKey;

import ru.scorpio92.authserver.entity.message.base.EncryptableMessage;
import ru.scorpio92.authserver.entity.message.base.IEncryptableMessage;
import ru.scorpio92.authserver.tools.JsonWorker;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class RegisterMessage extends EncryptableMessage implements IEncryptableMessage<RegisterPayload> {

    public RegisterMessage(RegisterPayload payload, String publicKey) throws Exception {
        super(MessageType.REGISTER, payload, publicKey);
    }

    @Override
    public RegisterPayload getPayload(PrivateKey privateKey) throws Exception {
        return JsonWorker.getDeserializeJson(getDecryptedPayloadString(privateKey), RegisterPayload.class);
    }
}
