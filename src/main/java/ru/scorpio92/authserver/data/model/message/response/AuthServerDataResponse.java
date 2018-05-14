package ru.scorpio92.authserver.data.model.message.response;

public class AuthServerDataResponse {

    private String authToken;

    public AuthServerDataResponse(String authToken) {
        this.authToken = authToken;
    }
}
