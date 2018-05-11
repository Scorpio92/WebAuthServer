package ru.scorpio92.authserver.data.model.message.request;

public class RegisterServerData {

    private String nickname;
    private String login;
    private String password;

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
