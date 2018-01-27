package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.EncryptablePayload;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class AuthorizePayload extends EncryptablePayload {

    private String authToken;

    private String sessionKey;

    public AuthorizePayload(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getAuthToken() {
        return authToken;
    }
}
