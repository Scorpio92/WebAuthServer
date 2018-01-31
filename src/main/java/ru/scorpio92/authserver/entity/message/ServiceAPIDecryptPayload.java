package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.Payload;

/**
 * Created by scorpio92 on 1/31/18.
 */

public class ServiceAPIDecryptPayload extends Payload {

    private String AuthToken;

    public String getAuthToken() {
        return AuthToken;
    }
}
