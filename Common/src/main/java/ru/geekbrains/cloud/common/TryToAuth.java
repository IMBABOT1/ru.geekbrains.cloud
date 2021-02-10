package ru.geekbrains.cloud.common;

public class TryToAuth extends AbstractMessage {
    private String login;
    private String pass;

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public TryToAuth(String login, String pass){
        this.login = login;
        this.pass = pass;
    }
}
