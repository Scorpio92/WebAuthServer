package ru.scorpio92.authserver.data.model.message.response;

public class CheckTokenServerDataResponse {

    private String authToken;
    private String accountId;
    private String login;
    private String nickname;

    public CheckTokenServerDataResponse(String authToken) {
        this.authToken = authToken;
    }

    public CheckTokenServerDataResponse(String authToken, String accountId, String login, String nickname) {
        this.authToken = authToken;
        this.accountId = accountId;
        this.login = login;
        this.nickname = nickname;
    }
}
