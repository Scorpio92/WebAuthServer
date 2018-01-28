package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.EncryptablePayload;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class AuthorizePayload extends EncryptablePayload {

    private String AuthToken;

    private String SessionKey;

    public AuthorizePayload(String sessionKey) {
        this.SessionKey = sessionKey;
    }

    public String getAuthToken() {
        return AuthToken;
    }
}
