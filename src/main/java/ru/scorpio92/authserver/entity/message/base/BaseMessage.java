package ru.scorpio92.authserver.entity.message.base;

import ru.scorpio92.authserver.tools.JsonWorker;

import static ru.scorpio92.authserver.entity.message.base.BaseMessage.Status.SUCCESS;

/**
 * Created by scorpio92 on 1/4/18.
 */

public class BaseMessage {

    public enum MessageType {
        GET_SERVER_KEY,
        REGISTER,
        AUTHORIZE
    }

    public enum Status {
        SUCCESS,
        ERROR
    }

    private String Type;
    protected String Status;
    protected String ErrorCode;
    protected String Payload;

    public BaseMessage() {
    }

    public BaseMessage(MessageType type, String payload) {
        this.Type = type.name();
        this.Payload = payload;
        Status = SUCCESS.name();
    }

    public MessageType getType() {
        return Enum.valueOf(MessageType.class, Type);
    }

    @Override
    public String toString() {
        return JsonWorker.getSerializeJson(this);
    }
}
