package ru.scorpio92.authserver.entity.message.base;

/**
 * Created by scorpio92 on 1/20/18.
 */

public abstract class Message extends BaseMessage {

    public Message(MessageType type, Payload payload) {
        super(type, payload.toString());
    }
}
