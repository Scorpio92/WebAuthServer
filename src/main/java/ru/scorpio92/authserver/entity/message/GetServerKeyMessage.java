package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.Message;

/**
 * Created by scorpio92 on 1/13/18.
 */

public class GetServerKeyMessage extends Message {

    public GetServerKeyMessage(GetServerKeyPayload payload) {
        super(MessageType.GET_SERVER_KEY, payload);
    }
}
