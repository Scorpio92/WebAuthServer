package ru.scorpio92.authserver.entity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import ru.scorpio92.authserver.crypto.SHA;
import ru.scorpio92.authserver.crypto.SecRandom;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class Account {

    private String nickname;
    private String authToken;
    private String email;

    public Account(String nickname, String authToken, String email) {
        this.nickname = nickname;
        this.authToken = authToken;
        this.email = email;
    }

    public static Account create(String nickname, String email) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String authToken = SHA.getSHA256(SHA.getSHA256(nickname) + SecRandom.getRandomString());
        return new Account(nickname, authToken, email);
    }

    public String getNickname() {
        return nickname;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getEmail() {
        return email;
    }
}
