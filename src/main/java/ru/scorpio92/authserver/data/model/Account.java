package ru.scorpio92.authserver.data.model;

public class Account {

    private String login;
    private String passwordHash;
    private String nickname;
    private String email;

    public Account(String login, String passwordHash, String nickname) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
    }

    public Account(String login, String passwordHash, String nickname, String email) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.email = email;
    }


    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
}
