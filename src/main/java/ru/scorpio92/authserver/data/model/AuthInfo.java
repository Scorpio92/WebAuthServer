package ru.scorpio92.authserver.data.model;

public class AuthInfo {

    private int accountId;
    private String authToken;

    public AuthInfo(int accountId, String authToken) {
        this.accountId = accountId;
        this.authToken = authToken;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAuthToken() {
        return authToken;
    }
}
