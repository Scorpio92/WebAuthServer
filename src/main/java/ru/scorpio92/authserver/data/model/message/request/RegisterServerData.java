package ru.scorpio92.authserver.data.model.message.request;

public class RegisterServerData {

    public static final String LOGIN_REGEXP = "^[A-Za-z0-9]{3,20}$";
    public static final String PASSWORD_REGEXP = "^(.){8,64}$";
    public static final String NICKNAME_REGEXP = "^([^\\s]+?\\s?[^\\s]+?)+?{3,20}$";

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
