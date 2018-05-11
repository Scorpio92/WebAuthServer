package ru.scorpio92.authserver.data.model.message.base;

public class BaseMessage {

    public enum Type {
        REGISTER,
        AUTHORIZE,
        DEAUTHORIZE
    }

    public enum Status {
        SUCCESS,
        ERROR
    }

    private String type;
    private String status;
    private String clientData;
    private String serverData;

    public BaseMessage() {
    }

    public BaseMessage(Type type, Status status) {
        this.type = type.name();
        this.status = status.name();
    }


    public Type getType() {
        return Enum.valueOf(Type.class, type);
    }

    public String getStatus() {
        return status;
    }

    public String getClientData() {
        return clientData;
    }

    public String getServerData() {
        return serverData;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }

    public void setServerData(String serverData) {
        this.serverData = serverData;
    }
}
