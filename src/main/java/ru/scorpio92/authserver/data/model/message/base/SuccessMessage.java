package ru.scorpio92.authserver.data.model.message.base;

public class SuccessMessage extends BaseMessage {

    public SuccessMessage(Type type) {
        super(type, Status.SUCCESS);
    }
}
