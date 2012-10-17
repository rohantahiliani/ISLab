package edu.gatech.islab.chat.utilities;

public class GoogleUser implements User {

    private String username;
    private String usernick;

    public GoogleUser(String username) {
        this.username = username;
    }

    public String getUserName() {
        return this.username;
    }

    public String getUserNick() {
        return "";
    }

    public String getLogin() {
        return "";
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
}