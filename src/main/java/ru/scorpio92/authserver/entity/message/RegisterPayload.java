package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.EncryptablePayload;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class RegisterPayload extends EncryptablePayload {

    private String Nickname;
    private String Email;

    private String AuthToken;

    public RegisterPayload(String authToken) {
        this.AuthToken = authToken;
    }

    public String getNickname() {
        return Nickname;
    }

    public String getEmail() {
        return Email;
    }
}
