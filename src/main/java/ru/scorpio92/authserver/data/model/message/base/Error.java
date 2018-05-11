package ru.scorpio92.authserver.data.model.message.base;

public class Error {

    private String errorCode;
    private String message;

    public Error(int errorCode) {
        this.errorCode = String.valueOf(errorCode);
        this.message = ErrorCode.getMessageForErrorCode(errorCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
