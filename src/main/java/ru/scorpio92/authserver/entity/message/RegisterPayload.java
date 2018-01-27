package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.EncryptablePayload;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class RegisterPayload extends EncryptablePayload {

    private String nickname;
    private String email;

    private String authToken;

    public RegisterPayload(String authToken) {
        this.authToken = authToken;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
}
