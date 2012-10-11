package edu.gatech.islab.chat.utilities;

public interface User {
    public String getUserName();

    public String getUserNick();

    public boolean hasGoogleLogin();

    public boolean hasFacebookLogin();

    public boolean hasYahooLogin();
}