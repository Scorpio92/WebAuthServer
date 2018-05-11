package ru.scorpio92.authserver.model.base;

public class ErrorMessage extends BaseMessage {

    private Error error;

    public ErrorMessage(Type type, int errorCode) {
        super(type, Status.ERROR);
        this.error = new Error(errorCode);
    }
}
