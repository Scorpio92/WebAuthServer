package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.Payload;

/**
 * Created by scorpio92 on 1/13/18.
 */

public class GetServerKeyPayload extends Payload {

    private String ServerPublicKeyId;
    private String ServerPublicKey;

    public GetServerKeyPayload(String serverPublicKeyId, String serverPublicKey) {
        this.ServerPublicKeyId = serverPublicKeyId;
        this.ServerPublicKey = serverPublicKey;
    }
}
