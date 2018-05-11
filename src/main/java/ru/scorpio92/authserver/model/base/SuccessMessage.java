package ru.scorpio92.authserver.model.base;

public class SuccessMessage extends BaseMessage {

    public SuccessMessage(Type type) {
        super(type, Status.SUCCESS);
    }
}
