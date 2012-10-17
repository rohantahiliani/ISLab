package edu.gatech.islab.chat.utilities;

public class GoogleUser implements User {

    private String username;
    private String login;

    public GoogleUser(String login, String username) {
        this.username = username;
        this.login = login;
    }

    public String getUserName() {
        return this.username;
    }

    public String getUserNick() {
        return "";
    }

    public String getLogin() {
        return this.login;
    }
    
    public boolean hasGoogleLogin() {
        return true;
    }

    public boolean hasFacebookLogin() {
        return true;
    }

    public boolean hasYahooLogin() {
        return true;
    }

    @Override
    public String toString() {
        return this.username + " " + this.login;
    }
}