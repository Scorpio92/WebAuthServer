package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.BaseMessage;

/**
 * Created by scorpio92 on 1/31/18.
 */

public class ServiceAPIMessage extends BaseMessage {

    private String SessionKeyId;

    public String getSessionKeyId() {
        return SessionKeyId;
    }

    public String getPayload() {
        return this.Payload;
    }
}
